package me.alfredobejarano.movieslist.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import me.alfredobejarano.movieslist.NavHostActivity
import me.alfredobejarano.movieslist.details.MovieDetailsFragment
import me.alfredobejarano.movieslist.movielist.MovieListFragment
import me.alfredobejarano.movieslist.search.MovieSearchFragment

@Module
abstract class UiModule {
    @ContributesAndroidInjector
    abstract fun contributeNavHostActivity(): NavHostActivity

    @ContributesAndroidInjector
    abstract fun contributeMovieListFragment(): MovieListFragment

    @ContributesAndroidInjector
    abstract fun contributeMovieSearchFragment(): MovieSearchFragment

    @ContributesAndroidInjector
    abstract fun contributeMovieDetailsFragment(): MovieDetailsFragment
}