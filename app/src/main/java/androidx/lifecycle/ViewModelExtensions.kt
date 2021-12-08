package androidx.lifecycle

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import me.alfredobejarano.movieslist.BuildConfig
import me.alfredobejarano.movieslist.core.Result
import java.io.Closeable
import kotlin.coroutines.CoroutineContext

private const val IO_JOB_KEY = "${BuildConfig.APPLICATION_ID}.IO_JOB_KEY"

/**
 *
 * [CoroutineScope] tied to this [ViewModel].
 * This scope will be canceled when ViewModel will be cleared, i.e [ViewModel.onCleared] is called
 *
 * This scope is bound to [Dispatchers.IO]
 *
 * Created by alfredo corona on 2019-08-02.
 */
val ViewModel.ioViewModelScope: CoroutineScope
    get() {
        val scope: CoroutineScope? = this.getTag(IO_JOB_KEY)
        return scope?.let { safeScope ->
            safeScope
        } ?: run {
            setTagIfAbsent(IO_JOB_KEY, CloseableCoroutineScope(SupervisorJob() + Dispatchers.IO))
        }
    }

/**
 * Returns a [LiveData] that reports the execution of a ViewModel closable coroutine.
 * The given block of code is executed in [Dispatchers.IO]
 */
fun <T> ViewModel.launchInIOForLiveData(block: suspend () -> Result<out T>): LiveData<Result<out T>> =
    MediatorLiveData<Result<out T>>().apply {
        value = Result.loading()
        ioViewModelScope.launch {
            postValue(block())
        }
    }

/**
 * Returns a [LiveData] that reports the execution of a ViewModel closable coroutine.
 * The given block of code is executed in [Dispatchers.Main]
 */
fun <T> ViewModel.launchInMainForLiveData(block: suspend () -> Result<out T>): LiveData<Result<out T>> =
    MediatorLiveData<Result<out T>>().apply {
        value = Result.loading()
        viewModelScope.launch {
            value = block()
        }
    }

internal class CloseableCoroutineScope(context: CoroutineContext) : Closeable, CoroutineScope {
    override fun close() = coroutineContext.cancel()
    override val coroutineContext: CoroutineContext = context
}