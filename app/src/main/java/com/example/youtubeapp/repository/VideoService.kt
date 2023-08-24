package com.example.youtubeapp.repository

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface VideoService {

    @POST("/users/signup")
    fun createSignUpUser(@Body signUpRequest: UserSignUpRequest): Call<ApiResponse>

    @GET("/users/login")
    fun loginUser(@Query("user_email") email : String, @Query("password") password : String) : Call<LoginResponse>

    @GET("/videos")
    suspend fun getAllVideosResponse(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("userId") userId: Int?
    ): VideoResponse

    @POST("/customer/{userId}/watch-later/{videoId}")
    fun addVideoToWatchLater(
        @Path("userId") userId: Int?,
        @Path("videoId") videoId: String
    ): Call<AddWatchLaterResponse>

    @GET("/customer/{userId}/watch-later/all")
    suspend fun getAllWatchLaterVideos(
        @Path("userId") userId: Int?
    ): AllWatchLaterResponse

    @POST("/customer/{userId}/delete-watch-later/{videoId}")
    fun deleteWatchListVideo(
        @Path("userId") userId : Int?,
        @Path("videoId") videoId : String
    ): Call<AddWatchLaterResponse>
}