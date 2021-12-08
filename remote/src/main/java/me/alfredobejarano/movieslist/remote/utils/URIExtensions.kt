package me.alfredobejarano.movieslist.remote.utils

import java.net.URI

fun URI.lastPathSegment() = path.run {
    substring(lastIndexOf("/") + 1)
}