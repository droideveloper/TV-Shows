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
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class OverlayView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, style: Int = 0): View(context, attrs, style) {

  private val paint by lazy { Paint().apply {
      color = Color.WHITE
      setStyle(Paint.Style.FILL)
    }
  }

  private val path by lazy { Path().apply {
      moveTo(0f, height * 0.6f)
      lineTo(width * 1f, height * 0.95f)
      lineTo(width * 1f, height * 1f)
      lineTo(0f, height * 1f)
      lineTo(0f, height * 0.6f)
      close()
    }
  }

  override fun onDraw(canvas: Canvas?) = canvas?.drawPath(path, paint) ?: Unit
}