package com.example.youtubeapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youtubeapp.databinding.WatchLaterCardBinding
import com.example.youtubeapp.network.RetrofitClient
import com.example.youtubeapp.repository.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.wait
import java.io.IOException

class VideoViewModel : ViewModel() {
    private val videoService = RetrofitClient.retrofit.create(VideoService::class.java)
    private val _videoList = MutableLiveData<List<VideoData>>()
    val videoList: LiveData<List<VideoData>> = _videoList
    private val _pagesLiveData = MutableLiveData<PageData>()
    val pagesInfo: LiveData<PageData> = _pagesLiveData
    private val _watchLaterData = MutableLiveData<List<VideoData>>()
    val watchLaterList: LiveData<List<VideoData>> = _watchLaterData


    fun getAllVideos(page: Int, limit: Int, userId: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = videoService.getAllVideosResponse(page, limit, userId)
                if (response.code == 200) {
                    _videoList.postValue(response.data)
                    _pagesLiveData.postValue(response.pageInfo)
                }
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
            }
        }
    }


    suspend fun addWaterLaterVideo(videoId: String, userId: Int?): AddWatchLaterResponse? {

        return withContext(Dispatchers.IO) {
            try {
                val response = videoService.addVideoToWatchLater(userId, videoId).execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.e("err message", e.message.toString())
                null
            }
        }
//        }
//        val deferredResponse = CompletableDeferred<Int?>()
//
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val response = videoService.addVideoToWatchLater(userId, videoId)
//                if (response.isSuccessful && response.body()?.code == 200) {
//                    deferredResponse.complete(response.body()?.code)
//                }
//            } catch (e: Exception) {
//                Log.e("error", e.message.toString())
//                deferredResponse.complete(null) // Complete with null in case of an error
//            }
//        }
//        return deferredResponse.await()
    }

    fun getAllWatchLaterVideos(userId: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = videoService.getAllWatchLaterVideos(userId)
                if (response.code == 200) {
                    _watchLaterData.postValue(response.data)
                }
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
            }
        }
    }

    suspend fun deleteFromWatchLaterList(videoId: String, userId: Int?): AddWatchLaterResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val response = videoService.deleteWatchListVideo(userId, videoId).execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.e("err message", e.message.toString())
                null
            }
        }
    }

    suspend fun createSignUpRequest(
        name: String,
        user_email: String,
        password: String
    ): ApiResponse? {
        return withContext(Dispatchers.IO) {
            val signUpRequest = UserSignUpRequest(user_name = name, user_email = user_email, password = password)

            try {
                val response = videoService.createSignUpUser(signUpRequest).execute()
                Log.e("info:",response.body()?.message.toString())
                if (response.isSuccessful) {
                    response.body()
                } else {
                    null
                }
            } catch (e: IOException) {
                null
            }
        }
    }

    suspend fun loginUserRequest(user_email: String , password : String) : LoginResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val response = videoService.loginUser(user_email,password).execute()
                if(response.isSuccessful) {
                    response.body()
                }else {
                    null
                }
            }catch(e : IOException) {
                null
            }
        }
    }

}

