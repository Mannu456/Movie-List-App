package me.alfredobejarano.movieslist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import dagger.android.AndroidInjection
import me.alfredobejarano.movieslist.movielist.MovieListFragmentDirections

class NavHostActivity : AppCompatActivity() {
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_nav_host)
        navController = findNavController(R.id.navHostFragment)
    }
}
