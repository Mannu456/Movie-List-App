package me.alfredobejarano.movieslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel class that helps fragments communicate between one each other without interfaces.
 */
class NavHostViewModel : ViewModel() {
    private val searchQueryMediatorLiveData = MediatorLiveData<String>()
    val searchQueryLiveData = searchQueryMediatorLiveData as LiveData<String>

    fun reportQueryChange(query: String) {
        searchQueryMediatorLiveData.value = query
    }
}