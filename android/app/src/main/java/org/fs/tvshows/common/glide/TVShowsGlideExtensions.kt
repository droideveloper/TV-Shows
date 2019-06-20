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

import com.bumptech.glide.annotation.GlideExtension
import com.bumptech.glide.annotation.GlideOption
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import org.fs.tvshows.util.applyBase

@GlideExtension
sealed class TVShowsGlideExtensions {

  companion object {

    @JvmStatic @GlideOption fun applyCenterInside(options: RequestOptions): RequestOptions {
      return options.centerInside()
        .applyBase()
    }

    @JvmStatic @GlideOption fun applyCircularCrop(options: RequestOptions): RequestOptions {
      return options.circleCrop()
        .applyBase()
    }

    @JvmStatic @GlideOption fun applyRoundedRectCrop(options: RequestOptions, r: Int = 12): RequestOptions {
      return options.transforms(CenterCrop(), RoundedCorners(r))
        .applyBase()
    }

    @JvmStatic @GlideOption fun applyCrop(options: RequestOptions): RequestOptions {
      return options.centerCrop()
        .applyBase()
    }
  }
}