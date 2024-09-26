package com.technicaltest.mapsdemo.data.server

import com.technicaltest.mapsdemo.data.server.remote.PostsResItem
import retrofit2.http.GET

interface PostsRestService {
    @GET("posts")
    suspend fun getPosts(): List<PostsResItem>
}
