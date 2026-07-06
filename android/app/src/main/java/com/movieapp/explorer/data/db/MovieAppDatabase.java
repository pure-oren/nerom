package com.movieapp.explorer.data.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.movieapp.explorer.data.model.Movie;
import com.movieapp.explorer.data.model.TvShow;

@Database(entities = {Movie.class, TvShow.class}, version = 1, exportSchema = false)
public abstract class MovieAppDatabase extends RoomDatabase {
    
    public abstract MovieDao movieDao();
    public abstract TvShowDao tvShowDao();
    
    private static volatile MovieAppDatabase instance;
    
    public static MovieAppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (MovieAppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        MovieAppDatabase.class,
                        "movie_app_db"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return instance;
    }
}
