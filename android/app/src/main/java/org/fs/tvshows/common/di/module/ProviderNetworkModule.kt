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

package org.fs.tvshows.common.di.module

import android.content.Context
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.fs.tvshows.BuildConfig
import org.fs.tvshows.common.adapter.DateAdapter
import org.fs.tvshows.model.entity.*
import org.fs.tvshows.net.Endpoint
import org.fs.tvshows.util.log
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.lang.IllegalArgumentException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ProviderNetworkModule {

  companion object {
    private const val CACHE_DIR = "caches"
    private const val CACHE_SIZE = 1024 * 1024 * 12L // 12 MB
    private const val DEFAULT_TIMEOUT = 20L
  }

  @Singleton @Provides fun provideHttpUrl(): HttpUrl {
    val httpUrl = HttpUrl.parse(BuildConfig.BASE_URL)
    if (httpUrl != null) {
      return httpUrl
    }
    throw IllegalArgumentException("can not parse url ${BuildConfig.BASE_URL}")
  }


  @Singleton @Provides fun provideCallFactory(context: Context, auth: Interceptor): OkHttpClient {
    // this is logger used only in debug build
    val logger = HttpLoggingInterceptor.Logger { msg -> log(msg) }
    val logInterceptor = HttpLoggingInterceptor(logger)
    logInterceptor.level = HttpLoggingInterceptor.Level.BODY

    // cache will be here now
    val file = File(context.cacheDir, CACHE_DIR)
    val cache = Cache(file, CACHE_SIZE)

    val builder = OkHttpClient.Builder()
      .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
      .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
      .cache(cache)
      .addInterceptor(auth)

    if (BuildConfig.DEBUG) {
      builder.addInterceptor(logInterceptor)
    }
    return builder.build()
  }

  @Singleton @Provides fun provideRetrofit(baseUrl: HttpUrl, client: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
    .baseUrl(baseUrl)
    .client(client)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .build()

  @Singleton @Provides fun provideMoshi(): Moshi = Moshi.Builder()
    .add(DateAdapter())
    .build()

  @Singleton @Provides fun provideEndpoint(retrofit: Retrofit): Endpoint = retrofit.create(Endpoint::class.java)
}