package com.ash.berfilm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.widget.PopupMenu;

import com.ash.berfilm.Models.MovieModel.Movie;
import com.ash.berfilm.ViewModel.AppViewModel;
import com.ash.berfilm.databinding.ActivityMainBinding;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogAnimation;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity
{
    ActivityMainBinding activityMainBinding;
    NavHostFragment navHostFragment;
    NavController navController;
    AppBarConfiguration appBarConfiguration;
    AppViewModel appViewModel;
    CompositeDisposable disposable;
    public DrawerLayout drawerLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);


        drawerLayout = activityMainBinding.drawerLayout;

        disposable = new CompositeDisposable();

        setUpViewModel();
        setUpNavigationComponent();



    }

    private void setUpNavigationComponent()
    {
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        navController   = navHostFragment.getNavController();

        appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment,R.id.movieFragment,R.id.seriesFragment)
                .setOpenableLayout(activityMainBinding.drawerLayout)
                .build();


        NavigationUI.setupWithNavController(activityMainBinding.navigationView,navController);


        setupSmoothBottomBar();

    }

    private void setupSmoothBottomBar()
    {
        PopupMenu popupMenu = new PopupMenu(this,null);
        popupMenu.inflate(R.menu.smooth_bottombar_menu);
        Menu menu = popupMenu.getMenu();
        activityMainBinding.smoothBottombar.setupWithNavController(menu,navController);
    }

    private void setUpViewModel()
    {
        appViewModel = new ViewModelProvider(MainActivity.this).get(AppViewModel.class);

    }



    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        disposable.clear();
    }
}