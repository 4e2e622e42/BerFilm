package com.ash.berfilm.MovieFragment.Adopter;

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
import com.ash.berfilm.databinding.MovieFragItemBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.List;

public class MoviesAdopter extends RecyclerView.Adapter<MoviesAdopter.MoviesViewHolder>
{
    List<MovieResult> movieResultList;
    LayoutInflater inflater;

    public MoviesAdopter(List<MovieResult> movieResultList)
    {
        this.movieResultList = movieResultList;
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if(inflater == null)
        {
            inflater = LayoutInflater.from(parent.getContext());
        }
        MovieFragItemBinding movieFragItemBinding = DataBindingUtil.inflate(inflater, R.layout.movie_frag_item,parent,false);

        return new MoviesViewHolder(movieFragItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        holder.bind(movieResultList.get(position));

        holder.movieFragItemBinding.trendingPoster.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Bundle bundle = new Bundle();
                bundle.putParcelable("model",movieResultList.get(position));

                Navigation.findNavController(view).navigate(R.id.action_movieFragment_to_detailFragment,bundle);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return movieResultList.size();
    }
    public void updateMovie(List<MovieResult> movieResults)
    {
        this.movieResultList.addAll(movieResults);
        notifyDataSetChanged();
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder
    {
        MovieFragItemBinding movieFragItemBinding;
        public MoviesViewHolder(@NonNull MovieFragItemBinding movieFragItemBinding)
        {
            super(movieFragItemBinding.getRoot());
            this.movieFragItemBinding = movieFragItemBinding;
        }

        public void bind(MovieResult moviesResult)
        {
            loadPoster(moviesResult);
            loadRate(moviesResult);
            loadName(moviesResult);

            movieFragItemBinding.executePendingBindings();

        }


        private void loadPoster(MovieResult movieResult)
        {
            if(movieResult.getPosterPath() != null)
            {
                Glide.with(movieFragItemBinding.getRoot().getContext())
                        .load("https://image.tmdb.org/t/p/w500" + movieResult.getPosterPath())
                        .thumbnail(Glide.with(movieFragItemBinding.getRoot().getContext()).load(R.drawable.loading))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(movieFragItemBinding.trendingPoster);
            }else
            {
                movieFragItemBinding.trendingPoster.setImageResource(R.drawable.question_mark);
            }


        }
        private void loadRate(MovieResult movieResult)
        {
            movieFragItemBinding.trendingRateButton.setText(String.valueOf(movieResult.getVoteAverage()));
        }
        private void loadName(MovieResult movieResult)
        {
            movieFragItemBinding.Moviename.setText(movieResult.getTitle());
        }











    }


}
