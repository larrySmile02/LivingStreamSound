package com.md.network.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    private const val API_DOMAIN = "https://service-d7gclhmt-1256170993.sh.apigw.tencentcs.com/test/"

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(API_DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient())
                .build() //Doesn't require the adapter
    }
    //copy 来的，待研究
    val apiService: ApiService by lazy (LazyThreadSafetyMode.SYNCHRONIZED){
        getRetrofit().create(ApiService::class.java)
    }

    //copy 来的，待研究
    private fun getOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        //可以设置请求过滤的水平,body,basic,headers
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build()
    }
}