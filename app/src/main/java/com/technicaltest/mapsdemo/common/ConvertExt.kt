package com.technicaltest.mapsdemo.common

import com.technicaltest.domain.Posts
import com.technicaltest.domain.User
import com.technicaltest.mapsdemo.data.server.remote.PostsResItem
import com.technicaltest.mapsdemo.data.database.User as DbUser

fun DbUser.toDomainModel(): User = User(
    email = email,
    password = password,
    loged = loged
)

fun User.fromDomainModel(): DbUser = DbUser(
    id = 1,
    email = email,
    password = password,
    loged = loged
)

fun PostsResItem.toDomainModel(): Posts = Posts(
    title = title ?: "",
    body = body ?: ""
)

fun List<PostsResItem>.toDomainModel(): List<Posts> = map { it.toDomainModel() }
