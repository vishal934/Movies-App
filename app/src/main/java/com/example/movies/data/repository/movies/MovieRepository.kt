package com.example.movies.data.repository.movies


import com.example.movies.domain.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import java.net.UnknownHostException
import javax.inject.Inject

class MovieRepository @Inject constructor(private val local:MovieLocalDataSource,private val  remote :MovieRemoteDataSource) {

    fun getMovie(id:Long): Flow<Movie?>  = local.getMovieFlow(id).transform { localMovie->
        emit(localMovie?.toMovie())
        try{
            if(localMovie ==null ){
                remote.getMovie(id).let {
                    local.insert(it.toMovieEntity())
                }
            }
        }
        catch (e: UnknownHostException){
            throw (e)
        }
    };
}