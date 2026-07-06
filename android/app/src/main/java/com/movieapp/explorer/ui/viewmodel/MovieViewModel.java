package com.movieapp.explorer.ui.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.movieapp.explorer.data.model.Movie;
import com.movieapp.explorer.data.repository.MovieRepository;
import java.util.List;

public class MovieViewModel extends AndroidViewModel {
    private final MovieRepository repository;
    private final MutableLiveData<Integer> currentPage = new MutableLiveData<>(1);
    private final MutableLiveData<String> sortBy = new MutableLiveData<>("popularity.desc");
    private final MutableLiveData<String> selectedGenres = new MutableLiveData<>("");
    private final MutableLiveData<String> selectedStudios = new MutableLiveData<>("");
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    
    private LiveData<List<Movie>> movies;
    private LiveData<List<Movie>> pinProtectedMovies;
    
    public MovieViewModel(Application application) {
        super(application);
        this.repository = new MovieRepository(application);
        this.movies = repository.getPopularMovies(1);
        this.pinProtectedMovies = repository.getPinProtectedMovies();
    }
    
    public LiveData<List<Movie>> getPopularMovies() {
        movies = repository.getPopularMovies(currentPage.getValue() != null ? currentPage.getValue() : 1);
        return movies;
    }
    
    public LiveData<List<Movie>> getTopRatedMovies() {
        movies = repository.getTopRatedMovies(currentPage.getValue() != null ? currentPage.getValue() : 1);
        return movies;
    }
    
    public LiveData<List<Movie>> getUpcomingMovies() {
        movies = repository.getUpcomingMovies(currentPage.getValue() != null ? currentPage.getValue() : 1);
        return movies;
    }
    
    public LiveData<List<Movie>> searchMovies(String query) {
        searchQuery.setValue(query);
        movies = repository.searchMovies(query, 1);
        return movies;
    }
    
    public LiveData<List<Movie>> discoverMovies() {
        String genres = selectedGenres.getValue() != null ? selectedGenres.getValue() : "";
        String studios = selectedStudios.getValue() != null ? selectedStudios.getValue() : "";
        String sort = sortBy.getValue() != null ? sortBy.getValue() : "popularity.desc";
        
        movies = repository.discoverMovies(genres, studios, sort, 1);
        return movies;
    }
    
    public LiveData<Movie> getMovieDetails(int movieId) {
        return repository.getMovieDetails(movieId);
    }
    
    public void setPinProtected(Movie movie, boolean isPinProtected) {
        repository.setPinProtected(movie, isPinProtected);
    }
    
    public LiveData<List<Movie>> getPinProtectedMovies() {
        return pinProtectedMovies;
    }
    
    public void setCurrentPage(int page) {
        currentPage.setValue(page);
    }
    
    public void setSortBy(String sort) {
        sortBy.setValue(sort);
    }
    
    public void setSelectedGenres(String genres) {
        selectedGenres.setValue(genres);
    }
    
    public void setSelectedStudios(String studios) {
        selectedStudios.setValue(studios);
    }
    
    public LiveData<Integer> getCurrentPage() {
        return currentPage;
    }
    
    public LiveData<String> getSortBy() {
        return sortBy;
    }
    
    public LiveData<String> getSelectedGenres() {
        return selectedGenres;
    }
    
    public LiveData<String> getSelectedStudios() {
        return selectedStudios;
    }
}
