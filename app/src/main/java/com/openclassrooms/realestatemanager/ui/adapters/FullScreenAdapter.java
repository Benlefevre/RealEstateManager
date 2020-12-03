package com.openclassrooms.realestatemanager.ui.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.openclassrooms.realestatemanager.databinding.FullscreenItemBinding;
import com.openclassrooms.realestatemanager.ui.viewholder.FullScreenViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FullScreenAdapter extends RecyclerView.Adapter<FullScreenViewHolder> {

    private final List<Uri> mUris;
    private Map<Integer, SimpleExoPlayer> players = new HashMap();
    private Map<Integer, MediaItem> items = new HashMap<>();

    public FullScreenAdapter(List<Uri> uris) {
        mUris = uris;
    }

    @NonNull
    @Override
    public FullScreenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        FullscreenItemBinding binding = FullscreenItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new FullScreenViewHolder(binding, context);
    }

    @Override
    public void onBindViewHolder(@NonNull FullScreenViewHolder holder, int position) {
        holder.bindPicturesInCorrespondingView(mUris.get(position));
        if (mUris.get(position).toString().contains("mp4") || mUris.get(position).toString().contains("VID")) {
            players.put(position, holder.mPlayer);
        }
    }

    @Override
    public int getItemCount() {
        return mUris.size();
    }

    @Override
    public void onViewRecycled(@NonNull FullScreenViewHolder holder) {
        super.onViewRecycled(holder);
        holder.releasePlayer();
    }


    public void pausePlayer(int lastPosition) {
        if (players.get(lastPosition) != null) {
            Log.i("player", "paused last player");
            players.get(lastPosition).setPlayWhenReady(false);
            MediaItem item = players.get(lastPosition).getMediaItemAt(0);
            Uri uri = item.playbackProperties.uri;
            Log.i("player", "URI = " + uri);
            items.put(lastPosition, item);
            players.get(lastPosition).clearMediaItems();
        }
    }

    public void reloadPlayer(int newPosition) {
        if (players.get(newPosition) != null) {
            if (players.get(newPosition).getMediaItemCount() == 0) {
                Log.i("player", "prepare new player");
                players.get(newPosition).setMediaItem(items.get(newPosition));
                players.get(newPosition).prepare();
            }
        }
    }
}
