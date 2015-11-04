package com.debugcomputercompany.sfotipymusic.fragments;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
//import android.app.Fragment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.debugcomputercompany.sfotipymusic.R;
import com.debugcomputercompany.sfotipymusic.adapters.SongAdapter;
import com.debugcomputercompany.sfotipymusic.models.Song;
import com.debugcomputercompany.sfotipymusic.services.MusicService;
import com.debugcomputercompany.sfotipymusic.services.MusicService.MusicBinder;

import java.util.ArrayList;

public class SongFragment extends Fragment implements RecyclerViewClickListener{

    //private OnFragmentInteractionListener mListener;
    final String TAG = "SongFragment";

    private RecyclerView mRecyclerView;
    private SongAdapter mAdapter;
    private ArrayList<Song> mSongs;

    private MusicService mMusicService;
    private Intent mPlayIntent;
    private boolean mMusicBound = false;

    private ServiceConnection mMusicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            mMusicService = binder.getService();
            mMusicService.setList(mSongs);
            mMusicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMusicBound = false;
        }
    };

    public static SongFragment newInstance() {
        SongFragment fragment = new SongFragment();
        return fragment;
    }

    public SongFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mPlayIntent == null){
            mPlayIntent = new Intent(this.getContext(),MusicService.class);
            getContext().bindService(mPlayIntent,mMusicConnection, Context.BIND_AUTO_CREATE);
            getContext().startService(mPlayIntent);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSongs = new ArrayList<Song>();

        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Log.i(TAG, "Querying media...");
        Log.i(TAG, "URI: " + uri.toString());

        Cursor cur = getContext().getContentResolver().query(uri, null, MediaStore.Audio.Media.IS_MUSIC + " = 1", null, null);
        Log.i(TAG, "Query finished. " + (cur == null ? "Returned NULL." : "Returned a cursor."));

        if (cur == null) {
            // Query failed...
            Log.e(TAG, "Failed to retrieve music: cursor is null :-(");
            return;
        }
        if (!cur.moveToFirst()) {
            // Nothing to query. There is no music on the device. How boring.
            Log.e(TAG, "Failed to move cursor to first row (no query results).");
            return;
        }
        Log.i(TAG, "Listing...");
        int artistColumn = cur.getColumnIndex(MediaStore.Audio.Media.ARTIST);
        int titleColumn = cur.getColumnIndex(MediaStore.Audio.Media.TITLE);
        //int albumColumn = cur.getColumnIndex(MediaStore.Audio.Media.ALBUM);
        //int durationColumn = cur.getColumnIndex(MediaStore.Audio.Media.DURATION);
        int idColumn = cur.getColumnIndex(MediaStore.Audio.Media._ID);
        Log.i(TAG, "Title column index: " + String.valueOf(titleColumn));
        Log.i(TAG, "ID column index: " + String.valueOf(titleColumn));

        do {
            Log.i(TAG, "ID: " + cur.getString(idColumn) + " Title: " + cur.getString(titleColumn));
            Song song1 = new Song();
            song1.setSongName(cur.getString(titleColumn));
            song1.setSongArtist(cur.getString(artistColumn));
            song1.setSongStarts((float) 1.5);
            song1.setId(cur.getLong(idColumn));
            mSongs.add(song1);

            /*mItems.add(new Item(
                    cur.getLong(idColumn),
                    cur.getString(artistColumn),
                    cur.getString(titleColumn),
                    cur.getString(albumColumn),
                    cur.getLong(durationColumn)));*/
        } while (cur.moveToNext());
        Log.i(TAG, "Done querying media. MusicRetriever is ready.");



/*
        Song song1 = new Song();
        song1.setSongName("cancion1");
        song1.setSongArtist("@arjona");
        song1.setSongStarts((float) 1.5);
        //MediaStore.Audio.Media.DATA

        song1.setId(Long.parseLong("badnews.m4a"));

        Song song2 = new Song();
        song2.setSongName("cancion2");
        song2.setSongArtist("@arjonasda");
        song2.setSongStarts(4);
        song2.setId(Long.parseLong("badnews"));

        Song song3 = new Song();
        song3.setSongName("asd");
        song3.setSongArtist("@ll");
        song3.setSongStarts(3);
        song3.setId(Long.parseLong("badnews.m4a"));

        Song song4 = new Song();
        song4.setSongName("1231");
        song4.setSongArtist("@111");
        song4.setSongStarts(2);
        song4.setId(Long.parseLong("badnews.m4a"));

        mSongs.add(song1);
        mSongs.add(song2);
        mSongs.add(song3);
        mSongs.add(song4);*/

        mAdapter = new SongAdapter(mSongs, R.layout.song_row, this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song, container, false);

        // Set the adapter
        mRecyclerView = (RecyclerView) view.findViewById(R.id.song_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        /*
        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);*/

        return view;
    }



    @Override
    public void recyclerViewListClicked(View v, int position)
    {
        mMusicService.setSong(position);
        mMusicService.playSong();
    }

    public void stopMusic(){
        getContext().stopService(mPlayIntent);
        //mMusicService = null;
        //System.exit(0);
    }

    @Override
    public void onDestroy() {
        getContext().stopService(mPlayIntent);
        mMusicService = null;
        super.onDestroy();
    }
}
