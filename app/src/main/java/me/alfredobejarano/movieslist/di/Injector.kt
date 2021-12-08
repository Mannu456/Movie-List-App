package me.alfredobejarano.movieslist.di

import android.app.Application
import me.alfredobejarano.local.di.LocalModule
import me.alfredobejarano.movieslist.BuildConfig
import me.alfredobejarano.movieslist.remote.di.RemoteModule

/**
 * Created by alfredo on 2019-08-02.
 */
object Injector {
    @Volatile
    private lateinit var app: Application

    /**
     * Initializes the singleton access for the [DaggerAppComponent] object.
     */
    fun init(application: Application) {
        app = application
    }

    /**
     * Singleton reference for the [AppComponent] class.
     */
    val component: AppComponent by lazy {
        DaggerAppComponent
            .builder()
            .application(app)
            .localModule(LocalModule(app))
            .remoteModule(
                RemoteModule(
                    BuildConfig.DEBUG,
                    BuildConfig.API_KEY,
                    BuildConfig.BASE_URL,
                    BuildConfig.POSTER_BASE_URL
                )
            )
            .build()
    }
}