package com.movieapp.explorer.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import com.google.android.material.tabs.TabLayout;
import com.movieapp.explorer.R;
import com.movieapp.explorer.databinding.FragmentMovieBinding;
import com.movieapp.explorer.ui.adapter.MovieAdapter;
import com.movieapp.explorer.ui.viewmodel.MovieViewModel;

public class MovieFragment extends Fragment {
    private FragmentMovieBinding binding;
    private MovieViewModel viewModel;
    private MovieAdapter adapter;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMovieBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        setupRecyclerView();
        setupTabs();
        observeMovies();
    }
    
    private void setupRecyclerView() {
        adapter = new MovieAdapter(movie -> {
            // Handle movie click - navigate to detail
        });
        binding.recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.recyclerView.setAdapter(adapter);
    }
    
    private void setupTabs() {
        TabLayout tabLayout = new TabLayout(requireContext());
        tabLayout.addTab(tabLayout.newTab().setText(R.string.popular));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.top_rated));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.upcoming));
        
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                loadMoviesByCategory(tab.getPosition());
            }
            
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        
        binding.categoryTabs.addView(tabLayout);
    }
    
    private void loadMoviesByCategory(int position) {
        binding.progressBar.setVisibility(View.VISIBLE);
        
        switch (position) {
            case 0:
                viewModel.getPopularMovies().observe(getViewLifecycleOwner(), movies -> {
                    adapter.setMovies(movies);
                    binding.progressBar.setVisibility(View.GONE);
                });
                break;
            case 1:
                viewModel.getTopRatedMovies().observe(getViewLifecycleOwner(), movies -> {
                    adapter.setMovies(movies);
                    binding.progressBar.setVisibility(View.GONE);
                });
                break;
            case 2:
                viewModel.getUpcomingMovies().observe(getViewLifecycleOwner(), movies -> {
                    adapter.setMovies(movies);
                    binding.progressBar.setVisibility(View.GONE);
                });
                break;
        }
    }
    
    private void observeMovies() {
        viewModel.getPopularMovies().observe(getViewLifecycleOwner(), movies -> {
            if (movies != null && !movies.isEmpty()) {
                adapter.setMovies(movies);
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
