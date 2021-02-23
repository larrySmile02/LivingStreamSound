package com.md.network.api

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilderv2 {
    private const val API_DOMAIN = "https://service-d7gclhmt-1256170993.sh.apigw.tencentcs.com/test/"

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(API_DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build() //Doesn't require the adapter
    }

    val apiService = getRetrofit().create(ApiServiceV2::class.java)
}