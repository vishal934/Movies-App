package com.example.movies.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movies.databinding.FragmentHomeBinding
import com.example.movies.domain.Movie
import com.example.movies.ui.common.BaseFragment
import com.example.movies.ui.common.loadstate.ListLoadStateAdapter
import com.example.movies.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val homeViewModel: HomeViewModel by viewModels()
    private val homeAdapter by ViewLifeCycleDelegates { HomeAdapter(::onMovieClicked) }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()

            listenOnMoviesPagedData()


        listenOnPagerLoadStates()

    }

    private fun listenOnPagerLoadStates() = viewLifecycleOwner.lifecycleScope.launch{with(binding) {
homeAdapter.listenOnLoadState(
    recyclerView, loadStateView ,{homeAdapter.itemCount ==0},"There is no popular at this time"
)

    }
    }

    private  fun listenOnMoviesPagedData() =viewLifecycleOwner.lifecycleScope.launch{
        homeViewModel.movie.collectLatest {
            homeAdapter.submitData(it)
        }
    }

    private fun initUI() = with(binding) {

        recyclerView.apply {
            setHasFixedSize(true)
            adapter = homeAdapter.withLoadStateFooter(
                footer = ListLoadStateAdapter { homeAdapter.retry() }
            )
            layoutManager = GridLayoutManager(context, getSpansCount()).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        Timber.d("positionhomeAdapter.itemCount $position ${homeAdapter.itemCount}")
                        return if (position >= homeAdapter.itemCount) getSpansCount() else 1
                    }

                }

            }


        }

    }

    private fun getSpansCount() = if (isPortrait()) 2 else 4
    private fun onMovieClicked(
        movie: Movie,
        posterImageView: ImageView,
        titleTextView: TextView,
        dateTextView: TextView
    ) {


        val extras = FragmentNavigatorExtras(
            posterImageView to posterImageView.transitionName,
            titleTextView to titleTextView.transitionName,
            dateTextView to dateTextView.transitionName
        )

      findNavController().navigateSafe(HomeFragmentDirections.actionNavigationHomeToNavigationDetails(movie),extras)

    }

}