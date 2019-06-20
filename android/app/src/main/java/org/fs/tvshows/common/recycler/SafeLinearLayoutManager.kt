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

package org.fs.tvshows.common.recycler

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import org.fs.tvshows.BuildConfig

class SafeLinearLayoutManager(context: Context, orientation: Int, reverseLayout: Boolean): LinearLayoutManager(context, orientation, reverseLayout) {

  override fun supportsPredictiveItemAnimations(): Boolean = false // those animations cause bug on some devices

  override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
    try {
      super.onLayoutChildren(recycler, state)
    } catch (error: Throwable) {
      if (BuildConfig.DEBUG) {
        error.printStackTrace()
      }
    }
  }
}