package com.example.movies.ui.search

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movies.data.repository.search.SearchRepository
import com.example.movies.domain.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(val searchRepository: SearchRepository, val savedStateHandle: SavedStateHandle):ViewModel() {
    private var currentQueryValue: String? = null
    private var currentSearchResult: Flow<PagingData<Movie>>? = null
    var isSearchAnything =false

    fun search(query:String):Flow<PagingData<Movie>>?{
        if(query.trim().isEmpty()) return null
        isSearchAnything = true
        val lastResult = currentSearchResult
        if (query == currentQueryValue && lastResult != null) {
            return lastResult
        }
        savedStateHandle[KEY_QUERY_SAVED_STATE] = query
        val newResult = searchRepository.search(query).cachedIn(viewModelScope)
        currentSearchResult = newResult
        Log.d("qtyt",newResult.toString())
      //  currentQueryValue =query
        return newResult
    }

    fun getSavedQuery() = savedStateHandle.get(KEY_QUERY_SAVED_STATE) as? String
    companion object {
        const val KEY_QUERY_SAVED_STATE = "key_query_saved_state"
    }
}