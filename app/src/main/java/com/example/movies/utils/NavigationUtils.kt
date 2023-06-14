package com.example.movies.utils

import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.Navigator
import timber.log.Timber

fun NavController.navigateSafe(direction:NavDirections,extras: Navigator.Extras?=null){

 if(currentDestination?.getAction(direction.actionId)!=null){
     if(extras != null){
         navigate(direction,extras)
     }else{
         navigate(direction)
     }
 }else {
     Timber.w(Throwable("trying to navigate to a unknown destination"))
 }

}