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

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.util.SparseArray
import org.fs.tvshows.R

class ImageRatioView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, style: Int = 0): AppCompatImageView(context, attrs, style) {

  companion object {
    private const val RATIO_2_3  = 0.6666f
    private const val RATIO_3_2  = 1.5f
    private const val RATIO_4_3  = 1.3333f
    private const val RATIO_16_9 = 1.7777f
  }

  private val ratios by lazy { SparseArray<Float>().apply {
      put(0, RATIO_2_3)
      put(1, RATIO_3_2)
      put(2, RATIO_4_3)
      put(3, RATIO_16_9)
    }
  }

  private val ratio: Int

  init {
    val a = context.obtainStyledAttributes(attrs, R.styleable.ImageRatioView, 0, 0)
    ratio = a.getInt(R.styleable.ImageRatioView_image_ratio, -1)
    a.recycle()
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    if (ratio != -1) {
      val width = MeasureSpec.getSize(widthMeasureSpec)
      val f = ratios[ratio]
      if (f != null) {
        val newHeight = Math.round(width / f)
        val newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(newHeight, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec)
      } else {
        // fallback
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
      }
    } else {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
  }
}