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

public class TrendingSeriesAdopter extends RecyclerView.Adapter<TrendingSeriesAdopter.TrendingSeriesViewHolder>
{
    List<MovieResult> trendingSeriesResults;
    LayoutInflater layoutInflater;

    public TrendingSeriesAdopter(List<MovieResult> trendingSeriesResults)
    {
        this.trendingSeriesResults = trendingSeriesResults;
    }

    @NonNull
    @Override
    public TrendingSeriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if(layoutInflater == null)
        {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        MovieItemBinding movieItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.movie_item,parent,false);

        return new TrendingSeriesViewHolder(movieItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TrendingSeriesViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        holder.bind(trendingSeriesResults.get(position));
        holder.movieItemBinding.trendingPoster.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Bundle bundle = new Bundle();
                bundle.putParcelable("model",trendingSeriesResults.get(position));

                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_seriesDetailFragment,bundle);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return trendingSeriesResults.size();
    }

    public void updateSeries(List<MovieResult> trendingSeriesResults)
    {
        trendingSeriesResults.addAll(trendingSeriesResults);
        notifyDataSetChanged();
    }

    public class TrendingSeriesViewHolder extends RecyclerView.ViewHolder
    {
        MovieItemBinding movieItemBinding;


        public TrendingSeriesViewHolder(@NonNull MovieItemBinding movieItemBinding)
        {
            super(movieItemBinding.getRoot());
            this.movieItemBinding = movieItemBinding;
        }

        public void bind(MovieResult seriesResult)
        {
            loadPoster(seriesResult);
            loadRate(seriesResult);
            loadName(seriesResult);

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
            movieItemBinding.Moviename.setText(trendingResult.getName());
        }








    }
}
