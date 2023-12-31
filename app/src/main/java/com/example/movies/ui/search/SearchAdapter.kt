package com.example.movies.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movies.R
import com.example.movies.databinding.ListItemSearchBinding
import com.example.movies.domain.Movie
import com.example.movies.utils.dateTransitionName
import com.example.movies.utils.posterTransitionName
import com.example.movies.utils.titleTransitionName

class SearchAdapter(
    private val onItemClick: (Movie, ImageView, TextView, TextView) -> Unit,
) : PagingDataAdapter<Movie, SearchItemViewHolder>(MOVIE_COMPARATOR) {
    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
        getItem(position)?.let { holder.onBind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder =
        SearchItemViewHolder.create(parent, onItemClick)

    companion object {
        private val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem == newItem
        }
    }

}
class SearchItemViewHolder(
    private val binding: ListItemSearchBinding,
    private val onItemClick: (Movie, ImageView, TextView, TextView) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    private var _movie: Movie? = null

    init {
        with(binding) {
            root.setOnClickListener {
                _movie?.let {
                    onItemClick.invoke(
                        it,
                        posterImageView,
                        titleTextView,
                        dateTextView
                    )
                }
            }
        }

    }

    fun onBind(movie: Movie) = with(binding) {
        _movie = movie

        titleTextView.text = movie.title
        dateTextView.text = movie.releaseDate
        overviewTextView.text = movie.overview

        posterImageView.transitionName = posterTransitionName(movie.id)
        titleTextView.transitionName = titleTransitionName(movie.id)
        dateTextView.transitionName = dateTransitionName(movie.id)

        Glide.with(root.context)
            .load(movie.posterUrl)
            .placeholder(R.drawable.ic_place_holder_24)
            .into(posterImageView)
    }

    companion object {
        fun create(parent: ViewGroup, onItemClick: (Movie, ImageView, TextView, TextView) -> Unit) =
            SearchItemViewHolder(
                ListItemSearchBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ),
                onItemClick
            )
    }
}