package com.example.youtubeapp.repository

import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface VideoService {
    @GET("/videos")
    suspend fun getAllVideosResponse(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("userId") userId: Int?
    ): VideoResponse

    @POST(":userId/watch-later/:videoId")
    fun addVideoToWatchLater(
        @Path("userId") userId: Int?,
        @Path("videoId") videoId: String
    ): AddWatchLaterResponse

    @GET(":userId/watch-later/all")
    suspend fun getAllWatchLaterVideos(
        @Path("userId") userId: Int?
    ): AllWatchLaterResponse

    @DELETE(":userId/watch-later/:videoId")
    suspend fun deleteWatchListVideo(
        @Path("userId") userId : Int?,
        @Path("videoId") videoId : String
    ): Call<AddWatchLaterResponse>
}