package com.ash.berfilm.SearchFragment.Series.Adopter;

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
import com.ash.berfilm.databinding.SearchedItemBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.List;

public class SearchSeriesAdopter extends RecyclerView.Adapter<SearchSeriesAdopter.SearchSeriesViewHolder>
{
    List<MovieResult> searchResult;
    LayoutInflater layoutInflater;

    public SearchSeriesAdopter(List<MovieResult> searchResult)
    {
        this.searchResult = searchResult;
    }

    @NonNull
    @Override
    public SearchSeriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if(layoutInflater == null)
        {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        SearchedItemBinding searchedItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.searched_item,parent,false);

        return new SearchSeriesViewHolder(searchedItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchSeriesViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        holder.bind(searchResult.get(position));

        holder.searchedItemBinding.mainPoster.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Bundle bundle = new Bundle();
                bundle.putParcelable("model",searchResult.get(position));

                Navigation.findNavController(view).navigate(R.id.action_seriesSearchFragment_to_seriesDetailFragment,bundle);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return searchResult.size();
    }

    public void addItems(List<MovieResult> searchResult)
    {
        this.searchResult.addAll(searchResult);
        notifyDataSetChanged();
    }




    public class SearchSeriesViewHolder extends RecyclerView.ViewHolder
    {
        SearchedItemBinding searchedItemBinding;
        public SearchSeriesViewHolder(@NonNull SearchedItemBinding searchedItemBinding)
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


            searchedItemBinding.name.setText(searchResult.getName());
            searchedItemBinding.RateButton.setText(String.format("%.1f",searchResult.getVoteAverage()));


        }












    }



}
