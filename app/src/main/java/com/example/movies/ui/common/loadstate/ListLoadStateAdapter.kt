package com.example.movies.ui.common.loadstate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.movies.databinding.LayoutPageLoadStateBinding

class ListLoadStateAdapter(private val retry:()->Unit):LoadStateAdapter<LoadStateViewHolder>() {


    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder =
        LoadStateViewHolder.create(parent, loadState, retry)



}
    class LoadStateViewHolder(private val binding:LayoutPageLoadStateBinding,val retry:()-> Unit):ViewHolder(binding.root){

        init{
            binding.retryButton.setOnClickListener { retry.invoke() }
        }
        fun bind(loadState: LoadState)=with(binding){
            if (loadState is LoadState.Error) {
                messageTextView.text = loadState.error.localizedMessage
            }
            progressBar.isVisible = loadState is LoadState.Loading
            messageTextView.isVisible = loadState !is LoadState.Loading
            retryButton.isVisible = loadState !is LoadState.Loading
        }


        companion object {
            fun create(parent: ViewGroup, loadState: LoadState, retry: () -> Unit) =
                LoadStateViewHolder(
                    LayoutPageLoadStateBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ), retry
                )
        }



}