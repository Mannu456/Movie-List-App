package me.alfredobejarano.movieslist.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import me.alfredobejarano.movieslist.details.MovieDetailsViewModel
import me.alfredobejarano.movieslist.movielist.MovieListViewModel
import me.alfredobejarano.movieslist.search.MovieSearchViewModel

/**
 * Created by alfredo on 2019-08-02.
 */
@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MovieListViewModel::class)
    abstract fun bindMovieListViewModel(viewModel: MovieListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieSearchViewModel::class)
    abstract fun bindMovieSearchViewModel(viewModel: MovieSearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailsViewModel::class)
    abstract fun bindMovieMovieDetailsViewModel(viewModel: MovieDetailsViewModel): ViewModel
}