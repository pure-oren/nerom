package com.movieapp.explorer.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import com.google.android.material.tabs.TabLayout;
import com.movieapp.explorer.R;
import com.movieapp.explorer.databinding.FragmentTvShowBinding;
import com.movieapp.explorer.ui.adapter.TvShowAdapter;
import com.movieapp.explorer.ui.viewmodel.TvShowViewModel;

public class TvShowFragment extends Fragment {
    private FragmentTvShowBinding binding;
    private TvShowViewModel viewModel;
    private TvShowAdapter adapter;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTvShowBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        viewModel = new ViewModelProvider(this).get(TvShowViewModel.class);
        setupRecyclerView();
        setupTabs();
        observeTvShows();
    }
    
    private void setupRecyclerView() {
        adapter = new TvShowAdapter(tvShow -> {
            // Handle TV show click - navigate to detail
        });
        binding.recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.recyclerView.setAdapter(adapter);
    }
    
    private void setupTabs() {
        TabLayout tabLayout = new TabLayout(requireContext());
        tabLayout.addTab(tabLayout.newTab().setText(R.string.popular));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.top_rated));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.on_the_air));
        
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                loadTvShowsByCategory(tab.getPosition());
            }
            
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        
        binding.categoryTabs.addView(tabLayout);
    }
    
    private void loadTvShowsByCategory(int position) {
        binding.progressBar.setVisibility(View.VISIBLE);
        
        switch (position) {
            case 0:
                viewModel.getPopularTvShows().observe(getViewLifecycleOwner(), tvShows -> {
                    adapter.setTvShows(tvShows);
                    binding.progressBar.setVisibility(View.GONE);
                });
                break;
            case 1:
                viewModel.getTopRatedTvShows().observe(getViewLifecycleOwner(), tvShows -> {
                    adapter.setTvShows(tvShows);
                    binding.progressBar.setVisibility(View.GONE);
                });
                break;
            case 2:
                viewModel.getOnTheAirTvShows().observe(getViewLifecycleOwner(), tvShows -> {
                    adapter.setTvShows(tvShows);
                    binding.progressBar.setVisibility(View.GONE);
                });
                break;
        }
    }
    
    private void observeTvShows() {
        viewModel.getPopularTvShows().observe(getViewLifecycleOwner(), tvShows -> {
            if (tvShows != null && !tvShows.isEmpty()) {
                adapter.setTvShows(tvShows);
            }
            binding.progressBar.setVisibility(View.GONE);
        });
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
