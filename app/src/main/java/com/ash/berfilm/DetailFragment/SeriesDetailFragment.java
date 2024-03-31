package com.ash.berfilm.DetailFragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ash.berfilm.DetailFragment.Adopters.SeriesCastAdopter;
import com.ash.berfilm.DetailFragment.Adopters.RecommendedSeriesAdopter;
import com.ash.berfilm.Models.Credits.Cast;
import com.ash.berfilm.Models.Credits.Credits;
import com.ash.berfilm.Models.Credits.Crew;
import com.ash.berfilm.Models.MovieModel.Movie;
import com.ash.berfilm.Models.MovieModel.MovieResult;
import com.ash.berfilm.R;
import com.ash.berfilm.Service.ApiClient;
import com.ash.berfilm.ViewModel.AppViewModel;
import com.ash.berfilm.databinding.FragmentSereisDetailBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SeriesDetailFragment extends Fragment
{
    FragmentSereisDetailBinding fragmentSeriesBinding;
    SeriesCastAdopter seriesCastAdopter;
    RecommendedSeriesAdopter recommendedSeriesAdopter;
    List<Cast> castList;
    List<Crew> crewList;
    List<MovieResult> recommendedList;
    AppViewModel appViewModel;

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {


        fragmentSeriesBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_sereis_detail,container,false);
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        MovieResult series = getArguments().getParcelable("model");

        setUpCreditsDetail(series.getId());
        getRecommendedSeries(series.getId());
        setupDetail(series);

        fragmentSeriesBinding.playTrailerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Bundle bundle = new Bundle();
                bundle.putInt("seriesId",series.getId());

                Navigation.findNavController(view).navigate(R.id.action_seriesDetailFragment_to_seriesTrailerFragment,bundle);
            }
        });




        return fragmentSeriesBinding.getRoot();
    }

    private void setupDetail(MovieResult seriesResult)
    {
        if(seriesResult.getPosterPath() !=null || seriesResult.getBackdropPath() != null)
        {
            Glide.with(fragmentSeriesBinding.getRoot().getContext())
                    .load("https://image.tmdb.org/t/p/w500" + seriesResult.getBackdropPath())
                    .into(fragmentSeriesBinding.BackdropPoster);

            Glide.with(fragmentSeriesBinding.getRoot().getContext())
                    .load("https://image.tmdb.org/t/p/w500" + seriesResult.getPosterPath())
                    .into(fragmentSeriesBinding.mainPoster);
        }else
        {
            fragmentSeriesBinding.mainPoster.setImageResource(R.drawable.question_mark);
        }


        fragmentSeriesBinding.movieName.setText(seriesResult.getName());
        fragmentSeriesBinding.releaseDateText.setText(seriesResult.getFirstAirDate());

        fragmentSeriesBinding.rateText.setText(String.format("%.1f" ,seriesResult.getVoteAverage()));
        fragmentSeriesBinding.overViewContent.setText(seriesResult.getOverview());

    }


    private void setUpCreditsDetail(int id)
    {
        appViewModel.makeSeriesCreditsCall(id).enqueue(new Callback<Credits>()
        {
            @Override
            public void onResponse(Call<Credits> call, Response<Credits> response)
            {
                castList = response.body().getCast();
                crewList = response.body().getCrew();

                if(fragmentSeriesBinding.castRecyclerView.getAdapter() != null)
                {
                    seriesCastAdopter = (SeriesCastAdopter) fragmentSeriesBinding.castRecyclerView.getAdapter();
                }else
                {
                    seriesCastAdopter = new SeriesCastAdopter(castList);
                    fragmentSeriesBinding.castRecyclerView.setAdapter(seriesCastAdopter);
                }

                //Get Producer Name
                for(int i = 0;i<crewList.size();i++)
                {
                    if(crewList.get(i).getJob().equals("Producer"))
                    {
                        fragmentSeriesBinding.directerName.setText(crewList.get(i).getName());
                    }
                }
            }

            @Override
            public void onFailure(Call<Credits> call, Throwable t)
            {

            }
        });


    }


    private void getRecommendedSeries(int id)
    {
        appViewModel.makeRecommendedSeriesCall(id).enqueue(new Callback<Movie>()
        {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response)
            {
                recommendedList = response.body().getResults();

                if(fragmentSeriesBinding.RecommendedRecyclerView.getAdapter() != null)
                {
                    recommendedSeriesAdopter = (RecommendedSeriesAdopter) fragmentSeriesBinding.RecommendedRecyclerView.getAdapter();
                }else
                {
                    recommendedSeriesAdopter = new RecommendedSeriesAdopter(recommendedList);
                    fragmentSeriesBinding.RecommendedRecyclerView.setAdapter(recommendedSeriesAdopter);
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t)
            {

            }
        });




    }





}