package com.movieapp.explorer.data.api;

import com.movieapp.explorer.data.model.ApiResponse;
import com.movieapp.explorer.data.model.Movie;
import com.movieapp.explorer.data.model.TvShow;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TmdbApiService {
    
    // Movies
    @GET("movie/popular")
    Call<ApiResponse<Movie>> getPopularMovies(@Query("page") int page);
    
    @GET("movie/top_rated")
    Call<ApiResponse<Movie>> getTopRatedMovies(@Query("page") int page);
    
    @GET("movie/upcoming")
    Call<ApiResponse<Movie>> getUpcomingMovies(@Query("page") int page);
    
    @GET("movie/{id}")
    Call<Movie> getMovieDetails(@Path("id") int id);
    
    @GET("search/movie")
    Call<ApiResponse<Movie>> searchMovies(@Query("query") String query, @Query("page") int page);
    
    @GET("discover/movie")
    Call<ApiResponse<Movie>> discoverMovies(
        @Query("with_genres") String genreIds,
        @Query("with_companies") String studioIds,
        @Query("sort_by") String sortBy,
        @Query("page") int page
    );
    
    // TV Shows
    @GET("tv/popular")
    Call<ApiResponse<TvShow>> getPopularTvShows(@Query("page") int page);
    
    @GET("tv/top_rated")
    Call<ApiResponse<TvShow>> getTopRatedTvShows(@Query("page") int page);
    
    @GET("tv/on_the_air")
    Call<ApiResponse<TvShow>> getOnTheAirTvShows(@Query("page") int page);
    
    @GET("tv/{id}")
    Call<TvShow> getTvShowDetails(@Path("id") int id);
    
    @GET("search/tv")
    Call<ApiResponse<TvShow>> searchTvShows(@Query("query") String query, @Query("page") int page);
    
    @GET("discover/tv")
    Call<ApiResponse<TvShow>> discoverTvShows(
        @Query("with_genres") String genreIds,
        @Query("with_networks") String networkIds,
        @Query("sort_by") String sortBy,
        @Query("page") int page
    );
}
