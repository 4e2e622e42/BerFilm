package com.ash.berfilm.SearchFragment.Movies.Adopters;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.ash.berfilm.Models.MovieModel.Movie;
import com.ash.berfilm.Models.MovieModel.MovieResult;
import com.ash.berfilm.R;
import com.ash.berfilm.databinding.SearchedItemBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.List;

public class SearchMovieAdopter extends RecyclerView.Adapter<SearchMovieAdopter.SearchMovieViewHolder>
{
    List<MovieResult> movieSearchList;
    LayoutInflater layoutInflater;

    public SearchMovieAdopter(List<MovieResult> movieSearchList)
    {
        this.movieSearchList = movieSearchList;
    }

    @NonNull
    @Override
    public SearchMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if(layoutInflater == null)
        {
            layoutInflater  = LayoutInflater.from(parent.getContext());
        }
        SearchedItemBinding searchedItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.searched_item,parent,false);
        return new SearchMovieViewHolder(searchedItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchMovieViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        holder.bind(movieSearchList.get(position));

        holder.searchedItemBinding.mainPoster.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Bundle bundle = new Bundle();
                bundle.putParcelable("model",movieSearchList.get(position));

                Navigation.findNavController(view).navigate(R.id.action_movieSearchFragment_to_detailFragment,bundle);
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return movieSearchList.size();
    }



    public void addItems(List<MovieResult> movieSearchList)
    {
        this.movieSearchList.addAll(movieSearchList);
        notifyDataSetChanged();
    }





    public class SearchMovieViewHolder extends RecyclerView.ViewHolder
    {
        SearchedItemBinding searchedItemBinding;

        public SearchMovieViewHolder(@NonNull SearchedItemBinding searchedItemBinding)
        {
            super(searchedItemBinding.getRoot());
            this.searchedItemBinding = searchedItemBinding;
        }


        public void bind(MovieResult searchResult)
        {
            if(searchResult.getPosterPath() != null)
            {
            Glide.with(searchedItemBinding.getRoot().getContext())
                    .load("https://image.tmdb.org/t/p/w500"+ searchResult.getPosterPath())
                    .thumbnail(Glide.with(searchedItemBinding.getRoot().getContext()).load(R.drawable.loading))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(searchedItemBinding.mainPoster);
            }else
            {
                searchedItemBinding.mainPoster.setImageResource(R.drawable.question_mark);
            }


            searchedItemBinding.name.setText(searchResult.getTitle());

            searchedItemBinding.RateButton.setText(String.format("%.1f",searchResult.getVoteAverage()));


        }












    }


}
