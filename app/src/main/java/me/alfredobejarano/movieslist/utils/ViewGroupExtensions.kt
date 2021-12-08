package me.alfredobejarano.movieslist.utils

import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Created by alfredo on 2019-08-02.
 */

val ViewGroup.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(context)