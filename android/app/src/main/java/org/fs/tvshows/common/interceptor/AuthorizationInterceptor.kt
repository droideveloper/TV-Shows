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

package org.fs.tvshows.common.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import org.fs.tvshows.BuildConfig
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthorizationInterceptor @Inject constructor(): Interceptor {

  companion object {
    private const val QUERY_API_KEY = "api_key"
    private const val QUERY_LANGUAGE = "language"
  }

  private val locale by lazy { Locale.getDefault() }
  private val defaults by lazy { "${locale.language}-${locale.country}" }

  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
    val url = request.url()

    val newUrl = url.newBuilder()
      .addQueryParameter(QUERY_API_KEY, BuildConfig.API_KEY)
      .addQueryParameter(QUERY_LANGUAGE, defaults)
      .build()

    val newRequest = request.newBuilder()
      .url(newUrl)
      .build()

    return chain.proceed(newRequest)
  }
}