package com.movieapp.explorer.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.movieapp.explorer.data.model.Movie;
import java.util.List;

@Dao
public interface MovieDao {
    
    @Query("SELECT * FROM movies ORDER BY popularity DESC LIMIT :limit OFFSET :offset")
    LiveData<List<Movie>> getAllMovies(int limit, int offset);
    
    @Query("SELECT * FROM movies WHERE id = :id")
    LiveData<Movie> getMovieById(int id);
    
    @Query("SELECT * FROM movies WHERE title LIKE '%' || :query || '%'")
    LiveData<List<Movie>> searchMovies(String query);
    
    @Query("SELECT * FROM movies WHERE pinProtected = 1")
    LiveData<List<Movie>> getPinProtectedMovies();
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(Movie movie);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovies(List<Movie> movies);
    
    @Update
    void updateMovie(Movie movie);
    
    @Delete
    void deleteMovie(Movie movie);
    
    @Query("DELETE FROM movies")
    void clearAllMovies();
    
    @Query("SELECT COUNT(*) FROM movies")
    LiveData<Integer> getMovieCount();
}
