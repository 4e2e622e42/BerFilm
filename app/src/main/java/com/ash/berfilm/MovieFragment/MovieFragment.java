package com.ash.berfilm.MovieFragment;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Parcelable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.Toast;

import com.ash.berfilm.MainActivity;
import com.ash.berfilm.Models.MovieModel.Movie;
import com.ash.berfilm.Models.MovieModel.MovieResult;
import com.ash.berfilm.MovieFragment.Adopter.MoviesAdopter;
import com.ash.berfilm.R;
import com.ash.berfilm.Service.ApiClient;
import com.ash.berfilm.ViewModel.AppViewModel;
import com.ash.berfilm.databinding.FragmentMovieBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.iammert.library.ui.multisearchviewlib.MultiSearchView;
import com.paulrybitskyi.persistentsearchview.PersistentSearchView;
import com.paulrybitskyi.persistentsearchview.adapters.model.SuggestionItem;
import com.paulrybitskyi.persistentsearchview.listeners.OnSearchConfirmedListener;
import com.paulrybitskyi.persistentsearchview.listeners.OnSearchQueryChangeListener;
import com.paulrybitskyi.persistentsearchview.utils.VoiceRecognitionDelegate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@AndroidEntryPoint
public class MovieFragment extends Fragment
{
    FragmentMovieBinding fragmentMoviesBinding;
    MainActivity mainActivity;
    int page = 1;
    int totalPage;
    List<MovieResult> movieResults;
    MoviesAdopter moviesAdopter;
    AppViewModel appViewModel;
    CompositeDisposable compositeDisposable;



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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        fragmentMoviesBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_movie, container, false);

        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
        compositeDisposable = new CompositeDisposable();



        getAllMovies(page);


       fragmentMoviesBinding.nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener()
        {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY)
            {
                if(scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())
                {
                    if(page <= totalPage)
                    {
                        page++;
                    }
                    fragmentMoviesBinding.progressBar.setVisibility(View.VISIBLE);
                    getAllMovies(page);
                }
            }
        });



       //Setup MovieSearch
        fragmentMoviesBinding.multiSearchView.setVoiceInputButtonEnabled(false);
        fragmentMoviesBinding.multiSearchView.setSuggestionsDisabled(true);
        fragmentMoviesBinding.multiSearchView.setQueryInputHint("SpiderMan");

       fragmentMoviesBinding.multiSearchView.setOnSearchConfirmedListener(new OnSearchConfirmedListener()
       {
           @Override
           public void onSearchConfirmed(PersistentSearchView searchView, String query)
           {
               if(query.equals(""))
               {
                   Toast.makeText(getActivity(),"SearchBox is Empty",Toast.LENGTH_SHORT).show();
               }else
               {
                   Bundle bundle = new Bundle();
                   bundle.putString("searchedText",query);
                   Navigation.findNavController(getView()).navigate(R.id.action_movieFragment_to_movieSearchFragment,bundle);
               }

           }
       });




        fragmentMoviesBinding.multiSearchView.setOnClearInputBtnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                fragmentMoviesBinding.multiSearchView.collapse();
            }
        });






        return  fragmentMoviesBinding.getRoot();
    }


    private void getAllMovies(int page)
    {
       appViewModel.makeAllMoviesCall(page).enqueue(new Callback<Movie>()
       {
           @Override
           public void onResponse(Call<Movie> call, Response<Movie> response)
           {
               movieResults = response.body().getResults();
               totalPage = response.body().getTotalPages();

               fragmentMoviesBinding.progressBar.setVisibility(View.GONE);
               if(fragmentMoviesBinding.movieRecyclerView.getAdapter() !=null)
               {
                   moviesAdopter = (MoviesAdopter) fragmentMoviesBinding.movieRecyclerView.getAdapter();
                   moviesAdopter.updateMovie(movieResults);
               }else
               {
                   moviesAdopter = new MoviesAdopter(movieResults);
                   RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
                   fragmentMoviesBinding.movieRecyclerView.setLayoutManager(layoutManager);
                   fragmentMoviesBinding.movieRecyclerView.setAdapter(moviesAdopter);
               }





           }

           @Override
           public void onFailure(Call<Movie> call, Throwable t)
           {

           }
       });













    }

    private void setUpToolBar(View view)
    {
        NavController navController = Navigation.findNavController(view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.movieFragment)
                .setOpenableLayout(mainActivity.drawerLayout)
                .build();

        Toolbar toolbar = view.findViewById(R.id.toolbar);

        NavigationUI.setupWithNavController(toolbar,navController,appBarConfiguration);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if(destination.getId() == R.id.movieFragment)
                {
                    toolbar.setNavigationIcon(R.drawable.ic_baseline_sort_24);
                    toolbar.setTitle("Movies");
                    toolbar.setTitleTextAppearance(view.getContext(),R.style.toolBarTitleFont);

                }
            }
        });

    }






    @Override
    public void onDestroy()
    {
        super.onDestroy();
        compositeDisposable.clear();
    }
}