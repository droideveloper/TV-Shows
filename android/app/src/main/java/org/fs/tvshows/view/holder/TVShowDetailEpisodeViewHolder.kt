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
import kotlinx.android.synthetic.main.view_episode_item.view.*
import kotlinx.android.synthetic.main.view_tv_show_detail_episode_item.view.*
import org.fs.architecture.mvi.util.inflate
import org.fs.tvshows.R
import org.fs.tvshows.common.glide.GlideRequests
import org.fs.tvshows.model.entity.EpisodeEntity
import org.fs.tvshows.model.entity.TVShowDetailEntity
import org.fs.tvshows.util.toStillUri
import java.text.SimpleDateFormat
import java.util.*

class TVShowDetailEpisodeViewHolder(view: View, private val glide: GlideRequests): BaseTVShowDetailViewHolder(view) {

  private val viewImagePoster by lazy { itemView.viewImagePoster }
  private val viewTextTitle by lazy { itemView.viewTextTitle }
  private val viewTextRate by lazy { itemView.viewTextRate }
  private val viewTextRelease by lazy { itemView.viewTextRelease }
  private val viewTextSpot by lazy { itemView.viewTextSpot }

  private val context by lazy { itemView.context }
  private val r by lazy { context.resources.getDimensionPixelSize(R.dimen.dp4) }
  private val dateFormat by lazy { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }

  private val viewTitleText by lazy { itemView.viewTitleText }

  constructor(parent: ViewGroup, glide: GlideRequests): this(parent.inflate(R.layout.view_tv_show_detail_episode_item), glide)

  override fun bind(value: TVShowDetailEntity) {
    val titleRes = if (value.nextEpisodeToAir != null) R.string.str_next_episode_title else R.string.str_last_episode_title
    viewTitleText.setText(titleRes)
    val episode = value.nextEpisodeToAir ?: value.lastEpisodeAired ?: EpisodeEntity.EMPTY
    bindEpisode(episode)
  }

  override fun unbind() = Unit

  private fun bindEpisode(episode: EpisodeEntity) {
    if (episode != EpisodeEntity.EMPTY) {
      val uri = episode.toStillUri(context)

      glide.clear(viewImagePoster)
      glide.load(uri)
        .applyRoundedRectCrop(r)
        .into(viewImagePoster)

      viewTextRelease.text = dateFormat.format(episode.airDate)
      viewTextRate.text = episode.voteAverage.toString()
      viewTextTitle.text = episode.name
      viewTextSpot.text = episode.overview
    }
  }
}