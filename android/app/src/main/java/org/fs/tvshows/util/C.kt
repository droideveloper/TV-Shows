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

sealed class C {
  companion object {
    const val RECYCLER_CACHE_SIZE = 10

    const val BUNDLE_ARGS_TV_SHOW = "bundle.args.tv.show"

    const val SHARED_ELEMENT_POSTER = "shared.element.poster"
    const val SHARED_ELEMENT_TITLE = "shared.element.title"
    const val SHARED_ELEMENT_DURATION = "shared.element.duration"
    const val SHARED_ELEMENT_RATE = "shared.element.rate"
  }
}