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

package org.fs.tvshows.view.adapter

import android.content.Context
import android.view.ViewGroup
import org.fs.architecture.mvi.common.ForFragment
import org.fs.architecture.mvi.core.AbstractRecyclerViewAdapter
import org.fs.architecture.mvi.util.ObservableList
import org.fs.tvshows.common.glide.GlideApp
import org.fs.tvshows.model.entity.TVShowDetailEntity
import org.fs.tvshows.view.holder.*
import java.lang.IllegalArgumentException
import javax.inject.Inject

@ForFragment
class TVShowDetailAdapter @Inject constructor(dataSet: ObservableList<TVShowDetailEntity>, context: Context): AbstractRecyclerViewAdapter<TVShowDetailEntity, BaseTVShowDetailViewHolder>(dataSet) {

  companion object {
    private const val TYPE_SPOT = 0x01
    private const val TYPE_SEASON = 0x02
    private const val TYPE_CREATED = 0x03
    private const val TYPE_PRODUCTION = 0x04
    private const val TYPE_NETWORK = 0x05
    private const val TYPE_EPISODE = 0x06
  }

  private val glide by lazy { GlideApp.with(context) }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseTVShowDetailViewHolder = when(viewType) {
    TYPE_SPOT -> TVShowDetailSpotViewHolder(parent)
    TYPE_SEASON -> TVShowDetailSeasonViewHolder(parent)
    TYPE_CREATED -> TVShowDetailCreatedByViewHolder(parent)
    TYPE_PRODUCTION -> TVShowDetailProductionViewHolder(parent)
    TYPE_NETWORK -> TVShowDetailNetworkViewHolder(parent)
    TYPE_EPISODE -> TVShowDetailEpisodeViewHolder(parent, glide)
    else -> throw IllegalArgumentException("could not recognize viewType for $viewType")
  }

  override fun getItemViewType(position: Int): Int {
    val item = dataSet[position]
    return when {
      item.genres != null && item.name != null -> TYPE_SPOT
      item.seasons != null -> TYPE_SEASON
      item.creators != null -> TYPE_CREATED
      item.productionCompanies != null -> TYPE_PRODUCTION
      item.networks != null -> TYPE_NETWORK
      item.nextEpisodeToAir != null || item.lastEpisodeAired != null -> TYPE_EPISODE
      else -> super.getItemViewType(position)
    }
  }
}