package me.alfredobejarano.movieslist

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import me.alfredobejarano.movieslist.movielist.MovieListAdapter.MovieViewHolder
import me.alfredobejarano.movieslist.search.MovieSearchResultsListAdapter.MovieSearchResultViewHolder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MovieListFragmentTest {
    @Rule
    @JvmField
    val activityTestRule: ActivityTestRule<NavHostActivity> = ActivityTestRule(NavHostActivity::class.java)

    @Test
    fun openApp_searchForMovie_selectMovieResult() {
        val searchBarViewMatcher = onView(withId(R.id.searchBar))
        searchBarViewMatcher.perform(typeText("spider"))

        val searchListMatcher = onView(withId(R.id.searchResultsFrameLayout))
        searchListMatcher.check(matches(isDisplayed()))

        Thread.sleep(2500) // Wait for the search HTTP request to finish.

        val searchResultsListMatcher = onView(withId(R.id.searchResultsList))
        searchResultsListMatcher.perform(actionOnItemAtPosition<MovieSearchResultViewHolder>(3, click()))

        onView(withId(R.id.moviePosterSimpleDraweeView)).check(matches(isDisplayed()))
    }

    @Test
    fun openApp_swipeThroughMovieLists_openMovieDetails() {
        val viewMatcher = onView(withId(R.id.upcomingMovieList))
        repeat(3) {
            viewMatcher.perform(swipeRight())
        }
        repeat(2) {
            viewMatcher.perform(swipeLeft())
        }
        viewMatcher
            .perform(actionOnItemAtPosition<MovieViewHolder>(4, click()))

        onView(withId(R.id.moviePosterSimpleDraweeView)).check(matches(isDisplayed()))
    }

    @Test
    fun openApp_selectMovie_openMovieDetails_clickVideo() {
        val viewMatcher = onView(withId(R.id.popularMovieList))
        viewMatcher
            .perform(actionOnItemAtPosition<MovieViewHolder>(0, click()))

        onView(withId(R.id.movieVideoFrameLayout))
            .check(matches(isDisplayed()))
            .perform(click())
    }
}