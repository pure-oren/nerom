package com.movieapp.explorer.ui.adapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.movieapp.explorer.ui.fragment.MovieFragment;
import com.movieapp.explorer.ui.fragment.TvShowFragment;

public class MainPagerAdapter extends FragmentStateAdapter {
    
    public MainPagerAdapter(AppCompatActivity activity) {
        super(activity);
    }
    
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new MovieFragment();
        } else {
            return new TvShowFragment();
        }
    }
    
    @Override
    public int getItemCount() {
        return 2;
    }
}
