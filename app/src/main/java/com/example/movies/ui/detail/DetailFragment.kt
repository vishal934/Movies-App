package com.example.movies.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.movies.R
import com.example.movies.databinding.FragmentNavigationDetailsBinding
import com.example.movies.domain.Movie
import com.example.movies.domain.utils.IIError
import com.example.movies.domain.utils.getHumanReadableText
import com.example.movies.ui.common.BaseFragment
import com.example.movies.utils.State
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentNavigationDetailsBinding>() {

     val viewModel :DetailViewModel by viewModels()
    val args:DetailFragmentArgs by navArgs()
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentNavigationDetailsBinding.inflate(inflater,container,false)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI()
        loadData()
    }

    private fun loadData() = viewModel.apply {
        loadMovie(args.movie.id).observe(viewLifecycleOwner){state->
            when(state){
                is State.Failure -> populateError(state.error)
                is State.Loading -> showLoading()
                is State.Success -> state.value?.let { populateUI(it) }
            }


        }
    }

    private fun initUI() = with(binding) {
        val movie = args.movie
        titleTextView.text = movie.title
        dateTextView.text = movie.releaseDate

        Glide.with(requireContext())
            .load(movie.posterUrl)
            .placeholder(R.drawable.ic_place_holder_24)
            .into(posterImageView)
        loadStateView.setOnRetryClickListener { loadData() }


    }
    fun populateError(error: IIError)= with(binding){
        loadStateView.showErrorMessage(error.getHumanReadableText(requireContext()))
    }
    private fun showLoading()= with(binding){
        loadStateView.isLoadingVisible=true
        loadStateView.hideErrorMessage()
    }
    fun populateUI(movie: Movie)= with(binding){
        loadStateView.hide()
        Glide.with(requireContext())
            .load(movie.posterUrl)
            .placeholder(R.drawable.ic_place_holder_24)
            .into(posterImageView)

        ratingView.apply {
            root.isVisible=true
            rateProgress.progress=movie.rate100
            rateText.text=movie.rate100.toString()
        }

        titleTextView.text=movie.title
        dateTextView.text=movie.releaseDate
        overviewTextView.text = movie.overview
        headingOverview.isVisible = true
        loadStateView.hideErrorMessage()
    }
}
