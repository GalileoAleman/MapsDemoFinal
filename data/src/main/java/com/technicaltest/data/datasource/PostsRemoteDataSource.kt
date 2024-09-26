package com.technicaltest.data.datasource

import com.technicaltest.domain.PostsListResult

interface PostsRemoteDataSource {
    suspend fun getPosts(): PostsListResult
}
