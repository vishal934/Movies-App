package com.example.movies.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.movies.data.repository.movies.MovieRepository
import com.example.movies.data.repository.popular.PopularMovieRepository
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(repository: PopularMovieRepository):ViewModel() {

    val movie = repository.getPopularMovies().cachedIn(viewModelScope)
}