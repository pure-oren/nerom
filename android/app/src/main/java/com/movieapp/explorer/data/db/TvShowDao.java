package com.movieapp.explorer.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.movieapp.explorer.data.model.TvShow;
import java.util.List;

@Dao
public interface TvShowDao {
    
    @Query("SELECT * FROM tv_shows ORDER BY popularity DESC LIMIT :limit OFFSET :offset")
    LiveData<List<TvShow>> getAllTvShows(int limit, int offset);
    
    @Query("SELECT * FROM tv_shows WHERE id = :id")
    LiveData<TvShow> getTvShowById(int id);
    
    @Query("SELECT * FROM tv_shows WHERE name LIKE '%' || :query || '%'")
    LiveData<List<TvShow>> searchTvShows(String query);
    
    @Query("SELECT * FROM tv_shows WHERE pinProtected = 1")
    LiveData<List<TvShow>> getPinProtectedTvShows();
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTvShow(TvShow tvShow);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTvShows(List<TvShow> tvShows);
    
    @Update
    void updateTvShow(TvShow tvShow);
    
    @Delete
    void deleteTvShow(TvShow tvShow);
    
    @Query("DELETE FROM tv_shows")
    void clearAllTvShows();
    
    @Query("SELECT COUNT(*) FROM tv_shows")
    LiveData<Integer> getTvShowCount();
}
