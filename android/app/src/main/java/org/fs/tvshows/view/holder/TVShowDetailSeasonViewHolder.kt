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
import kotlinx.android.synthetic.main.view_tv_show_detail_seasons_item.view.*
import org.fs.architecture.mvi.util.inflate
import org.fs.tvshows.R
import org.fs.tvshows.model.entity.TVShowDetailEntity

class TVShowDetailSeasonViewHolder(view: View): BaseTVShowDetailViewHolder(view) {

  private val viewSeasonLayout by lazy { itemView.viewSeasonLayout }

  constructor(parent: ViewGroup): this(parent.inflate(R.layout.view_tv_show_detail_seasons_item))

  override fun bind(value: TVShowDetailEntity) {
    val season = value.seasons ?: emptyList()
    viewSeasonLayout.bindSeasons(season)
  }

  override fun unbind() = Unit
}