package com.technicaltest.mapsdemo.data.server.remote

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class PostsResItem(
	val id: Int? = null,
	val title: String? = null,
	val body: String? = null,
	val userId: Int? = null
) : Parcelable
