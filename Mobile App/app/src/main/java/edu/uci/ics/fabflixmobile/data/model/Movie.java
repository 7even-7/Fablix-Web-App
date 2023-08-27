package edu.uci.ics.fabflixmobile.data.model;

/**
 * Movie class that captures movie information for movies retrieved from MovieListActivity
 */
public class Movie {
    private final String name;
    private final String year;
    private final String starsName;
    private final String director;
    private final String genres;
    private  final String id;
    private final String rating;
    public Movie(String name, String year, String starsName, String director, String genres, String id, String rating) {
        this.name = name;
        this.year = year;
        this.starsName = starsName;
        this.director = director;
        this.genres = genres;
        this.id = id;
        this.rating =rating;
    }
    public String getId(){return this.id;}
    public String getName() {
        return name;
    }
    public String getDirector(){return director;}
    public String getStarsName(){return starsName;}
    public String getGenres(){return genres;}

    public String getYear() {
        return year;
    }
    public String getRating(){return  rating;}
}