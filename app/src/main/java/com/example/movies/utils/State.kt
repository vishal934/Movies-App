package com.example.movies.utils

import androidx.paging.RemoteMediator
import com.example.movies.domain.utils.IIError
import com.example.movies.domain.utils.toIIError
import kotlinx.coroutines.flow.*


sealed  class State<out R> {
    object Loading:State<Nothing>()
    data class  Success<out V>(val value:V):State<V>()
    data class Failure<out V>(val error: IIError):State<V>()
}
 fun<V> loadingState():State<V> = State.Loading
fun <V> successState(value:V):State<V> = State.Success(value)
fun <V> failureState(error: IIError):State<V> = State.Failure(error)

fun <T> withstate(debounce:Long=0,flowProvider:()->Flow<T>):Flow<State<T>>{
    return flow{
        emit(loadingState())
      flowProvider.invoke()
    .debounce(debounce)
    .mapLatest { successState(it) }
    .catch { emit(failureState(it.toIIError())) }
    .also { emitAll(it) }
    }
}