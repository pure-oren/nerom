package com.movieapp.explorer.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.movieapp.explorer.data.api.RetrofitClient;
import com.movieapp.explorer.data.api.TmdbApiService;
import com.movieapp.explorer.data.db.MovieAppDatabase;
import com.movieapp.explorer.data.db.TvShowDao;
import com.movieapp.explorer.data.model.ApiResponse;
import com.movieapp.explorer.data.model.TvShow;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class TvShowRepository {
    private final TmdbApiService apiService;
    private final TvShowDao tvShowDao;
    
    public TvShowRepository(Context context) {
        this.apiService = RetrofitClient.getClient().create(TmdbApiService.class);
        this.tvShowDao = MovieAppDatabase.getInstance(context).tvShowDao();
    }
    
    public LiveData<List<TvShow>> getPopularTvShows(int page) {
        MutableLiveData<List<TvShow>> data = new MutableLiveData<>();
        
        apiService.getPopularTvShows(page).enqueue(new Callback<ApiResponse<TvShow>>() {
            @Override
            public void onResponse(Call<ApiResponse<TvShow>> call, Response<ApiResponse<TvShow>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tvShowDao.insertTvShows(response.body().results);
                    data.setValue(response.body().results);
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<TvShow>> call, Throwable t) {
                data.setValue(null);
            }
        });
        
        return data;
    }
    
    public LiveData<List<TvShow>> getTopRatedTvShows(int page) {
        MutableLiveData<List<TvShow>> data = new MutableLiveData<>();
        
        apiService.getTopRatedTvShows(page).enqueue(new Callback<ApiResponse<TvShow>>() {
            @Override
            public void onResponse(Call<ApiResponse<TvShow>> call, Response<ApiResponse<TvShow>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tvShowDao.insertTvShows(response.body().results);
                    data.setValue(response.body().results);
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<TvShow>> call, Throwable t) {
                data.setValue(null);
            }
        });
        
        return data;
    }
    
    public LiveData<List<TvShow>> getOnTheAirTvShows(int page) {
        MutableLiveData<List<TvShow>> data = new MutableLiveData<>();
        
        apiService.getOnTheAirTvShows(page).enqueue(new Callback<ApiResponse<TvShow>>() {
            @Override
            public void onResponse(Call<ApiResponse<TvShow>> call, Response<ApiResponse<TvShow>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tvShowDao.insertTvShows(response.body().results);
                    data.setValue(response.body().results);
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<TvShow>> call, Throwable t) {
                data.setValue(null);
            }
        });
        
        return data;
    }
    
    public LiveData<List<TvShow>> searchTvShows(String query, int page) {
        MutableLiveData<List<TvShow>> data = new MutableLiveData<>();
        
        apiService.searchTvShows(query, page).enqueue(new Callback<ApiResponse<TvShow>>() {
            @Override
            public void onResponse(Call<ApiResponse<TvShow>> call, Response<ApiResponse<TvShow>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body().results);
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<TvShow>> call, Throwable t) {
                data.setValue(null);
            }
        });
        
        return data;
    }
    
    public LiveData<List<TvShow>> discoverTvShows(String genreIds, String networkIds, String sortBy, int page) {
        MutableLiveData<List<TvShow>> data = new MutableLiveData<>();
        
        apiService.discoverTvShows(genreIds, networkIds, sortBy, page).enqueue(new Callback<ApiResponse<TvShow>>() {
            @Override
            public void onResponse(Call<ApiResponse<TvShow>> call, Response<ApiResponse<TvShow>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tvShowDao.insertTvShows(response.body().results);
                    data.setValue(response.body().results);
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<TvShow>> call, Throwable t) {
                data.setValue(null);
            }
        });
        
        return data;
    }
    
    public LiveData<TvShow> getTvShowDetails(int tvShowId) {
        MutableLiveData<TvShow> data = new MutableLiveData<>();
        
        apiService.getTvShowDetails(tvShowId).enqueue(new Callback<TvShow>() {
            @Override
            public void onResponse(Call<TvShow> call, Response<TvShow> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tvShowDao.insertTvShow(response.body());
                    data.setValue(response.body());
                }
            }
            
            @Override
            public void onFailure(Call<TvShow> call, Throwable t) {
                data.setValue(null);
            }
        });
        
        return data;
    }
    
    public void setPinProtected(TvShow tvShow, boolean isPinProtected) {
        tvShow.pinProtected = isPinProtected;
        tvShowDao.updateTvShow(tvShow);
    }
    
    public LiveData<List<TvShow>> getPinProtectedTvShows() {
        return tvShowDao.getPinProtectedTvShows();
    }
}
