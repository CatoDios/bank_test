package com.example.banktest.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import com.example.domain.IOTaskResult
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import retrofit2.Response
import java.io.IOException


/**
 * Readable naming convention for Network call lambda
 * @since 1.0
 */
typealias NetworkAPIInvoke<T> = suspend () -> Response<T>

/**
 * typealias for lambda passed when a photo is tapped on in Popular Photos Fragment
 */
typealias ListItemClickListener<T> = (T) -> Unit



/**
 * Utility function that works to perform a Retrofit API call and return either a success model
 * instance or an error message wrapped in an [Exception] class
 * @param messageInCaseOfError Custom error message to wrap around [IOTaskResult.OnFailed]
 * with a default value provided for flexibility
 * @param networkApiCall lambda representing a suspend function for the Retrofit API call
 * @return [IOTaskResult.OnSuccess] object of type [T], where [T] is the success object wrapped around
 * [IOTaskResult.OnSuccess] if network call is executed successfully, or [IOTaskResult.OnFailed]
 * object wrapping an [Exception] class stating the error
 * @since 1.0
 */

suspend fun <T : Any> performSafeNetworkApiCall(
    messageInCaseOfError: String = "Network error",
    allowRetries: Boolean = true,
    numberOfRetries: Int = 2,
    networkApiCall: NetworkAPIInvoke<T>
): Flow<IOTaskResult<T>> {
    var delayDuration = 1000L
    val delayFactor = 2

    return flow {
        val response = networkApiCall()
        if (response.isSuccessful) {
            response.body()?.let {
                emit(IOTaskResult.OnSuccess(it))
            }
                ?: emit(IOTaskResult.OnFailed(IOException("API call successful but empty response body")))
            return@flow
        }
        else {
            emit(
                IOTaskResult.OnFailed(
                    IOException(
                        "API call failed with error ${response.code()} - ${response.errorBody()
                            ?.string() ?: messageInCaseOfError}"
                    )
                )
            )
        }
        return@flow
    }.catch { e ->
        emit(IOTaskResult.OnFailed(IOException("Exception during network API call: ${e.message}")))
        Log.e("AEA", "Exception during network API call: ${e.message}" )
        return@catch
    }.retryWhen { cause, attempt ->
        if (!allowRetries || attempt > numberOfRetries || cause !is IOException) return@retryWhen false
        delay(delayDuration)
        delayDuration *= delayFactor
        return@retryWhen true
    }.flowOn(Dispatchers.IO)
}




/**
 * [ImageView] extension function adds the capability to loading image by directly specifying
 * the url
 * @param url Image URL
 */
fun ImageView.loadUrl(
        @NonNull url: String
) {
    Picasso.get()
        .load(url)
        .into(this)
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

/**
 * Lets the UI act on a controlled bound of states that can be defined here
 * @author Prasan
 * @since 1.0
 */
sealed class ViewState<out T : Any> {

    /**
     * Represents UI state where the UI should be showing a loading UX to the user
     * @param isLoading will be true when the loading UX needs to display, false when not
     */
    data class Loading(val isLoading: Boolean) : ViewState<Nothing>()

    /**
     * Represents the UI state where the operation requested by the UI has been completed successfully
     * and the output of type [T] as asked by the UI has been provided to it
     * @param output result object of [T] type representing the fruit of the successful operation
     */
    data class RenderSuccess<out T : Any>(val output: T) : ViewState<T>()

    /**
     * Represents the UI state where the operation requested by the UI has failed to complete
     * either due to a IO issue or a service exception and the same is conveyed back to the UI
     * to be shown the user
     * @param throwable [Throwable] instance containing the root cause of the failure in a [String]
     */
    data class RenderFailure(val throwable: Throwable) : ViewState<Nothing>()
}

/**
 * Extension function on a fragment to show a toast message
 */
fun Context.showToast(@NonNull message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}




fun Context.newFacebookIntent( url: String) {
    var uri = Uri.parse(url)
    val packageManager = this.packageManager
    try {
        val applicationInfo = packageManager.getApplicationInfo("com.facebook.katana", 0)
        if (applicationInfo.enabled) {
            // http://stackoverflow.com/a/24547437/1048340
            uri = Uri.parse("fb://facewebmodal/f?href=$url")
        }
    } catch (ignored: PackageManager.NameNotFoundException) {
    }
    this.startActivity( Intent(Intent.ACTION_VIEW, uri))
}

 fun Context.openFacebookPage(pageId: String) {
    val pageUrl = "https://www.facebook.com/$pageId"
    try {
        val applicationInfo: ApplicationInfo = packageManager.getApplicationInfo("com.facebook.katana", 0)
        if (applicationInfo.enabled) {
            val versionCode: Int = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode
            val url: String
            url = if (versionCode >= 3002850) {
                "fb://facewebmodal/f?href=$pageUrl"
            } else {
                "fb://page/$pageId"
            }
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } else {
            throw Exception("Facebook is disabled")
        }
    } catch (e: Exception) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(pageUrl)))
    }
}


