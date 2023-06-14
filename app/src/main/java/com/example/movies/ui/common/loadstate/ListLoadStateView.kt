package com.example.movies.ui.common.loadstate

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.movies.R
import com.example.movies.databinding.LayoutListLoadStateBinding
import java.util.jar.Attributes

class ListLoadStateView (context: Context, attributeSet: AttributeSet?=null):FrameLayout(context,attributeSet){

    private val  binding =LayoutListLoadStateBinding.inflate(LayoutInflater.from(context),this,true)
    fun hide(){
        binding.root.isVisible =false
    }
    fun showErrorMessage(message:String,isRetryVisible:Boolean=true)=with(binding){
        root.isVisible = true
        progressBar.isInvisible=true
        retryButton.isVisible=isRetryVisible
        messageTextView.apply {
            text =message
            isVisible =true
        }
    }

    fun hideErrorMessage()= with(binding){
        retryButton.isVisible =false
        messageTextView.isVisible=false
    }
    var isLoadingVisible:Boolean = false
    set(value){
        if(value){
            showloading()
        }else hideLoading()
        field = value
    }

    private fun hideLoading() =with(binding){
        progressBar.isVisible=false
    }

    private fun showloading()=with(binding) {
        root.isVisible=true
        progressBar.isInvisible=false
        retryButton.isVisible=false
        messageTextView.apply {
            isVisible=true
            text=context.getString(R.string.loading)
        }
    }
    fun setOnRetryListener(listener:(View)-> Unit){
        binding.retryButton.setOnClickListener (listener)
    }

}