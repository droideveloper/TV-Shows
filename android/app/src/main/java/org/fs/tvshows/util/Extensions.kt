/*
 *  Copyright (C) 2019 Fatih, Tv Shows Android Kotlin.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.fs.tvshows.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import org.fs.rx.extensions.util.EMPTY
import org.fs.tvshows.BuildConfig
import org.fs.tvshows.R
import org.fs.tvshows.model.entity.*
import java.io.PrintWriter
import java.io.StringWriter

inline fun <reified T> T.isLogEnabled(): Boolean = BuildConfig.DEBUG
inline fun <reified T> T.getClassTag(): String = T::class.java.simpleName

inline fun <reified T> T.log(error: Throwable) {
  val sw = StringWriter()
  val pw = PrintWriter(sw)
  error.printStackTrace(pw)
  log(Log.ERROR, sw.toString())
}
inline fun <reified T> T.log(message: String) = log(Log.DEBUG, message)

inline fun <reified T> T.log(logLevel: Int, message: String) {
  if (isLogEnabled()) {
    Log.println(logLevel, getClassTag(), message)
  }
}

fun SwipeRefreshLayout.bind(showProgress: Boolean) {
  isRefreshing = showProgress
}

// glide options base
fun RequestOptions.applyBase(): RequestOptions = placeholder(R.drawable.ic_placeholder)
  .error(R.drawable.ic_error_placeholder)
  .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
  .dontAnimate()

// recycler extensions
fun Drawable.recyclerDivider(viewRecycler: RecyclerView, gravity: Int = DividerItemDecoration.VERTICAL) {
  val divider = DividerItemDecoration(viewRecycler.context, gravity)
  divider.setDrawable(this)
  viewRecycler.addItemDecoration(divider)
}

fun toUri(baseUrl: String = BuildConfig.BASE_IMAGE_URL, context: Context, path: String): Uri {
  val displayMetrics = context.resources.displayMetrics
  val density = "w${displayMetrics.densityDpi}"
  return Uri.parse("$baseUrl$density$path") ?: Uri.EMPTY
}

fun TVShowEntity.toPosterUri(context: Context): Uri = toUri(context = context, path = posterPath ?: String.EMPTY)
fun TVShowEntity.toBackdropUri(context: Context): Uri = toUri(context = context, path = backdropPath ?: String.EMPTY)

fun TVShowDetailEntity.toPosterUri(context: Context): Uri = toUri(context = context, path = posterPath ?: String.EMPTY)
fun TVShowDetailEntity.toBackdropUri(context: Context): Uri = toUri(context = context, path = backdropPath ?: String.EMPTY)

fun EpisodeEntity.toStillUri(context: Context): Uri = toUri(context = context, path = stillPath ?: String.EMPTY)

fun SeasonEntity.toPosterUri(context: Context): Uri = toUri(context = context, path = posterPath ?: String.EMPTY)

fun ProductionCompany.toLogoUri(context: Context): Uri = toUri(context = context, path = logoPath ?: String.EMPTY)

fun CreatorEntity.toProfileUri(context: Context): Uri = toUri(context = context, path = profilePath ?: String.EMPTY)