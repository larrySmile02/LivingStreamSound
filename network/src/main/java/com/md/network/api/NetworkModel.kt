package com.md.network.api


open interface ICategory
//首页写死数据，后续网络请求
data class LocalCategory(val localRes: Int,
                         val name: String,
                         val id: String,
                         val logo: String):ICategory

data class Category(
        val author: String,
        val desc: String,
        val groups: List<Group>,
        val id: String,
        val logo: String,
        val name: String
):ICategory

data class Group(
        val albums: List<Album>,
        val name: String
)

data class Album(
        val audios: List<Audio>,
        val author: String,
        val cover: String,
        val desc: String,
        val id: String,
        val name: String
)

data class Audio(
        val author: String,
        val cover: String,
        val desc: String,
        val encrypt: Int,
        val id: String,
        val name: String,
        val url: String
)
