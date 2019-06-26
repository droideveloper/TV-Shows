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

package org.fs.tvshows.widget

import android.graphics.Color
import android.support.design.widget.BaseTransientBottomBar
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import org.fs.architecture.mvi.util.inflate
import org.fs.tvshows.R

class ErrorSnackbar private constructor(parent: ViewGroup, content: View, callback: ContentViewCallback): BaseTransientBottomBar<ErrorSnackbar>(parent, content, callback) {

  companion object {

    @JvmStatic fun make(parent: ViewGroup, duration: Int): ErrorSnackbar {
      val view = parent.inflate(R.layout.view_snackbar_layout)
      val callback = ContentViewCallback(view)
      return ErrorSnackbar(parent, view, callback).also {
        view.setPadding(0, 0, 0, 0)
        view.setBackgroundColor(Color.parseColor("#888888"))
      }
    }
  }

  fun setMessage(text: CharSequence?): ErrorSnackbar {
    val viewTextMessage = view.findViewById<TextView>(R.id.viewTextMessage)
    viewTextMessage.text = text
    return this
  }

  fun setCallback(callback: View.OnClickListener): ErrorSnackbar {
    val viewButtonCancel = view.findViewById<Button>(R.id.viewButtonCancel)
    viewButtonCancel.setOnClickListener { v ->
      callback.onClick(v).also {
        dismiss()
      }
    }
    return this
  }

  internal class ContentViewCallback(private val view: View): android.support.design.snackbar.ContentViewCallback {

    override fun animateContentOut(delay: Int, duration: Int)= animate(0f, 1f, delay.toLong(), duration.toLong())

    override fun animateContentIn(delay: Int, duration: Int) = animate(1f, 0f, delay.toLong(), duration.toLong())

    private fun animate(startY: Float, endY: Float, delay: Long, duration: Long) {
      view.scaleY = startY
      view.animate()
        .scaleY(endY)
        .setDuration(duration)
        .setStartDelay(delay)
        .start()
    }
  }
}