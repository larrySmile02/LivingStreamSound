package com.md.livingstreamsound

import android.app.Application
import android.util.Log
import com.md.network.api.Album
import com.md.network.api.Audio
import com.md.network.api.Category
import com.md.network.api.RetrofitBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

/**
 * @author : liyue
 * created : 2021/2/14
 * desc: 组件及三方库初始化
 */
class LivingApp : Application() {


    override fun onCreate() {
        super.onCreate()
//        GlobalScope.launch {
//            try {
//                // Wait (suspend) for reult
//
//                val categories: List<Category> = RetrofitBuilder.apiService.getCategoriesList()
//                Log.d("ApiTest", "getCategories: $categories")
//
//                val dailyAlbums: List<Album> = RetrofitBuilder.apiService.getDailyAlbums()
//                Log.d("ApiTest", "getDailyAlbums: $dailyAlbums")
//
//                val category1: Category = RetrofitBuilder.apiService.getCategoryInfo(1)
//                // Now we can work with result object
//                Log.d("ApiTest", "getCategory: $category1")
//
//                val album1: Album = RetrofitBuilder.apiService.getAlbumInfo(1)
//                Log.d("ApiTest", "getAlbum: $album1")
//                val audio1: Audio = RetrofitBuilder.apiService.getAudioInfo(1)
//                Log.d("ApiTest", "getAudio: $audio1")
//
//            } catch (e: HttpException) {
//                // Catch http errors
//                Log.e("ApiTest", "exception${e.code()}", e)
//
//                println()
//            } catch (e: Throwable) {
//                Log.e("ApiTest", "exception", e)
//            }
//        }
    }
}