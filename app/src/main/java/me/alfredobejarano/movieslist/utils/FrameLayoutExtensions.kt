package me.alfredobejarano.movieslist.utils

import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * Created by alfredo on 2019-08-02.
 */

fun FrameLayout.replaceFragment(fragmentManager: FragmentManager, fragment: Fragment): Boolean {
    fragmentManager.beginTransaction()
        .replace(id, fragment)
        .disallowAddToBackStack()
        .commitAllowingStateLoss()
    return true
}