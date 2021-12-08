package me.alfredobejarano.movieslist.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.AndroidSupportInjection
import me.alfredobejarano.movieslist.NavHostViewModel
import me.alfredobejarano.movieslist.R
import me.alfredobejarano.movieslist.core.Movie
import me.alfredobejarano.movieslist.core.Result
import me.alfredobejarano.movieslist.di.ViewModelFactory
import me.alfredobejarano.movieslist.utils.openMovieDetails
import javax.inject.Inject

class MovieSearchFragment : Fragment() {
    companion object {
        const val FRAGMENT_TAG = "search_results"
    }

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var viewModel: MovieSearchViewModel
    private lateinit var navHostViewModel: NavHostViewModel

    private lateinit var searchListRecyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        RecyclerView(requireContext()).apply {
            id = R.id.searchResultsList

            layoutManager = LinearLayoutManager(context)
            searchListRecyclerView = this

            AndroidSupportInjection.inject(this@MovieSearchFragment)

            navHostViewModel = ViewModelProviders.of(requireActivity())[NavHostViewModel::class.java]
            viewModel = ViewModelProviders.of(this@MovieSearchFragment, factory)[MovieSearchViewModel::class.java]
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navHostViewModel.searchQueryLiveData.observe(this, Observer { query -> searchMovie(query ?: "") })
    }

    private fun searchMovie(query: String) = viewModel.searchMovieByTitle(query).observe(this, Observer { result ->
        when (result.status) {
            Result.Status.LOADING -> Log.d(this.javaClass.name, "Loading")
            Result.Status.OK -> renderMovieSearchResult(result.payload ?: emptyList())
            Result.Status.ERROR -> Log.d(this.javaClass.name, result.error ?: "Error")
        }
    })

    private fun renderMovieSearchResult(list: List<Movie>) = searchListRecyclerView.adapter?.let { adapter ->
        (adapter as MovieSearchResultsListAdapter).updateList(list)
    } ?: run {
        searchListRecyclerView.adapter =
            MovieSearchResultsListAdapter(list) { movieId, view -> openMovieDetails(movieId, view) }
    }
}