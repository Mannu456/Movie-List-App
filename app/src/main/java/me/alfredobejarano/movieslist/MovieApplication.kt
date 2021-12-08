package me.alfredobejarano.movieslist

import android.app.Activity
import android.app.Application
import android.app.Service
import androidx.fragment.app.Fragment
import com.facebook.drawee.backends.pipeline.Fresco
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import dagger.android.support.HasSupportFragmentInjector
import me.alfredobejarano.movieslist.di.Injector
import javax.inject.Inject

class MovieApplication : Application(), HasActivityInjector, HasSupportFragmentInjector, HasServiceInjector {
    @Inject
    lateinit var serviceInjector: DispatchingAndroidInjector<Service>
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> = activityDispatchingAndroidInjector
    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector
    override fun serviceInjector(): AndroidInjector<Service> = serviceInjector

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
        Injector.init(this)
        Injector.component.inject(this)
    }
}