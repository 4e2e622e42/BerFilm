package com.ash.berfilm.DetailFragment.Adopters;

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

public class RecommendedSeriesAdopter extends RecyclerView.Adapter<RecommendedSeriesAdopter.RecommendedSeriesViewHolder>
{
    List<MovieResult> recommendedList;
    LayoutInflater layoutInflater;

    public RecommendedSeriesAdopter(List<MovieResult> recommendedList)
    {
        this.recommendedList = recommendedList;
    }

    @NonNull
    @Override
    public RecommendedSeriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if(layoutInflater == null)
        {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        MovieItemBinding movieItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.movie_item,parent,false);

        return new RecommendedSeriesViewHolder(movieItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendedSeriesViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        holder.bind(recommendedList.get(position));
        holder.movieItemBinding.trendingPoster.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Bundle bundle = new Bundle();
                bundle.putParcelable("model",recommendedList.get(position));

                Navigation.findNavController(view).navigate(R.id.action_seriesDetailFragment_self,bundle);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return recommendedList.size();
    }

    public class RecommendedSeriesViewHolder extends RecyclerView.ViewHolder
    {
        MovieItemBinding movieItemBinding;

        public RecommendedSeriesViewHolder(@NonNull MovieItemBinding movieItemBinding)
        {
            super(movieItemBinding.getRoot());

            this.movieItemBinding = movieItemBinding;        }


        public void bind(MovieResult recommendedResult)
        {
            loadPoster(recommendedResult);
            loadRate(recommendedResult);
            loadName(recommendedResult);

            movieItemBinding.executePendingBindings();

        }


        private void loadPoster(MovieResult recommendedSeries)
        {
            Glide.with(movieItemBinding.getRoot().getContext())
                    .load("https://image.tmdb.org/t/p/w500"+ recommendedSeries.getPosterPath())
                    .thumbnail(Glide.with(movieItemBinding.getRoot().getContext()).load(R.drawable.loading))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(movieItemBinding.trendingPoster);



        }
        private void loadRate(MovieResult recommendedSeries)
        {
            movieItemBinding.trendingRateButton.setText(String.format("%.1f" ,recommendedSeries.getVoteAverage()));
        }
        private void loadName(MovieResult recommendedSeries)
        {
            movieItemBinding.Moviename.setText(recommendedSeries.getName());
        }









    }


}
