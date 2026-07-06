package com.movieapp.explorer.data.model;

import com.google.gson.annotations.SerializedName;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movies")
public class Movie {
    @PrimaryKey
    public int id;
    
    public String title;
    
    @SerializedName("release_date")
    public String releaseDate;
    
    public double popularity;
    
    @SerializedName("vote_average")
    public double voteAverage;
    
    @SerializedName("vote_count")
    public int voteCount;
    
    public String overview;
    
    @SerializedName("poster_path")
    public String posterPath;
    
    @SerializedName("backdrop_path")
    public String backdropPath;
    
    @SerializedName("genre_ids")
    public int[] genreIds;
    
    public int[] studios;
    
    public boolean pinProtected;
    
    public Movie() {}
    
    public Movie(int id, String title, String releaseDate, double popularity, 
                 double voteAverage, int voteCount, String overview, 
                 String posterPath, String backdropPath, int[] genreIds) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.overview = overview;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.genreIds = genreIds;
        this.pinProtected = false;
    }
}
