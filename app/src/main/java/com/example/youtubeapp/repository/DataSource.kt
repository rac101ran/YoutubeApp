package com.example.youtubeapp.repository

import android.graphics.pdf.PdfDocument.PageInfo
import com.google.gson.annotations.SerializedName
import java.math.BigInteger

data class UserSignUpResponse(
    @SerializedName("user_name") val user_name : String,
    @SerializedName("user_email") val user_email : String,
    @SerializedName("password") val password : String,
    @SerializedName("id") val id : Int
)

data class ApiResponse(@SerializedName("status") val status: Int, @SerializedName("message") val message: String , @SerializedName("data") val data : UserSignUpResponse)
data class UserSignUpRequest(
    @SerializedName("user_name") val user_name : String,
    @SerializedName("user_email") val user_email : String,
    @SerializedName("password") val password : String,
)

data class VideoData(
    @SerializedName("id") val id : Int,
    @SerializedName("videoId") val videoId : String,
    @SerializedName("title") val title : String,
    @SerializedName("publishedAt") val publishedAt : String,
    @SerializedName("thumbnailUrl") val thumbnailUrl : String,
    @SerializedName("videoUrl") val videoUrl : String,
    @SerializedName("description") val description : String
)

data class PageData(
    @SerializedName("pageNum") val pageNum : Int,
    @SerializedName("limitPerPage") val limit : Int,
    @SerializedName("totalPages") val totalPages : Int,
)

data class VideoResponse(
    @SerializedName("status") val code : Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data : List<VideoData>,
    @SerializedName("pageInfo") val pageInfo : PageData
)


data class AddWatchLaterResponse(
    @SerializedName("status") val code : Int,
    @SerializedName("message") val message : String
)

data class AllWatchLaterResponse(
    @SerializedName("status") val code : Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data : List<VideoData>,
)
