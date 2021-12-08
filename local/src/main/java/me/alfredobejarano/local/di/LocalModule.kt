package me.alfredobejarano.local.di

import android.app.Application
import dagger.Module
import dagger.Provides
import me.alfredobejarano.local.AppDatabase
import me.alfredobejarano.local.CachesLifeManager

/**
 * Created by alfredo on 2019-08-02.
 * Copyright © 2019 GROW. All rights reserved.
 */
@Module
class LocalModule(private val app: Application) {
    @Provides
    fun provideMovieDao() = AppDatabase.getInstance(app).provideMovieDao()

    @Provides
    fun provideMovieListIndexDao() = AppDatabase.getInstance(app).provideMovieListIndexDao()

    @Provides
    fun provideMovieDetailsDao() = AppDatabase.getInstance(app).provideMovieDetailsDao()

    @Provides
    fun provideCacheManager() = CachesLifeManager(app)
}