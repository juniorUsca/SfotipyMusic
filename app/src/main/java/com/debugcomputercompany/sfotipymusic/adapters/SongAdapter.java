package com.debugcomputercompany.sfotipymusic.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.debugcomputercompany.sfotipymusic.R;
import com.debugcomputercompany.sfotipymusic.fragments.SongFragment;
import com.debugcomputercompany.sfotipymusic.models.Song;

import java.util.ArrayList;

/**
 * Created by Junior on 30/10/2015.
 */
public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder>{

    private ArrayList<Song> songs;
    private int itemLayout;
    private static SongFragment itemListener;

    public SongAdapter(ArrayList<Song> songs, int itemLayout, SongFragment itemListener) {
        this.songs = songs;
        this.itemLayout = itemLayout;
        this.itemListener = itemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.songName.setText(song.getSongName());
        holder.songArtist.setText(song.getSongArtist());
        holder.songStarts.setRating(song.getSongStarts());
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView songName;
        public TextView songArtist;
        public RatingBar songStarts;

        public ViewHolder(View itemView) {
            super(itemView);

            songName = (TextView) itemView.findViewById(R.id.name_song);
            songArtist = (TextView) itemView.findViewById(R.id.artist_song);
            songStarts = (RatingBar) itemView.findViewById(R.id.rating_song);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemListener.recyclerViewListClicked(v,this.getLayoutPosition());
        }
    }
}

