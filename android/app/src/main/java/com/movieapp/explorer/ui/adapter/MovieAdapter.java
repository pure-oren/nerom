package com.movieapp.explorer.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.movieapp.explorer.data.model.Movie;
import com.movieapp.explorer.databinding.ItemMovieBinding;
import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<Movie> movies = new ArrayList<>();
    private OnMovieClickListener listener;
    
    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }
    
    public MovieAdapter(OnMovieClickListener listener) {
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMovieBinding binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.getContext()),
            parent,
            false
        );
        return new MovieViewHolder(binding);
    }
    
    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bind(movies.get(position));
    }
    
    @Override
    public int getItemCount() {
        return movies.size();
    }
    
    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }
    
    public class MovieViewHolder extends RecyclerView.ViewHolder {
        private final ItemMovieBinding binding;
        
        public MovieViewHolder(@NonNull ItemMovieBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        
        public void bind(Movie movie) {
            binding.movieTitle.setText(movie.title);
            binding.movieRating.setText(String.format("★ %.1f", movie.voteAverage));
            binding.movieYear.setText(movie.releaseDate != null ? 
                movie.releaseDate.substring(0, 4) : "N/A");
            
            // Load poster using Glide
            String posterUrl = "https://image.tmdb.org/t/p/w500" + movie.posterPath;
            Glide.with(itemView.getContext())
                .load(posterUrl)
                .into(binding.moviePoster);
            
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMovieClick(movie);
                }
            });
        }
    }
}
