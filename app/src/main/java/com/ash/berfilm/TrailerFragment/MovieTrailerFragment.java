package com.ash.berfilm.TrailerFragment;

import static android.app.Activity.RESULT_CANCELED;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ash.berfilm.MainActivity;
import com.ash.berfilm.Models.Trailer.Trailer;
import com.ash.berfilm.Models.Trailer.TrailerResult;
import com.ash.berfilm.R;
import com.ash.berfilm.Service.ApiClient;
import com.ash.berfilm.databinding.FragmentMovieTrailerBinding;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;

import java.util.List;

import javax.inject.Inject;

import me.ibrahimsn.lib.SmoothBottomBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieTrailerFragment extends Fragment
{
    FragmentMovieTrailerBinding fragmentMovieTrailerBinding;
    List<TrailerResult> trailerResult;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        fragmentMovieTrailerBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_movie_trailer, container, false);

        getLifecycle().addObserver(fragmentMovieTrailerBinding.youtubePlayerView);

        SmoothBottomBar smoothBottomBar = getActivity().findViewById(R.id.smooth_bottombar);
        smoothBottomBar.setVisibility(View.INVISIBLE);

        int movieId = getArguments().getInt("movieId");

        getMovieTrailer(movieId);




        return fragmentMovieTrailerBinding.getRoot();
    }


    private void getMovieTrailer(int movieId)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiClient apiClient = retrofit.create(ApiClient.class);

        Call<Trailer> call = apiClient.getMovieTrailer(movieId);

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


                        fragmentMovieTrailerBinding.youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener()
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
        fragmentMovieTrailerBinding.youtubePlayerView.release();
        SmoothBottomBar smoothBottomBar = getActivity().findViewById(R.id.smooth_bottombar);
        smoothBottomBar.setVisibility(View.VISIBLE);
    }
}