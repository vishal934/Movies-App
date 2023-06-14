package com.example.movies.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.movies.databinding.LayoutPageLoadStateBinding

class PageLoadStateView(context: Context, attributeSet: AttributeSet? =null) : FrameLayout(context,attributeSet) {


    var binding= LayoutPageLoadStateBinding.inflate(LayoutInflater.from(context),this,true)
    fun hide(){
        binding.root.isVisible=false
    }

    fun showErrorMessage(message:String,isRetryVisible:Boolean=true)= with(binding){
        binding.root.isVisible=true
        progressBar.isInvisible=true
        retryButton.isVisible=isRetryVisible
        messageTextView.apply {
            isVisible=true
            text=message
        }
    }

    fun hideErrorMessage()= with(binding){
        retryButton.isVisible=false
        messageTextView.isVisible=false
    }

    var isLoadingVisible:Boolean=false
        set(value) {
            if(value){
                showLoading()
            }
            else{
                hideLoading()
            }
            field=value
        }
    fun showLoading()= with(binding){
        binding.root.isVisible=true
        progressBar.isInvisible=false
        retryButton.isVisible=false
        messageTextView.isVisible=false
    }

    fun hideLoading()= with(binding){
        progressBar.isVisible=false
    }

    fun setOnRetryClickListener(listener:(View) -> Unit){
        binding.retryButton.setOnClickListener(listener)
    }
}