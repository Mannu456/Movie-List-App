package me.alfredobejarano.movieslist.remote.map

/**
 * Interface that defines a :remote module model that can transform into a :core module model.
 *
 * :core module models are used through all the app, for caching and UI.
 * Following the Google's Guide to architecture, The UI should be model driven.
 *
 * Created by alfredo on 2019-08-02.
 */
interface TransformableModel<E> {
    /**
     * Defines how the implementation of this interface
     * will transform into a :core module model / entity.
     */
    fun transform(): E
}