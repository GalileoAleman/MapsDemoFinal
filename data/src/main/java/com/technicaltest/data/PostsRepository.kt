package com.technicaltest.data

import com.technicaltest.data.datasource.PostsRemoteDataSource
import javax.inject.Inject

class PostsRepository @Inject constructor(private val postsRemoteDataSource: PostsRemoteDataSource) {

    suspend fun getPosts() = postsRemoteDataSource.getPosts()
}
