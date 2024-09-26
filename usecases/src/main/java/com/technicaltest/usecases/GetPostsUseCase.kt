package com.technicaltest.usecases

import com.technicaltest.data.PostsRepository
import com.technicaltest.domain.PostsListResult
import javax.inject.Inject

class GetPostsUseCase @Inject constructor(private val postsRepository: PostsRepository) {
    suspend operator fun invoke(): PostsListResult = postsRepository.getPosts()
}
