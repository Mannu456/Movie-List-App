package me.alfredobejarano.movieslist.search

import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import me.alfredobejarano.movieslist.core.Movie
import me.alfredobejarano.movieslist.databinding.ItemMovieSearchResultBinding
import me.alfredobejarano.movieslist.utils.layoutInflater

/**
 * [RecyclerView.Adapter] class that gives instructions to a [RecyclerView] of how
 * to render a list of [Movie] elements.
 *
 * It uses [DiffUtil] to maintain the code complexity as lower as possible.
 */
internal class MovieSearchResultsListAdapter(
    private var movies: List<Movie>,
    private val onMovieSelected: (Int, View) -> Unit = { movieId, view -> }
) :
    RecyclerView.Adapter<MovieSearchResultsListAdapter.MovieSearchResultViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MovieSearchResultViewHolder(
            ItemMovieSearchResultBinding.inflate(parent.layoutInflater, parent, false)
        )

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: MovieSearchResultViewHolder, position: Int) = holder.binding.run {
        movie = movies[position]
        executePendingBindings()
        root.setOnClickListener { onMovieSelected(movie?.id ?: 0, holder.binding.moviePosterSimpleDraweeView) }
        root.startAnimation(AnimationUtils.loadAnimation(root.context, android.R.anim.fade_in))
    }

    fun updateList(newMovieList: List<Movie>) {
        val callback =
            MovieCallback(
                movies,
                newMovieList
            )
        val diff = DiffUtil.calculateDiff(callback)
        diff.dispatchUpdatesTo(this)
        movies = newMovieList
    }

    /**
     * Simple [RecyclerView.ViewHolder] class that represents a [Movie] in a [RecyclerView].
     */
    class MovieSearchResultViewHolder(internal val binding: ItemMovieSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * [DiffUtil] callback that calculates the differences between two lists of
     * Movies and just updates the updated elements instead of the whole list.
     */
    internal class MovieCallback(
        private val oList: List<Movie>,
        private val nList: List<Movie>
    ) : DiffUtil.Callback() {
        override fun getNewListSize() = nList.size
        override fun getOldListSize() = oList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oList[oldItemPosition].id == nList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            areItemsTheSame(oldItemPosition, newItemPosition)
    }
}