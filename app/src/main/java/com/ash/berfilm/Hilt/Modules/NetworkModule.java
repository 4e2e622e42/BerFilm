package com.ash.berfilm.Hilt.Modules;

import com.ash.berfilm.AppRepository;
import com.ash.berfilm.Service.ApiClient;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule
{
    String BaseUrl = "https://api.themoviedb.org/3/";

    @Provides
    @Singleton
    Retrofit providesRetrofit()
    {
        return new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    @Provides
    @Singleton
    ApiClient providesApi(Retrofit retrofit)
    {
        return retrofit.create(ApiClient.class);
    }


    @Provides
    @Singleton
    AppRepository appRepository(ApiClient apiClient)
    {
        return new AppRepository(apiClient);
    }




}
