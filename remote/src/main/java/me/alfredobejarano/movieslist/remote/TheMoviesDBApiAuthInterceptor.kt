package me.alfredobejarano.movieslist.remote

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.Locale

/**
 * Class that will add the common query params such as authorization token or language
 * to all the TheMoviesDB API endpoints requests.
 *
 * Created by alfredo on 2019-08-02.
 */
class TheMoviesDBApiAuthInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(request().newBuilder().apply {
            url(addQueryParamsToURL(request().url.newBuilder()))
        }.build())
    }

    private fun addQueryParamsToURL(urlBuilder: HttpUrl.Builder) = urlBuilder.apply {
        val deviceRegion = getLanguageAndRegion()
        addQueryParameter("api_key", apiKey)
        addQueryParameter("language", deviceRegion.first)
        addQueryParameter("region", deviceRegion.second)
    }.build()

    /**
     * Retrieves the language and country (region) from the current device
     * locale in ISO-639-1 format.
     *
     * @return [Pair] object containing the language in the first position
     * and the country in the second position. Ex: es, MX
     */
    private fun getLanguageAndRegion() = Locale.getDefault().run {
        Pair(language ?: "es", country.toUpperCase(this))
    }
}