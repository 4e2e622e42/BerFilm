package com.ash.berfilm.SeriesFragment.Adopter;

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

public class SeriesAdopter extends RecyclerView.Adapter<SeriesAdopter.SeriesViewHolder>
{
    List<MovieResult> seriesResultList;
    LayoutInflater layoutInflater;

    public SeriesAdopter(List<MovieResult> seriesResultList)
    {
        this.seriesResultList = seriesResultList;
    }

    @NonNull
    @Override
    public SeriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if(layoutInflater == null)
        {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        MovieFragItemBinding movieFragItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.movie_frag_item,parent,false);

        return new SeriesViewHolder(movieFragItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SeriesViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        holder.bind(seriesResultList.get(position));


        holder.seriesFragItemBinding.trendingPoster.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Bundle bundle = new Bundle();
                bundle.putParcelable("model",seriesResultList.get(position));

                Navigation.findNavController(view).navigate(R.id.action_seriesFragment_to_seriesDetailFragment,bundle);
            }
        });


    }

    @Override
    public int getItemCount()
    {
        return seriesResultList.size();
    }




    public void updateData(List<MovieResult> seriesResultList)
    {
        this.seriesResultList.addAll(seriesResultList);
        notifyDataSetChanged();
    }




    public class SeriesViewHolder extends RecyclerView.ViewHolder
    {
        MovieFragItemBinding seriesFragItemBinding;

        public SeriesViewHolder(@NonNull MovieFragItemBinding seriesFragItemBinding)
        {
            super(seriesFragItemBinding.getRoot());

            this.seriesFragItemBinding = seriesFragItemBinding;


        }

        public void bind(MovieResult seriesResult)
        {
            loadPoster(seriesResult);
            loadRate(seriesResult);
            loadName(seriesResult);

            seriesFragItemBinding.executePendingBindings();

        }


        private void loadPoster(MovieResult seriesResult)
        {
            Glide.with(seriesFragItemBinding.getRoot().getContext())
                    .load("https://image.tmdb.org/t/p/w500"+ seriesResult.getPosterPath())
                    .thumbnail(Glide.with(seriesFragItemBinding.getRoot().getContext()).load(R.drawable.loading))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(seriesFragItemBinding.trendingPoster);



        }
        private void loadRate(MovieResult seriesResult)
        {
            seriesFragItemBinding.trendingRateButton.setText(String.valueOf(seriesResult.getVoteAverage()));
        }
        private void loadName(MovieResult seriesResult)
        {
            seriesFragItemBinding.Moviename.setText(seriesResult.getName());
        }










    }


}
