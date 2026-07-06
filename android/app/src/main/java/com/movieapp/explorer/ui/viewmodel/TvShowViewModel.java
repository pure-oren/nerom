package com.movieapp.explorer.ui.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.movieapp.explorer.data.model.TvShow;
import com.movieapp.explorer.data.repository.TvShowRepository;
import java.util.List;

public class TvShowViewModel extends AndroidViewModel {
    private final TvShowRepository repository;
    private final MutableLiveData<Integer> currentPage = new MutableLiveData<>(1);
    private final MutableLiveData<String> sortBy = new MutableLiveData<>("popularity.desc");
    private final MutableLiveData<String> selectedGenres = new MutableLiveData<>("");
    private final MutableLiveData<String> selectedNetworks = new MutableLiveData<>("");
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    
    private LiveData<List<TvShow>> tvShows;
    private LiveData<List<TvShow>> pinProtectedTvShows;
    
    public TvShowViewModel(Application application) {
        super(application);
        this.repository = new TvShowRepository(application);
        this.tvShows = repository.getPopularTvShows(1);
        this.pinProtectedTvShows = repository.getPinProtectedTvShows();
    }
    
    public LiveData<List<TvShow>> getPopularTvShows() {
        tvShows = repository.getPopularTvShows(currentPage.getValue() != null ? currentPage.getValue() : 1);
        return tvShows;
    }
    
    public LiveData<List<TvShow>> getTopRatedTvShows() {
        tvShows = repository.getTopRatedTvShows(currentPage.getValue() != null ? currentPage.getValue() : 1);
        return tvShows;
    }
    
    public LiveData<List<TvShow>> getOnTheAirTvShows() {
        tvShows = repository.getOnTheAirTvShows(currentPage.getValue() != null ? currentPage.getValue() : 1);
        return tvShows;
    }
    
    public LiveData<List<TvShow>> searchTvShows(String query) {
        searchQuery.setValue(query);
        tvShows = repository.searchTvShows(query, 1);
        return tvShows;
    }
    
    public LiveData<List<TvShow>> discoverTvShows() {
        String genres = selectedGenres.getValue() != null ? selectedGenres.getValue() : "";
        String networks = selectedNetworks.getValue() != null ? selectedNetworks.getValue() : "";
        String sort = sortBy.getValue() != null ? sortBy.getValue() : "popularity.desc";
        
        tvShows = repository.discoverTvShows(genres, networks, sort, 1);
        return tvShows;
    }
    
    public LiveData<TvShow> getTvShowDetails(int tvShowId) {
        return repository.getTvShowDetails(tvShowId);
    }
    
    public void setPinProtected(TvShow tvShow, boolean isPinProtected) {
        repository.setPinProtected(tvShow, isPinProtected);
    }
    
    public LiveData<List<TvShow>> getPinProtectedTvShows() {
        return pinProtectedTvShows;
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
    
    public void setSelectedNetworks(String networks) {
        selectedNetworks.setValue(networks);
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
    
    public LiveData<String> getSelectedNetworks() {
        return selectedNetworks;
    }
}
