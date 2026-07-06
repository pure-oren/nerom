package com.movieapp.explorer.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.movieapp.explorer.data.api.RetrofitClient;
import com.movieapp.explorer.data.api.TmdbApiService;
import com.movieapp.explorer.data.db.MovieAppDatabase;
import com.movieapp.explorer.data.db.MovieDao;
import com.movieapp.explorer.data.model.ApiResponse;
import com.movieapp.explorer.data.model.Movie;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class MovieRepository {
    private final TmdbApiService apiService;
    private final MovieDao movieDao;
    
    public MovieRepository(Context context) {
        this.apiService = RetrofitClient.getClient().create(TmdbApiService.class);
        this.movieDao = MovieAppDatabase.getInstance(context).movieDao();
    }
    
    public LiveData<List<Movie>> getPopularMovies(int page) {
        MutableLiveData<List<Movie>> data = new MutableLiveData<>();
        
        apiService.getPopularMovies(page).enqueue(new Callback<ApiResponse<Movie>>() {
            @Override
            public void onResponse(Call<ApiResponse<Movie>> call, Response<ApiResponse<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movieDao.insertMovies(response.body().results);
                    data.setValue(response.body().results);
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<Movie>> call, Throwable t) {
                // Fallback to cached data
                data.setValue(null);
            }
        });
        
        return data;
    }
    
    public LiveData<List<Movie>> getTopRatedMovies(int page) {
        MutableLiveData<List<Movie>> data = new MutableLiveData<>();
        
        apiService.getTopRatedMovies(page).enqueue(new Callback<ApiResponse<Movie>>() {
            @Override
            public void onResponse(Call<ApiResponse<Movie>> call, Response<ApiResponse<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movieDao.insertMovies(response.body().results);
                    data.setValue(response.body().results);
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<Movie>> call, Throwable t) {
                data.setValue(null);
            }
        });
        
        return data;
    }
    
    public LiveData<List<Movie>> getUpcomingMovies(int page) {
        MutableLiveData<List<Movie>> data = new MutableLiveData<>();
        
        apiService.getUpcomingMovies(page).enqueue(new Callback<ApiResponse<Movie>>() {
            @Override
            public void onResponse(Call<ApiResponse<Movie>> call, Response<ApiResponse<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movieDao.insertMovies(response.body().results);
                    data.setValue(response.body().results);
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<Movie>> call, Throwable t) {
                data.setValue(null);
            }
        });
        
        return data;
    }
    
    public LiveData<List<Movie>> searchMovies(String query, int page) {
        MutableLiveData<List<Movie>> data = new MutableLiveData<>();
        
        apiService.searchMovies(query, page).enqueue(new Callback<ApiResponse<Movie>>() {
            @Override
            public void onResponse(Call<ApiResponse<Movie>> call, Response<ApiResponse<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body().results);
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<Movie>> call, Throwable t) {
                data.setValue(null);
            }
        });
        
        return data;
    }
    
    public LiveData<List<Movie>> discoverMovies(String genreIds, String studioIds, String sortBy, int page) {
        MutableLiveData<List<Movie>> data = new MutableLiveData<>();
        
        apiService.discoverMovies(genreIds, studioIds, sortBy, page).enqueue(new Callback<ApiResponse<Movie>>() {
            @Override
            public void onResponse(Call<ApiResponse<Movie>> call, Response<ApiResponse<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movieDao.insertMovies(response.body().results);
                    data.setValue(response.body().results);
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<Movie>> call, Throwable t) {
                data.setValue(null);
            }
        });
        
        return data;
    }
    
    public LiveData<Movie> getMovieDetails(int movieId) {
        MutableLiveData<Movie> data = new MutableLiveData<>();
        
        apiService.getMovieDetails(movieId).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movieDao.insertMovie(response.body());
                    data.setValue(response.body());
                }
            }
            
            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                data.setValue(null);
            }
        });
        
        return data;
    }
    
    public void setPinProtected(Movie movie, boolean isPinProtected) {
        movie.pinProtected = isPinProtected;
        movieDao.updateMovie(movie);
    }
    
    public LiveData<List<Movie>> getPinProtectedMovies() {
        return movieDao.getPinProtectedMovies();
    }
}
