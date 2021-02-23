package com.md.network.api

data class Category(
        val author: String,
        val desc: String,
        val groups: List<Group>,
        val id: String,
        val logo: String,
        val name: String
)

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
