package com.ash.berfilm.HomeFragment.Adopters;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.ash.berfilm.Models.MovieModel.MovieResult;
import com.ash.berfilm.R;
import com.ash.berfilm.databinding.MovieItemBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.List;

public class TrendingMovieAdopter extends RecyclerView.Adapter<TrendingMovieAdopter.TrendingHolder>
{
    List<MovieResult> trendingResults;
    LayoutInflater layoutInflater;

    public TrendingMovieAdopter(List<MovieResult> trendingResults)
    {
        this.trendingResults = trendingResults;
    }

    @NonNull
    @Override
    public TrendingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if(layoutInflater == null)
        {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        MovieItemBinding movieItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.movie_item,parent,false);
        return new TrendingHolder(movieItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TrendingHolder holder, @SuppressLint("RecyclerView") int position)
    {
        holder.bind(trendingResults.get(position));
        holder.movieItemBinding.trendingPoster.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Bundle bundle = new Bundle();
                bundle.putParcelable("model",trendingResults.get(position));

                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_detailFragment,bundle);
            }
        });
    }


    @Override
    public int getItemCount()
    {
        return trendingResults.size();
    }

    public void updateTrending(List<MovieResult> trendingMovie)
    {
        trendingMovie.addAll(trendingMovie);
        notifyDataSetChanged();
    }


    public class TrendingHolder extends RecyclerView.ViewHolder
    {
        MovieItemBinding movieItemBinding;

        public TrendingHolder(@NonNull MovieItemBinding movieItemBinding)
        {
            super(movieItemBinding.getRoot());
            this.movieItemBinding = movieItemBinding;

        }

        public void bind(MovieResult moviesResult)
        {
            loadPoster(moviesResult);
            loadRate(moviesResult);
            loadName(moviesResult);

            movieItemBinding.executePendingBindings();

        }


        private void loadPoster(MovieResult trendingResult)
        {
            Glide.with(movieItemBinding.getRoot().getContext())
                    .load("https://image.tmdb.org/t/p/w500"+ trendingResult.getPosterPath())
                    .thumbnail(Glide.with(movieItemBinding.getRoot().getContext()).load(R.drawable.loading))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(movieItemBinding.trendingPoster);



        }
        private void loadRate(MovieResult trendingResult)
        {
            movieItemBinding.trendingRateButton.setText(String.format("%.1f" ,trendingResult.getVoteAverage()));
        }

        private void loadName(MovieResult trendingResult)
        {
            movieItemBinding.Moviename.setText(trendingResult.getTitle());
        }




    }


}
