package com.example.movies.ui.search

import android.graphics.drawable.ClipDrawable.VERTICAL
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.movies.R
import com.example.movies.databinding.FragmentNavigationSearchBinding
import com.example.movies.domain.Movie
import com.example.movies.ui.common.BaseFragment
import com.example.movies.ui.common.loadstate.ListLoadStateAdapter
import com.example.movies.utils.*
import com.google.android.material.internal.ViewUtils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentNavigationSearchBinding>(){

    private val searchViewModel: SearchViewModel by viewModels()

    private val searchAdapter by ViewLifeCycleDelegates { SearchAdapter(::onMovieClicked) }
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentNavigationSearchBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        listenOnPagerLoadStates()
    }

    private fun initUI()   = with(binding){
        searchViewModel.getSavedQuery().let {
            populateUI(it)
            search(it)
        }
        editText.apply {
            requestFocus()
            showKeyboard()
            setOnKeyActionListener(EditorInfo.IME_ACTION_SEARCH) { updateSearchFromInput() }
            setOnClickListener { isCursorVisible = true }
        }
        searchButton.setOnClickListener { updateSearchFromInput() }
        backButton.setOnClickListener { findNavController().navigateUp() }
        postponeEnterTransition()
        recyclerView.apply {
            adapter = searchAdapter.withLoadStateFooter(
                footer = ListLoadStateAdapter { searchAdapter.retry() }
            )
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            doOnPreDraw { startPostponedEnterTransition() }
        }

        loadStateView.setOnRetryListener { searchAdapter.retry() }
    }

    private fun listenOnPagedData(query: String) = viewLifecycleOwner.lifecycleScope.launch {
        searchViewModel.search(query)?.collectLatest { searchAdapter.submitData(it) }
    }
    private fun listenOnPagerLoadStates() = viewLifecycleOwner.lifecycleScope.launch {
        with(binding) {
            searchAdapter.listenOnLoadState(
                recyclerView,
                loadStateView,
                { searchViewModel.isSearchAnything && searchAdapter.itemCount == 0 },
                getString(R.string.no_popular_movies)
            )
        }
    }

    private fun populateUI(query: String?) = with(binding) {
        query?.let {
            editText.setText(it)
            updateSearchFromInput()
        }
    }

    private fun updateSearchFromInput() = with(binding.editText) {
        text?.trim()?.let {
            hideKeyboard()
            isCursorVisible = false
            if (it.isNotEmpty()) {
                search(it.toString())
            }
        }
    }

    private var searchJob: Job? = null

    private fun search(query: String?) {
        query ?: return
        searchJob?.cancel()
        searchJob = listenOnPagedData(query)
    }

    private fun onMovieClicked(
        movie: Movie,
        posterImageView: ImageView,
        titleTextView: TextView,
        dateTextView: TextView,
    ) {
        val extras = FragmentNavigatorExtras(
            posterImageView to posterImageView.transitionName,
            titleTextView to titleTextView.transitionName,
            dateTextView to dateTextView.transitionName,
        )
        findNavController().navigateSafe(
          SearchFragmentDirections.actionNavigationSearchToNavigationDetails(movie),extras
        )
    }

}