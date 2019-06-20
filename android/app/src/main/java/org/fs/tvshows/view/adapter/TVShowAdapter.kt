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
import org.fs.architecture.mvi.common.ForActivity
import org.fs.architecture.mvi.common.ForFragment
import org.fs.architecture.mvi.core.AbstractRecyclerViewAdapter
import org.fs.architecture.mvi.util.ObservableList
import org.fs.rx.extensions.common.Variable
import org.fs.tvshows.common.glide.GlideApp
import org.fs.tvshows.model.entity.GenreEntity
import org.fs.tvshows.model.entity.TVShowEntity
import org.fs.tvshows.view.holder.BaseTVShowViewHolder
import org.fs.tvshows.view.holder.ProgressTVShowViewHolder
import org.fs.tvshows.view.holder.SimpleTVShowViewHolder
import java.lang.IllegalArgumentException
import javax.inject.Inject

@ForFragment
class TVShowAdapter @Inject constructor(
  private val genres: Variable<List<GenreEntity>>,
  context: Context,
  dataSet: ObservableList<TVShowEntity>): AbstractRecyclerViewAdapter<TVShowEntity, BaseTVShowViewHolder>(dataSet) {

  companion object {
    const val VIEW_TYPE_SIMPLE = 0x01
    const val VIEW_TYPE_PROGRESS = 0x02
  }

  private val glide by lazy { GlideApp.with(context) }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseTVShowViewHolder = when(viewType) {
    VIEW_TYPE_SIMPLE -> SimpleTVShowViewHolder(parent, genres, glide)
    VIEW_TYPE_PROGRESS -> ProgressTVShowViewHolder(parent)
    else -> throw IllegalArgumentException("could not recognize viewType for $viewType")
  }

  override fun getItemViewType(position: Int): Int {
    val item = dataSet[position]
    return when {
      item == TVShowEntity.EMPTY -> VIEW_TYPE_PROGRESS
      item.originalName != null -> VIEW_TYPE_SIMPLE
      else -> super.getItemViewType(position)
    }
  }
}