fun Context.newInstagramActivity(){
    val uri = Uri.parse("http://instagram.com/_u/oximap2020")
    val likeIng = Intent(Intent.ACTION_VIEW, uri)

    likeIng.setPackage("com.instagram.android")

    try {
        this.startActivity(likeIng)
    } catch (e: ActivityNotFoundException) {
        this.startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse("http://instagram.com/oximap2020")))
    }
}

fun Context.newLinkedinActivity(){
    val uri = Uri.parse("https://www.linkedin.com/company/oximap/")
    val likeIng = Intent(Intent.ACTION_VIEW, uri)

    likeIng.setPackage("com.linkedin.android")

    try {
        this.startActivity(likeIng)
    } catch (e: ActivityNotFoundException) {
        this.startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.linkedin.com/company/oximap/")))
    }
}



fun Context.startNewActivity(packageName: String) {
    var intent = this.packageManager.getLaunchIntentForPackage(packageName)
    if (intent != null) {
        // We found the activity now start the activity
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        this.startActivity(intent)
    } else {
        // Bring user to the market or let them choose an app?
        intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.data = Uri.parse("market://details?id=$packageName")
        this.startActivity(intent)
    }
}

fun Activity.startNewActivityNewTask(cls : Class<*> ){
    val intent = Intent(this, cls)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
    this.finish()

}

fun View.hide(){
    this.visibility=View.GONE
}
fun View.show(){
    this.visibility=View.VISIBLE
}



/**
 * Util method that takes a suspend function returning a [Flow] of [IOTaskResult] as input param and returns a
 * [Flow] of [ViewState], which emits [ViewState.Loading] with true prior to performing the IO Task. If the
 * IO operation results a [IOTaskResult.OnSuccess], the result is mapped to a [ViewState.RenderSuccess] instance and emitted,
 * else a [IOTaskResult.OnFailed] is mapped to a [ViewState.RenderFailure] instance and emitted.
 * The flowable is then completed by emitting a [ViewState.Loading] with false
 */

suspend fun <T : Any> getViewStateFlowForNetworkCall(ioOperation: suspend () -> Flow<IOTaskResult<T>>) =
        flow {
            emit(ViewState.Loading(true))
            ioOperation().map {
                when (it) {
                    is IOTaskResult.OnSuccess -> ViewState.RenderSuccess(it.data)
                    is IOTaskResult.OnFailed -> ViewState.RenderFailure(it.throwable)
                }
            }.collect {
                emit(it)
            }
            emit(ViewState.Loading(false))
        }.flowOn(Dispatchers.IO)

/**
 * Returns the `location` object as a human readable string.
 */
fun Location?.toText():String {
    return if (this != null) {
        "($latitude, $longitude)"
    } else {
        "Unknown location"
    }
}