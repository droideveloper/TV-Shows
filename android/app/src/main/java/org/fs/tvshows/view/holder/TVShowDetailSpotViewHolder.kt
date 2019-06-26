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

package org.fs.tvshows.view.holder

import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.view_tv_show_detail_spot_item.view.*
import org.fs.architecture.mvi.util.inflate
import org.fs.tvshows.R
import org.fs.tvshows.model.entity.TVShowDetailEntity

class TVShowDetailSpotViewHolder(view: View): BaseTVShowDetailViewHolder(view) {

  private val viewTextTitle by lazy { itemView.viewTextTitle }
  private val viewTextGenre by lazy { itemView.viewTextGenre }
  private val viewTextRate by lazy { itemView.viewTextRating }
  private val viewRateBar by lazy { itemView.viewRateBar }
  private val viewTextDuration by lazy { itemView.viewTextDuration }
  private val viewTextSpot by lazy { itemView.viewTextSpot }

  private val context by lazy { itemView.context }

  constructor(parent: ViewGroup): this(parent.inflate(R.layout.view_tv_show_detail_spot_item))

  override fun bind(value: TVShowDetailEntity) {
    viewTextTitle.text = value.name
    viewTextSpot.text = value.overview
    viewTextGenre.text = value.genres?.joinToString(",")
    viewTextDuration.text = context.getString(R.string.str_format, value.episodeRuntime?.firstOrNull() ?: 0)
    val rate = "${value.voteAverage}/10"
    viewTextRate.text = rate
    viewRateBar.max = 10
    viewRateBar.rating = (value.voteAverage?.toFloat() ?: 0f) / 2
  }

  override fun unbind() = Unit
}