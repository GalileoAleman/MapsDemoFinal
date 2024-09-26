package com.technicaltest.mapsdemo.di

import android.app.Application
import androidx.room.Room
import com.technicaltest.data.PermissionChecker
import com.technicaltest.data.datasource.LocationDataSource
import com.technicaltest.data.datasource.PostsRemoteDataSource
import com.technicaltest.data.datasource.UserLocalDataSource
import com.technicaltest.mapsdemo.data.AndroidPermissionChecker
import com.technicaltest.mapsdemo.data.PlayServicesLocationDataSource
import com.technicaltest.mapsdemo.data.database.MapsDemoDataBase
import com.technicaltest.mapsdemo.data.database.UserRoomDataSource
import com.technicaltest.mapsdemo.data.server.PostsRestService
import com.technicaltest.mapsdemo.data.server.PostsServerDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application) = Room.databaseBuilder(
        app,
        MapsDemoDataBase::class.java,
        "maps-demo-db"
    ).build()

    @Provides
    @Singleton
    fun provideUserDao(db: MapsDemoDataBase) = db.userDao()

    @Provides
    @Singleton
    @ApiUrl
    fun provideApiUrl() : String = "https://jsonplaceholder.typicode.com/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = HttpLoggingInterceptor().run {
        level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder().addInterceptor(this).build()
    }

    @Singleton
    @Provides
    fun providePostsRestService(@ApiUrl apiUrl : String, okHttpClient: OkHttpClient): PostsRestService {
        val retrofit = Retrofit.Builder()
            .baseUrl(apiUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        return retrofit.create(PostsRestService::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AppDataModule{

    @Binds
    abstract fun bindUserLocalDataSource(impl: UserRoomDataSource): UserLocalDataSource

    @Binds
    abstract fun bindPostsRemoteDataSource(impl: PostsServerDataSource): PostsRemoteDataSource

    @Binds
    abstract fun bindLocationDataSource(locationDataSource: PlayServicesLocationDataSource): LocationDataSource

    @Binds
    abstract fun bindPermissionChecker(permissionChecker: AndroidPermissionChecker): PermissionChecker
}