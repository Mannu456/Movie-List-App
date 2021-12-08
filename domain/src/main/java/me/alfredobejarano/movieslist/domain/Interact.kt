package me.alfredobejarano.movieslist.domain

import me.alfredobejarano.movieslist.core.Result

/**
 * Top-level function that helps catching HTTP or Local Exceptions
 * from executing calls from the repository.
 *
 * Created by alfredo on 2019-08-02.
 */
internal suspend fun <T> interact(suspendWork: suspend () -> T): Result<out T> = try {
    val payload = suspendWork()
    Result.success(payload)
} catch (e: Exception) {
    Result.error(e.localizedMessage)
}