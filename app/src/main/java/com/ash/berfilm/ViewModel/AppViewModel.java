package com.ash.berfilm.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.ash.berfilm.AppRepository;
import com.ash.berfilm.Models.CastInfo.CastInfo;
import com.ash.berfilm.Models.Credits.Credits;
import com.ash.berfilm.Models.MovieModel.Movie;

import java.util.concurrent.Future;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;

@HiltViewModel
public class AppViewModel extends ViewModel
{

    @Inject
    AppRepository appRepository;


    @Inject
    public AppViewModel(Application application)
    {
        super();
    }

    public Future<Observable<Movie>> makeTrendingMovieFutureCall()
    {
        return appRepository.trendingMovieFutureCall();
    }

    public Future<Observable<Movie>> makeTrendingSeriesFutureCall()
    {
        return appRepository.trendingSeriesFutureCall();
    }

    public Future<Observable<Movie>> makePopularFutureCall()
    {
       return appRepository.popularFutureCall();
    }

    public Future<Observable<Movie>> makeUpComingFutureCall()
    {
        return appRepository.upComingFutureCall();
    }


    public Call<Credits> makeCreditsCall(int id)
    {
        return appRepository.getMovieCredits(id);
    }

    public Call<Movie> makeRecommendedMovieCall(int id)
    {
        return appRepository.getRecommendedMovie(id);
    }

    public Call<CastInfo> makeCastInfoCall(int id)
    {
        return appRepository.getCastInfo(id);
    }


    public Call<Credits> makeSeriesCreditsCall(int id)
    {
        return appRepository.getSeriesCredits(id);
    }


    public Call<Movie> makeRecommendedSeriesCall(int id)
    {
        return appRepository.getRecommendedSeries(id);
    }







    public Call<Movie> makeAllMoviesCall(int page)
    {
        return appRepository.moviesCall(page);
    }

    public Call<Movie> makeAllSeriesCall(int page)
    {
        return  appRepository.seriesCall(page);
    }

    public Call<Movie> makeSearchCall(String search,int page)
    {
        return appRepository.searchedMovie(search,page);
    }


    public Call<Movie> makeSeriesSearchCall(String search,int page)
    {
        return appRepository.searchedSeries(search,page);
    }




}
