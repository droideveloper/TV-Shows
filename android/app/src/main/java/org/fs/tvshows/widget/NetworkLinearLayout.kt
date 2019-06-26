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
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import org.fs.tvshows.R
import org.fs.tvshows.common.glide.GlideApp
import org.fs.tvshows.model.entity.NetworkEntity
import org.fs.tvshows.util.toLogoUri

class NetworkLinearLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, style: Int = 0): LinearLayout(context, attrs, style) {

  private val glide by lazy { GlideApp.with(context) }
  private val margin by lazy { resources.getDimensionPixelSize(R.dimen.dp8) }

  private val lp by lazy { LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) }
  private val lpMargin by lazy { LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
      marginStart = margin
    }
  }

  init {
    orientation = HORIZONTAL
    isHorizontalScrollBarEnabled = true
    scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
  }

  fun bindNetworks(networks: List<NetworkEntity>) {
    removeAllViews()
    val factory = LayoutInflater.from(context)
    networks.forEachIndexed { index, network ->
      val view = factory.inflate(R.layout.view_network_item, this, false)
      val params = if (index == 0) lp else lpMargin
      addView(view, params)
      bindNetwork(network, view)
    }
  }

  private fun bindNetwork(network: NetworkEntity, view: View) {
    val viewImage = view.findViewById<ImageView>(R.id.viewImageNetwork)
    val viewText = view.findViewById<TextView>(R.id.viewTextNetwork)

    val uri = network.toLogoUri(context)

    glide.clear(viewImage)
    glide.load(uri)
      .applyCenterInside()
      .into(viewImage)

    val text = "${network.name}, ${network.originCountry}"
    viewText.text = text
  }
}