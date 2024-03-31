package com.ash.berfilm.DetailFragment;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ash.berfilm.Models.CastInfo.CastInfo;
import com.ash.berfilm.Models.Credits.Cast;
import com.ash.berfilm.Models.MovieModel.MovieResult;
import com.ash.berfilm.R;
import com.ash.berfilm.Service.ApiClient;
import com.ash.berfilm.ViewModel.AppViewModel;
import com.ash.berfilm.databinding.FragmentCastInformationBinding;
import com.bumptech.glide.Glide;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CastInformationFragment extends Fragment
{
    FragmentCastInformationBinding fragmentCastInformationBinding;
    CastInfo castInfo;
    AppViewModel appViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        fragmentCastInformationBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_cast_information, container, false);
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);


        Cast castId = getArguments().getParcelable("credits");



        setUpCastInfo(castId.getId());

        return fragmentCastInformationBinding.getRoot();
    }

    private void setUpCastInfo(int id)
    {
        appViewModel.makeCastInfoCall(id).enqueue(new Callback<CastInfo>()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<CastInfo> call, Response<CastInfo> response)
            {
                castInfo = response.body();


                Log.e("TAG","Path:  "+castInfo.getProfilePath());
                if(castInfo.getProfilePath() != null)
                {
                    Glide.with(fragmentCastInformationBinding.getRoot().getContext())
                            .load("https://image.tmdb.org/t/p/w500/"+castInfo.getProfilePath())
                            .into(fragmentCastInformationBinding.picture);
                }else
                {
                    Glide.with(fragmentCastInformationBinding.getRoot().getContext())
                            .load(R.drawable.ic_baseline_person_24)
                            .into(fragmentCastInformationBinding.picture);
                }

                //Calculate CastAge
                LocalDate castBirthday = LocalDate.parse(castInfo.getBirthday());
                LocalDate currentTime = LocalDate.now();

                Period difference  = Period.between(castBirthday,currentTime);

                int castAge = difference.getYears();

                fragmentCastInformationBinding.name.setText(castInfo.getName());
                fragmentCastInformationBinding.birthdayDate.setText(castInfo.getBirthday()+"("+castAge+" yearsOld)");
                fragmentCastInformationBinding.birthdayPlaceText.setText(castInfo.getPlaceOfBirth());
                fragmentCastInformationBinding.deathDateText.setText(castInfo.getDeathday());
                fragmentCastInformationBinding.biography.setText(castInfo.getBiography());


            }

            @Override
            public void onFailure(Call<CastInfo> call, Throwable t)
            {

            }
        });



    }
}