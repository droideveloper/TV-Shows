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

package org.fs.tvshows.common.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import com.bumptech.glide.BuildConfig
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.webp.decoder.*
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.resource.bitmap.BitmapDrawableDecoder
import com.bumptech.glide.module.AppGlideModule
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.InputStream
import java.nio.ByteBuffer

@GlideModule class TVShowsGlideModule: AppGlideModule() {

  private val logger by lazy {
    val l = HttpLoggingInterceptor.Logger { msg ->
      if (BuildConfig.DEBUG) {
        Log.println(Log.INFO, TVShowsGlideModule::class.java.simpleName, msg)
      }
    }

    HttpLoggingInterceptor(l).apply {
      level = HttpLoggingInterceptor.Level.HEADERS
    }
  }

  private val factory by lazy {
    val builder = OkHttpClient.Builder()
    // add logger if debug
    if (BuildConfig.DEBUG) {
      builder.addInterceptor(logger)
    }
    // return http
    builder.build()
  }

  override fun applyOptions(context: Context, builder: GlideBuilder) {
    val calculator = MemorySizeCalculator.Builder(context)
      .setBitmapPoolScreens(3f)
      .build()


    val memoryCacheSize = Math.round(calculator.memoryCacheSize * 0.6f) * 1L
    val diskCacheSize = 1024 * 1024 * 128L // cache 128 mb in disk
    // we set up glide this way
    builder.setMemoryCache(LruResourceCache(memoryCacheSize))
    builder.setBitmapPool(LruBitmapPool(calculator.bitmapPoolSize.toLong()))
    builder.setDiskCache(DiskLruCacheFactory(Glide.getPhotoCacheDir(context)?.absolutePath, diskCacheSize))
  }

  override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
    register(context, glide, registry)
    registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(factory))
  }

  override fun isManifestParsingEnabled(): Boolean = false

  private fun register(context: Context, glide: Glide, registry: Registry) {
    // We should put our decoder before the build-in decoders,
    // because the Down sampler will consume arbitrary data and make the input stream corrupt
    // on some devices will be supporting webp
    val resources = context.resources
    val bitmapPool = glide.bitmapPool
    val arrayPool = glide.arrayPool
    /* static webp decoders */
    val webpDownSampler = WebpDownsampler(registry.imageHeaderParsers,
      resources.displayMetrics, bitmapPool, arrayPool)
    val bitmapDecoder = AnimatedWebpBitmapDecoder(arrayPool, bitmapPool)
    val byteBufferBitmapDecoder = ByteBufferBitmapWebpDecoder(webpDownSampler)
    val streamBitmapDecoder = StreamBitmapWebpDecoder(webpDownSampler, arrayPool)
    /* animate webp decoders */
    val byteBufferWebpDecoder = ByteBufferWebpDecoder(context, arrayPool, bitmapPool)
    registry
      /* Bitmaps for static webp images */
      .prepend(Registry.BUCKET_BITMAP, ByteBuffer::class.java, Bitmap::class.java,
        byteBufferBitmapDecoder)
      .prepend(Registry.BUCKET_BITMAP, InputStream::class.java, Bitmap::class.java,
        streamBitmapDecoder)
      /* BitmapDrawables for static webp images */
      .prepend(
        Registry.BUCKET_BITMAP_DRAWABLE,
        ByteBuffer::class.java,
        BitmapDrawable::class.java,
        BitmapDrawableDecoder(resources, byteBufferBitmapDecoder)
      )
      .prepend(
        Registry.BUCKET_BITMAP_DRAWABLE,
        InputStream::class.java,
        BitmapDrawable::class.java,
        BitmapDrawableDecoder(resources, streamBitmapDecoder))
      /* Bitmaps for animated webp images*/
      .prepend(Registry.BUCKET_BITMAP, ByteBuffer::class.java, Bitmap::class.java,
        ByteBufferAnimatedBitmapDecoder(bitmapDecoder))
      .prepend(Registry.BUCKET_BITMAP, InputStream::class.java, Bitmap::class.java,
        StreamAnimatedBitmapDecoder(bitmapDecoder))
      /* Animated webp images */
      .prepend(ByteBuffer::class.java, WebpDrawable::class.java, byteBufferWebpDecoder)
      .prepend(InputStream::class.java, WebpDrawable::class.java,
        StreamWebpDecoder(byteBufferWebpDecoder, arrayPool))
      .prepend(WebpDrawable::class.java, WebpDrawableEncoder())
  }
}