package com.ash.berfilm.Service;

import com.ash.berfilm.BuildConfig;
import com.ash.berfilm.Models.CastInfo.CastInfo;
import com.ash.berfilm.Models.Credits.Credits;
import com.ash.berfilm.Models.MovieModel.Movie;
import com.ash.berfilm.Models.MovieModel.MovieResult;
import com.ash.berfilm.Models.Trailer.Trailer;
import com.ash.berfilm.Models.Trailer.TrailerResult;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiClient
{
    //Base_URL =https://api.themoviedb.org/3/
    String apiKey = "";
    //->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->
    //Movies Api

    //Get All Movie
    @GET("discover/movie?api_key="+apiKey)
    Call<Movie> getAllMovie(@Query("page") int page);

    //Get Trending Movie
    @GET("trending/movie/day?api_key="+apiKey)
    Observable<Movie> getTrendingMovie();

    //Get Popular Movie
    @GET("movie/popular?api_key="+apiKey)
    Observable<Movie> getPopular();

    //Get Movie Cast
    @GET("movie/{id}/credits?api_key="+apiKey)
    Call<Credits> getMovieCredits(@Path("id") int id);

    //Get Upcoming Movie
    @GET("movie/upcoming?api_key="+apiKey)
    Observable<Movie> getUpComing();

    //Get Recommendations Movie
    @GET("movie/{id}/recommendations?api_key="+apiKey)
    Call<Movie> getRecommendedMovie(@Path("id") int id);

    //Get Movie Trailer
    @GET("movie/{id}/videos?api_key=+"apiKey)
    Call<Trailer> getMovieTrailer(@Path("id") int id);

    //Search Movie
    @GET("search/movie?api_key="+apiKey)
    Call<Movie> getSearchText(@Query("query") String text,
                              @Query("page") int page
                              );


    //->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->
    //Series Api


    //Get All Series
    @GET("discover/tv?api_key="+apiKey)
    Call<Movie> getAllSeries(@Query("page") int page);


    //Get Trending Series
    @GET("trending/tv/day?api_key="+apiKey)
    Observable<Movie> getTrendingSeries();

    //Get Series Cast
    @GET("tv/{id}/credits?api_key="+apiKey)
    Call<Credits> getSeriesCredits(@Path("id") int id);


    //Get Recommended Series
    @GET("tv/{id}/recommendations?api_key="+apiKey)
    Call<Movie> getRecommendedSeries(@Path("id") int id);


    //Get Series Trailer
    @GET("tv/{id}/videos?api_key="+apiKey)
    Call<Trailer> getSeriesTrailer(@Path("id") int id);


    //Search Series
    @GET("search/tv?api_key="+apiKey)
    Call<Movie> getSeriesSearchText(@Query("query") String text,
                              @Query("page") int page
    );





    //->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->
    //Cast Information

    @GET("person/{id}?api_key="+apiKey)
    Call<CastInfo> getCastInfo(@Path("id") int id);


}
