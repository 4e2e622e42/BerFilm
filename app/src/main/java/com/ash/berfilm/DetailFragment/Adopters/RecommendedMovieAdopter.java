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

public class RecommendedMovieAdopter extends RecyclerView.Adapter<RecommendedMovieAdopter.RecommendedViewHolder>
{
    List<MovieResult> recommendedList;
    LayoutInflater layoutInflater;

    public RecommendedMovieAdopter(List<MovieResult> recommendedList)
    {
        this.recommendedList = recommendedList;
    }

    @NonNull
    @Override
    public RecommendedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if(layoutInflater == null)
        {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        MovieItemBinding movieItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.movie_item,parent,false);
        return new RecommendedViewHolder(movieItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendedViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        holder.bind(recommendedList.get(position));
        holder.movieItemBinding.trendingPoster.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Bundle bundle = new Bundle();
                bundle.putParcelable("model",recommendedList.get(position));

                Navigation.findNavController(view).navigate(R.id.action_detailFragment_self,bundle);
            }
        });
    }


    @Override
    public int getItemCount()
    {
        return recommendedList.size();
    }

    public class RecommendedViewHolder extends RecyclerView.ViewHolder
    {
        MovieItemBinding movieItemBinding;


        public RecommendedViewHolder(@NonNull MovieItemBinding movieItemBinding)
        {
            super(movieItemBinding.getRoot());
            this.movieItemBinding = movieItemBinding;
        }


        public void bind(MovieResult recommendMoviesResult)
        {
            loadPoster(recommendMoviesResult);
            loadRate(recommendMoviesResult);
            loadName(recommendMoviesResult);

            movieItemBinding.executePendingBindings();

        }


        private void loadPoster(MovieResult recommendMovie)
        {
            Glide.with(movieItemBinding.getRoot().getContext())
                    .load("https://image.tmdb.org/t/p/w500"+ recommendMovie.getPosterPath())
                    .thumbnail(Glide.with(movieItemBinding.getRoot().getContext()).load(R.drawable.loading))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(movieItemBinding.trendingPoster);



        }
        private void loadRate(MovieResult recommendMovie)
        {
            movieItemBinding.trendingRateButton.setText(String.format("%.1f" ,recommendMovie.getVoteAverage()));
        }
        private void loadName(MovieResult recommendMovie)
        {
            movieItemBinding.Moviename.setText(recommendMovie.getTitle());
        }





    }
}
