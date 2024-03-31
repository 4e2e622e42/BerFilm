package com.ash.berfilm.HomeFragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ash.berfilm.HomeFragment.Adopters.PopularAdopter;
import com.ash.berfilm.HomeFragment.Adopters.TrendingMovieAdopter;
import com.ash.berfilm.HomeFragment.Adopters.TrendingSeriesAdopter;
import com.ash.berfilm.HomeFragment.Adopters.UpComingAdopter;
import com.ash.berfilm.MainActivity;
import com.ash.berfilm.Models.MovieModel.Movie;
import com.ash.berfilm.R;
import com.ash.berfilm.ViewModel.AppViewModel;
import com.ash.berfilm.databinding.FragmentHomeBinding;
import com.ash.berfilm.databinding.MovieItemBinding;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogAnimation;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class HomeFragment extends Fragment
{
    FragmentHomeBinding fragmentHomeBinding;
    MainActivity mainActivity;
    AppViewModel appViewModel;
    CompositeDisposable compositeDisposable;
    TrendingMovieAdopter trendingMovieAdopter;
    TrendingSeriesAdopter trendingSeriesAdopter;
    PopularAdopter popularAdopter;
    UpComingAdopter upComingAdopter;

    @Inject
    ConnectivityManager connectivityManager;
    @Inject
    NetworkRequest networkRequest;

    AestheticDialog aestheticDialog;

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        setUpToolBar(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentHomeBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);

        appViewModel = new ViewModelProvider(getActivity()).get(AppViewModel.class);

        compositeDisposable = new CompositeDisposable();



        checkConnection();


        return fragmentHomeBinding.getRoot();
    }


    private void setUpToolBar(View view)
    {
       NavController navController = Navigation.findNavController(view);

       AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment)
               .setOpenableLayout(mainActivity.drawerLayout)
               .build();

       Toolbar toolbar = view.findViewById(R.id.toolbar);

       NavigationUI.setupWithNavController(toolbar,navController,appBarConfiguration);


       navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener()
       {
           @Override
           public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments)
           {
               if(destination.getId() == R.id.homeFragment)
               {
                   toolbar.setNavigationIcon(R.drawable.ic_baseline_sort_24);
                   toolbar.setTitle("BerFilm");
                   toolbar.setTitleTextAppearance(view.getContext(),R.style.toolBarTitleFont);
               }

           }
       });


    }

    private void getTrendingMovie()
    {

        Observable.interval(1, TimeUnit.MILLISECONDS)
                .flatMap(n -> appViewModel.makeTrendingMovieFutureCall().get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Movie>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d)
                    {

                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull Movie trending)
                    {
                        fragmentHomeBinding.trendingMovieText.setVisibility(View.VISIBLE);
                        if(fragmentHomeBinding.trendingMovieRecycleView.getAdapter() != null)
                        {
                            trendingMovieAdopter = (TrendingMovieAdopter) fragmentHomeBinding.trendingMovieRecycleView.getAdapter();
                            trendingMovieAdopter.updateTrending(trending.getResults());

                        }else
                        {
                            trendingMovieAdopter = new TrendingMovieAdopter(trending.getResults());
                            fragmentHomeBinding.trendingMovieRecycleView.setAdapter(trendingMovieAdopter);

                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e)
                    {

                    }

                    @Override
                    public void onComplete()
                    {

                    }
                });
    }

    private void getPopular()
    {
        Observable.interval(1, TimeUnit.MILLISECONDS)
                .flatMap(n -> appViewModel.makePopularFutureCall().get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Movie>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d)
                    {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull Movie popular)
                    {
                        fragmentHomeBinding.popularText.setVisibility(View.VISIBLE);
                        if(fragmentHomeBinding.popularRecycleView.getAdapter() != null)
                        {
                            popularAdopter = (PopularAdopter) fragmentHomeBinding.popularRecycleView.getAdapter();
                            popularAdopter.updatePopular(popular.getResults());

                        }else
                        {
                            popularAdopter = new PopularAdopter(popular.getResults());
                            fragmentHomeBinding.popularRecycleView.setAdapter(popularAdopter);

                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e)
                    {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getUpcoming()
    {
        Observable.interval(1, TimeUnit.MILLISECONDS)
                .flatMap(n -> appViewModel.makeUpComingFutureCall().get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Movie>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d)
                    {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull Movie upComing)
                    {
                        fragmentHomeBinding.upComingText.setVisibility(View.VISIBLE);
                        if(fragmentHomeBinding.upComingRecycleView.getAdapter() != null)
                        {
                            upComingAdopter = (UpComingAdopter) fragmentHomeBinding.upComingRecycleView.getAdapter();
                            upComingAdopter.updateUpcoming(upComing.getResults());

                        }else
                        {
                            upComingAdopter = new UpComingAdopter(upComing.getResults());
                            fragmentHomeBinding.upComingRecycleView.setAdapter(upComingAdopter);

                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e)
                    {
                        Log.e("TAG","Error:  "+e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getTrendingSeries()
    {
        Observable.interval(1,TimeUnit.MILLISECONDS)
                .flatMap(n -> appViewModel.makeTrendingSeriesFutureCall().get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Movie>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d)
                    {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull Movie trendingSeries)
                    {
                        fragmentHomeBinding.trendingSeriesText.setVisibility(View.VISIBLE);
                        if(fragmentHomeBinding.trendingSeriesRecycleView.getAdapter() != null)
                        {
                            trendingSeriesAdopter = (TrendingSeriesAdopter) fragmentHomeBinding.trendingSeriesRecycleView.getAdapter();
                            trendingSeriesAdopter.updateSeries(trendingSeries.getResults());

                        }else
                        {
                            trendingSeriesAdopter = new TrendingSeriesAdopter(trendingSeries.getResults());
                            fragmentHomeBinding.trendingSeriesRecycleView.setAdapter(trendingSeriesAdopter);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e)
                    {
                        Log.e("TAG","Error:  "+e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });





    }

    private void checkConnection()
    {
        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback()
        {

            @Override
            public void onAvailable(@androidx.annotation.NonNull Network network)
            {
                getTrendingMovie();
                getTrendingSeries();
                getPopular();
                getUpcoming();
            }

            @Override
            public void onLost(@androidx.annotation.NonNull Network network)
            {
                aestheticDialog = new AestheticDialog.Builder(getActivity(), DialogStyle.CONNECTIFY, DialogType.ERROR)
                        .setTitle("No Available Connection")
                        .setMessage("internet connection has been interrupted")
                        .setDarkMode(true)
                        .setGravity(Gravity.CENTER)
                        .setAnimation(DialogAnimation.SPIN)
                        .show();
            }
        };

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            connectivityManager.registerDefaultNetworkCallback(networkCallback);
        }else{
            connectivityManager.registerNetworkCallback(networkRequest,networkCallback);
        }

    }





    @Override
    public void onDestroy()
    {
        super.onDestroy();
        compositeDisposable.clear();
    }
}