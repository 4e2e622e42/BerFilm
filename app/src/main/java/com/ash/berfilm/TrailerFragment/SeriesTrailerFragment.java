package com.ash.berfilm.TrailerFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ash.berfilm.Models.Trailer.Trailer;
import com.ash.berfilm.Models.Trailer.TrailerResult;
import com.ash.berfilm.R;
import com.ash.berfilm.Service.ApiClient;
import com.ash.berfilm.databinding.FragmentSeriesTrailerBinding;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;

import java.util.List;

import me.ibrahimsn.lib.SmoothBottomBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SeriesTrailerFragment extends Fragment
{
    FragmentSeriesTrailerBinding fragmentSeriesTrailerBinding;
    List<TrailerResult> trailerResult;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {

        fragmentSeriesTrailerBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_series_trailer, container, false);

        getLifecycle().addObserver(fragmentSeriesTrailerBinding.youtubePlayerView);

        SmoothBottomBar smoothBottomBar = getActivity().findViewById(R.id.smooth_bottombar);
        smoothBottomBar.setVisibility(View.INVISIBLE);

        int seriesId = getArguments().getInt("seriesId");

        getMovieTrailer(seriesId);


        return fragmentSeriesTrailerBinding.getRoot();


    }


    private void getMovieTrailer(int seriesId)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiClient apiClient = retrofit.create(ApiClient.class);

        Call<Trailer> call = apiClient.getSeriesTrailer(seriesId);

        call.enqueue(new Callback<Trailer>()
        {
            @Override
            public void onResponse(Call<Trailer> call, Response<Trailer> response)
            {
                trailerResult = response.body().getTrailerResults();

                for(int i = 0;i<trailerResult.size();i++)
                {
                    if(trailerResult.get(i).getType().equals("Trailer"))
                    {
                        String youTubeID = trailerResult.get(i).getKey();

                        fragmentSeriesTrailerBinding.youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener()
                        {
                            @Override
                            public void onReady(@NonNull YouTubePlayer youTubePlayer)
                            {
                                super.onReady(youTubePlayer);
                                youTubePlayer.loadVideo(youTubeID,0);
                                youTubePlayer.play();

                            }
                        });

                    }
                }
            }

            @Override
            public void onFailure(Call<Trailer> call, Throwable t)
            {

            }
        });
    }




    @Override
    public void onDestroy()
    {
        super.onDestroy();
        SmoothBottomBar smoothBottomBar = getActivity().findViewById(R.id.smooth_bottombar);
        smoothBottomBar.setVisibility(View.VISIBLE);
        fragmentSeriesTrailerBinding.youtubePlayerView.release();
    }


















}