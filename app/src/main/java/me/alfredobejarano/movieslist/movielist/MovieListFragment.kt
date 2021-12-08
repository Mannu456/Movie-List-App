package me.alfredobejarano.movieslist.movielist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.FrameLayout
import androidx.annotation.AnimRes
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.AndroidSupportInjection
import me.alfredobejarano.movieslist.NavHostViewModel
import me.alfredobejarano.movieslist.R
import me.alfredobejarano.movieslist.core.Movie
import me.alfredobejarano.movieslist.core.MovieListType
import me.alfredobejarano.movieslist.core.Result
import me.alfredobejarano.movieslist.databinding.FragmentMovieListBinding
import me.alfredobejarano.movieslist.di.ViewModelFactory
import me.alfredobejarano.movieslist.search.MovieSearchFragment
import me.alfredobejarano.movieslist.utils.hideSoftKeyboard
import me.alfredobejarano.movieslist.utils.openMovieDetails
import javax.inject.Inject

/**
 * Fragment class that displays lists of movies separated by categories.
 *
 * Created by alfredo on 2019-08-02.
 */
class MovieListFragment : Fragment() {
    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var viewModel: MovieListViewModel
    private lateinit var navHostViewModel: NavHostViewModel

    private lateinit var searchResultFrameLayout: FrameLayout

    private lateinit var popularRecyclerView: RecyclerView
    private lateinit var topRatedRecyclerView: RecyclerView
    private lateinit var upcomingRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedState: Bundle?
    ) = FragmentMovieListBinding.inflate(inflater, container, false).apply {
        AndroidSupportInjection.inject(this@MovieListFragment)

        popularRecyclerView = setupRecyclerView(popularMovieList)
        upcomingRecyclerView = setupRecyclerView(upcomingMovieList)
        topRatedRecyclerView = setupRecyclerView(topRatedMovieList)

        searchResultFrameLayout = searchResultsFrameLayout
        navHostViewModel = ViewModelProviders.of(requireActivity())[NavHostViewModel::class.java]
        viewModel = ViewModelProviders.of(this@MovieListFragment, factory)[MovieListViewModel::class.java]

        animateView(searchBar, R.anim.slide_in_up) {
            setupSearchEditText(searchBar)
        }
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchMovieList(MovieListType.MOVIE_LIST_POPULAR)
        fetchMovieList(MovieListType.MOVIE_LIST_UPCOMING)
        fetchMovieList(MovieListType.MOVIE_LIST_TOP_RATED)
    }

    private fun setupSearchEditText(editText: EditText) = editText.run {
        addTextChangedListener { query ->
            if (query?.length ?: 0 > 0) {
                displaySearchResultFragment(query?.toString() ?: "")
            } else {
                hideSearchResultFragment()
            }
        }
        setOnEditorActionListener { _, actionId, _ ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideSoftKeyboard()
                handled = true
            }
            handled
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) = recyclerView.apply {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun fetchMovieList(listType: MovieListType) =
        viewModel.getMovieList(listType).observe(this, Observer { result ->
            when (result.status) {
                Result.Status.LOADING -> Log.d(this.javaClass.name, "Loading")
                Result.Status.OK -> renderMovieList(result.payload ?: emptyList(), listType)
                Result.Status.ERROR -> Log.d(this.javaClass.name, result.error ?: "Error")
            }
        })

    private fun movieListRecyclerViewFactory(type: MovieListType) = when (type) {
        MovieListType.MOVIE_LIST_POPULAR -> popularRecyclerView
        MovieListType.MOVIE_LIST_UPCOMING -> upcomingRecyclerView
        MovieListType.MOVIE_LIST_TOP_RATED -> topRatedRecyclerView
    }

    private fun renderMovieList(list: List<Movie>, type: MovieListType) {
        if (list.isEmpty()) {
            Log.d(this.javaClass.name, "Empty List")
        } else {
            movieListRecyclerViewFactory(type).run {
                adapter?.let { adapter ->
                    (adapter as? MovieListAdapter)?.updateList(list)
                } ?: run {
                    adapter = MovieListAdapter(list) { movieId, view -> openMovieDetails(movieId, view) }
                }
                animateView(
                    this, if (type == MovieListType.MOVIE_LIST_POPULAR) {
                        R.anim.slide_in_right
                    } else {
                        R.anim.slide_in_left
                    }
                )
            }
        }
    }

    private fun animateView(view: View, @AnimRes animationRes: Int, onAnimationEnd: () -> Unit = {}) {
        val animation = AnimationUtils.loadAnimation(requireContext(), animationRes)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) = Unit
            override fun onAnimationEnd(p0: Animation?) = onAnimationEnd()
            override fun onAnimationStart(p0: Animation?) {
                View.VISIBLE
            }
        })
        view.startAnimation(animation)
    }

    private fun displaySearchResultFragment(query: String) {
        searchResultFrameLayout.visibility = View.VISIBLE
        navHostViewModel.reportQueryChange(query)
        requireFragmentManager().beginTransaction()
            .replace(R.id.searchResultsFrameLayout, MovieSearchFragment(), MovieSearchFragment.FRAGMENT_TAG)
            .commit()
    }

    private fun hideSearchResultFragment() =
        requireFragmentManager().findFragmentByTag(MovieSearchFragment.FRAGMENT_TAG)?.run {
            requireFragmentManager().beginTransaction().remove(this).commit()
            searchResultFrameLayout.visibility = View.GONE
            hideSoftKeyboard()
        }
}