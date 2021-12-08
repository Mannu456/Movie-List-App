package me.alfredobejarano.movieslist.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import me.alfredobejarano.movieslist.R
import me.alfredobejarano.movieslist.movielist.MovieListFragmentDirections

fun Fragment.hideSoftKeyboard() {
    val inputMethodManager = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
    inputMethodManager?.hideSoftInputFromWindow(requireView().windowToken, 0)
}

fun Fragment.openMovieDetails(movieId: Int, moviePosterId: View) {
    val args = MovieListFragmentDirections.openMovieDetails(movieId).arguments
    val extras = FragmentNavigatorExtras(
        moviePosterId to "moviePosterView"
    )
    findNavController().navigate(R.id.movieDetailsFragment, args, null, extras)
}