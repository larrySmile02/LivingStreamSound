package com.md.network.api

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiServiceV2 {
    @GET("categories")
    fun getCategoriesList(): Observable<ArrayList<ICategory>>

    @GET("daily")
    fun getDailyAlbums(): Observable<ArrayList<Album>>

    @GET("category/{id}")
    fun getCategoryInfo(@Path("id") id: Int): Observable<Category>

    @GET("album/{id}")
    fun getAlbumInfo(@Path("id") id: Int): Observable<Album>

    @GET("audio/{id}")
    fun getAudioInfo(@Path("id") id: Int): Observable<Audio>

}