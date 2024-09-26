package com.technicaltest.mapsdemo.data.server

import com.technicaltest.data.datasource.PostsRemoteDataSource
import com.technicaltest.domain.PostsListResult
import com.technicaltest.mapsdemo.common.toDomainModel
import javax.inject.Inject

class PostsServerDataSource @Inject constructor(private val postsRestService: PostsRestService) :
    PostsRemoteDataSource {
    override suspend fun getPosts(): PostsListResult = runCatching {
        postsRestService.getPosts().toDomainModel()
    }.fold(
        onSuccess = { posts ->
            PostsListResult(success = true, posts = posts)
        },
        onFailure = {
            PostsListResult(success = false, posts = null)
        }
    )
}
