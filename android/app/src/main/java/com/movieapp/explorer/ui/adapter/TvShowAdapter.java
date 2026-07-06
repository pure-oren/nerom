package com.movieapp.explorer.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.movieapp.explorer.data.model.TvShow;
import com.movieapp.explorer.databinding.ItemTvShowBinding;
import java.util.ArrayList;
import java.util.List;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.TvShowViewHolder> {
    private List<TvShow> tvShows = new ArrayList<>();
    private OnTvShowClickListener listener;
    
    public interface OnTvShowClickListener {
        void onTvShowClick(TvShow tvShow);
    }
    
    public TvShowAdapter(OnTvShowClickListener listener) {
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public TvShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTvShowBinding binding = ItemTvShowBinding.inflate(
            LayoutInflater.from(parent.getContext()),
            parent,
            false
        );
        return new TvShowViewHolder(binding);
    }
    
    @Override
    public void onBindViewHolder(@NonNull TvShowViewHolder holder, int position) {
        holder.bind(tvShows.get(position));
    }
    
    @Override
    public int getItemCount() {
        return tvShows.size();
    }
    
    public void setTvShows(List<TvShow> tvShows) {
        this.tvShows = tvShows;
        notifyDataSetChanged();
    }
    
    public class TvShowViewHolder extends RecyclerView.ViewHolder {
        private final ItemTvShowBinding binding;
        
        public TvShowViewHolder(@NonNull ItemTvShowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        
        public void bind(TvShow tvShow) {
            binding.tvShowTitle.setText(tvShow.name);
            binding.tvShowRating.setText(String.format("★ %.1f", tvShow.voteAverage));
            binding.tvShowYear.setText(tvShow.firstAirDate != null ? 
                tvShow.firstAirDate.substring(0, 4) : "N/A");
            binding.tvShowSeasons.setText(String.format("Seasons: %d", tvShow.numberOfSeasons));
            
            // Load poster using Glide
            String posterUrl = "https://image.tmdb.org/t/p/w500" + tvShow.posterPath;
            Glide.with(itemView.getContext())
                .load(posterUrl)
                .into(binding.tvShowPoster);
            
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTvShowClick(tvShow);
                }
            });
        }
    }
}
