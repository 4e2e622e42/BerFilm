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

import com.ash.berfilm.Models.Credits.Cast;
import com.ash.berfilm.R;
import com.ash.berfilm.databinding.CastItemBinding;
import com.bumptech.glide.Glide;

import java.util.List;

public class SeriesCastAdopter extends RecyclerView.Adapter<SeriesCastAdopter.SeriesCastViewHolder>
{
    List<Cast> castList;
    LayoutInflater layoutInflater;

    public SeriesCastAdopter(List<Cast> castList)
    {
        this.castList = castList;
    }

    @NonNull
    @Override
    public SeriesCastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if(layoutInflater == null)
        {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        CastItemBinding castItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.cast_item,parent,false);

        return new SeriesCastViewHolder(castItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SeriesCastViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        holder.bind(castList.get(position));

        holder.castItemBinding.castImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Bundle bundle = new Bundle();
                bundle.putParcelable("credits",castList.get(position));
                Navigation.findNavController(view).navigate(R.id.action_seriesDetailFragment_to_castInformationFragment2,bundle);


            }
        });
    }

    @Override
    public int getItemCount()
    {
        return castList.size();
    }

    public class SeriesCastViewHolder extends RecyclerView.ViewHolder
    {
        CastItemBinding  castItemBinding;
        public SeriesCastViewHolder(@NonNull CastItemBinding castItemBinding)
        {
            super(castItemBinding.getRoot());

            this.castItemBinding = castItemBinding;
        }


        public void bind(Cast casts)
        {

            castInfo(casts);

        }

        private void castInfo(Cast cast)
        {
            //Load Actor Picture
            if(cast.getProfilePath() !=null)
            {
                Glide.with(castItemBinding.getRoot().getContext())
                        .load("https://image.tmdb.org/t/p/w500/"+cast.getProfilePath())
                        .into(castItemBinding.castImage);
            }else
            {
                Glide.with(castItemBinding.getRoot().getContext())
                        .load(R.drawable.ic_baseline_person_24)
                        .into(castItemBinding.castImage);

            }

            //Load Actor Name and Their Character
            castItemBinding.castName.setText(cast.getName()+"\nas"+"\n"+cast.getCharacter());
        }









    }

}
