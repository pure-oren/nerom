package com.movieapp.explorer.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ApiResponse<T> {
    public int page;
    public List<T> results;
    
    @SerializedName("total_results")
    public int totalResults;
    
    @SerializedName("total_pages")
    public int totalPages;
}

class GenreResponse {
    public List<Genre> genres;
}

class Genre {
    public int id;
    public String name;
}

class NetworkResponse {
    public List<Network> networks;
}

class Network {
    public int id;
    public String name;
}
