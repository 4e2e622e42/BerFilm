package com.ash.berfilm.SearchFragment.Movies;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ash.berfilm.Models.MovieModel.Movie;
import com.ash.berfilm.Models.MovieModel.MovieResult;
import com.ash.berfilm.MovieFragment.Adopter.MoviesAdopter;
import com.ash.berfilm.R;
import com.ash.berfilm.SearchFragment.Movies.Adopters.SearchMovieAdopter;
import com.ash.berfilm.Service.ApiClient;
import com.ash.berfilm.ViewModel.AppViewModel;
import com.ash.berfilm.databinding.FragmentMovieSearchBinding;

import java.util.List;

import me.ibrahimsn.lib.SmoothBottomBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieSearchFragment extends Fragment
{
    FragmentMovieSearchBinding fragmentMovieSearchBinding;
    AppViewModel appViewModel;
    int page = 1;
    int totalPage;
    List<MovieResult> searchResult;
    SearchMovieAdopter searchMovieAdopter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        fragmentMovieSearchBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_movie_search, container, false);
        appViewModel = new ViewModelProvider(getActivity()).get(AppViewModel.class);

        SmoothBottomBar smoothBottomBar = getActivity().findViewById(R.id.smooth_bottombar);
        smoothBottomBar.setVisibility(View.GONE);


        String searchedText = getArguments().getString("searchedText");

        findSearchedMovie(searchedText,page);

        fragmentMovieSearchBinding.nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener()
        {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY)
            {
                if(scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())
                {
                    if(page < totalPage)
                    {
                        page++;
                    }else if(page == totalPage)
                    {
                        page = totalPage;
                    }
                    findSearchedMovie(searchedText,page);
                    fragmentMovieSearchBinding.progressBar.setVisibility(View.VISIBLE);
                }
            }
        });






        return fragmentMovieSearchBinding.getRoot();
    }


    private void  findSearchedMovie(String searchText,int page)
    {
        appViewModel.makeSearchCall(searchText,page).enqueue(new Callback<Movie>()
        {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response)
            {
                if(response.body().getTotalPages() != 0)
                {
                    searchResult = response.body().getResults();
                    totalPage = response.body().getTotalPages();
                    fragmentMovieSearchBinding.progressBar.setVisibility(View.GONE);
                    fragmentMovieSearchBinding.movieCutAnimation.setVisibility(View.GONE);
                    fragmentMovieSearchBinding.resultTxt.setVisibility(View.VISIBLE);


                    //Change Specific text Color with SpannableStringBuilder
                    SpannableStringBuilder builder = new SpannableStringBuilder();

                    String findTxt = "Find ";
                    SpannableString redSpannable= new SpannableString(findTxt);
                    redSpannable.setSpan(new ForegroundColorSpan(Color.WHITE), 0, findTxt.length(), 0);
                    builder.append(redSpannable);

                    String totalResult = String.valueOf(response.body().getTotalResults());
                    SpannableString whiteSpannable= new SpannableString(totalResult);
                    whiteSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#38e065")), 0, totalResult.length(), 0);
                    builder.append(whiteSpannable);

                    String movieFor = " Movie for: "+searchText;
                    SpannableString blueSpannable = new SpannableString(movieFor);
                    blueSpannable.setSpan(new ForegroundColorSpan(Color.WHITE), 0, movieFor.length(), 0);
                    builder.append(blueSpannable);

                    fragmentMovieSearchBinding.resultTxt.setText(builder, TextView.BufferType.SPANNABLE);




                    if (fragmentMovieSearchBinding.searchRecyclerView.getAdapter() != null) {
                        searchMovieAdopter = (SearchMovieAdopter) fragmentMovieSearchBinding.searchRecyclerView.getAdapter();
                        searchMovieAdopter.addItems(searchResult);
                    } else {
                        searchMovieAdopter = new SearchMovieAdopter(searchResult);
                        fragmentMovieSearchBinding.searchRecyclerView.setAdapter(searchMovieAdopter);
                    }

                }else
                {
                    fragmentMovieSearchBinding.zeroSearchTxt.setText("No Result Found For: "+searchText+"\n\n☹️");
                    fragmentMovieSearchBinding.zeroSearchTxt.setVisibility(View.VISIBLE);
                    fragmentMovieSearchBinding.movieCutAnimation.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t)
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
    }
}