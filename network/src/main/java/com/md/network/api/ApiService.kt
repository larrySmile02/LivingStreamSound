package com.md.network.api

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("categories")
    suspend fun getCategoriesList(): List<Category>

    @GET("daily")
    suspend fun getDailyAlbums(): List<Album>

    @GET("category/{id}")
    suspend fun getCategoryInfo(@Path("id") id: Int): Category

    @GET("album/{id}")
    suspend fun getAlbumInfo(@Path("id") id: Int): Album

    @GET("audio/{id}")
    suspend fun getAudioInfo(@Path("id") id: Int): Audio

}