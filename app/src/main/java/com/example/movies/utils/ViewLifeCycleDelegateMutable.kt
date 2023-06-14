package com.example.movies.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ViewLifeCycleDelegateMutable <T>: ReadWriteProperty<Fragment, T>, LifecycleObserver {
    private var isObserverAdded=false
    private var value : T ? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return value!!
    }

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        if(!isObserverAdded){
            thisRef.viewLifecycleOwner.lifecycle.addObserver(this)
            isObserverAdded = true
        }
        this.value = value
    }
}