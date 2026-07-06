package com.movieapp.explorer.data.model;

import com.google.gson.annotations.SerializedName;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tv_shows")
public class TvShow {
    @PrimaryKey
    public int id;
    
    public String name;
    
    @SerializedName("first_air_date")
    public String firstAirDate;
    
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
    
    @SerializedName("number_of_seasons")
    public int numberOfSeasons;
    
    @SerializedName("number_of_episodes")
    public int numberOfEpisodes;
    
    public int[] networks;
    
    public boolean pinProtected;
    
    public TvShow() {}
    
    public TvShow(int id, String name, String firstAirDate, double popularity,
                  double voteAverage, int voteCount, String overview,
                  String posterPath, String backdropPath, int[] genreIds,
                  int numberOfSeasons, int numberOfEpisodes) {
        this.id = id;
        this.name = name;
        this.firstAirDate = firstAirDate;
        this.popularity = popularity;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.overview = overview;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.genreIds = genreIds;
        this.numberOfSeasons = numberOfSeasons;
        this.numberOfEpisodes = numberOfEpisodes;
        this.pinProtected = false;
    }
}
