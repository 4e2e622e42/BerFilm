package com.ash.berfilm.SeriesFragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ash.berfilm.MainActivity;
import com.ash.berfilm.Models.MovieModel.Movie;
import com.ash.berfilm.Models.MovieModel.MovieResult;
import com.ash.berfilm.MovieFragment.Adopter.MoviesAdopter;
import com.ash.berfilm.R;
import com.ash.berfilm.SeriesFragment.Adopter.SeriesAdopter;
import com.ash.berfilm.ViewModel.AppViewModel;
import com.ash.berfilm.databinding.FragmentSereisBinding;
import com.iammert.library.ui.multisearchviewlib.MultiSearchView;
import com.paulrybitskyi.persistentsearchview.PersistentSearchView;
import com.paulrybitskyi.persistentsearchview.listeners.OnSearchConfirmedListener;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class SeriesFragment extends Fragment
{
    FragmentSereisBinding fragmentSeriesBinding;
    MainActivity mainActivity;
    int page = 1;
    int totalPage;
    AppViewModel appViewModel;
    List<MovieResult> seriesList;
    SeriesAdopter seriesAdopter;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpToolBar(view);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        fragmentSeriesBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_sereis, container, false);
        appViewModel = new ViewModelProvider(getActivity()).get(AppViewModel.class);



        getAllSeries(page);



        fragmentSeriesBinding.nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener()
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
                    fragmentSeriesBinding.progressBar.setVisibility(View.VISIBLE);
                    getAllSeries(page);
                }
            }
        });




        //SetUp SeriesSearch
        fragmentSeriesBinding.multiSearchView.setVoiceInputButtonEnabled(false);
        fragmentSeriesBinding.multiSearchView.setSuggestionsDisabled(true);
        fragmentSeriesBinding.multiSearchView.setQueryInputHint("AttackOnTitan");

        fragmentSeriesBinding.multiSearchView.setOnSearchConfirmedListener(new OnSearchConfirmedListener()
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
                    Navigation.findNavController(getView()).navigate(R.id.action_seriesFragment_to_seriesSearchFragment,bundle);
                }

            }
        });




        fragmentSeriesBinding.multiSearchView.setOnClearInputBtnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                fragmentSeriesBinding.multiSearchView.collapse();
            }
        });










        return fragmentSeriesBinding.getRoot();
    }


    private void getAllSeries(int page)
    {
        appViewModel.makeAllSeriesCall(page).enqueue(new Callback<Movie>()
        {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response)
            {
                seriesList = response.body().getResults();
                totalPage = response.body().getTotalPages();

                fragmentSeriesBinding.progressBar.setVisibility(View.GONE);
                if(fragmentSeriesBinding.seriesRecyclerView.getAdapter() !=null)
                {
                    seriesAdopter = (SeriesAdopter) fragmentSeriesBinding.seriesRecyclerView.getAdapter();
                    seriesAdopter.updateData(seriesList);
                }else
                {
                    seriesAdopter = new SeriesAdopter(seriesList);
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
                    fragmentSeriesBinding.seriesRecyclerView.setLayoutManager(layoutManager);
                    fragmentSeriesBinding.seriesRecyclerView.setAdapter(seriesAdopter);
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

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.seriesFragment)
                .setOpenableLayout(mainActivity.drawerLayout)
                .build();
        Toolbar toolbar = view.findViewById(R.id.toolbar);


        NavigationUI.setupWithNavController(toolbar,navController,appBarConfiguration);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if(destination.getId() == R.id.seriesFragment)
                {
                    toolbar.setNavigationIcon(R.drawable.ic_baseline_sort_24);
                    toolbar.setTitle("Series");
                    toolbar.setTitleTextAppearance(view.getContext(),R.style.toolBarTitleFont);
                }
            }
        });

    }

}