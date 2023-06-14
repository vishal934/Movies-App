package com.example.movies.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.movies.data.repository.movies.MovieRepository
import com.example.movies.utils.withstate
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor( private val movieRepository: MovieRepository):ViewModel() {
 fun loadMovie(id:Long) = withstate(500) {  movieRepository.getMovie(id)}.asLiveData(viewModelScope.coroutineContext)
}