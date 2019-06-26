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
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.view_simple_show.view.*
import org.fs.architecture.mvi.common.BusManager
import org.fs.architecture.mvi.util.EMPTY
import org.fs.architecture.mvi.util.inflate
import org.fs.architecture.mvi.util.plusAssign
import org.fs.rx.extensions.common.Variable
import org.fs.rx.extensions.util.clicks
import org.fs.tvshows.R
import org.fs.tvshows.common.glide.GlideRequests
import org.fs.tvshows.model.entity.GenreEntity
import org.fs.tvshows.model.entity.TVShowEntity
import org.fs.tvshows.model.event.SelectTVShowEvent
import org.fs.tvshows.util.toPosterUri
import java.text.NumberFormat
import java.util.*

class SimpleTVShowViewHolder(view: View,
  private val genres: Variable<List<GenreEntity>>,
  private val glide: GlideRequests): BaseTVShowViewHolder(view) {

  private val disposeBag by lazy { CompositeDisposable() }
  private val context by lazy { itemView.context }

  private val viewImagePoster by lazy { itemView.viewImagePoster }
  private val viewTextRate by lazy { itemView.viewTextRate }
  private val viewTextTitle by lazy { itemView.viewTextTitle }
  private val viewTextGenre by lazy { itemView.viewTextGenre }
  private val viewTextSpot by lazy { itemView.viewTextSpot }

  private val r by lazy { context.resources.getDimensionPixelSize(R.dimen.dp4) }
  private val numberFormat by lazy { NumberFormat.getNumberInstance(Locale.getDefault()).apply {
      minimumFractionDigits = 1
      maximumFractionDigits = 2
    }
  }

  constructor(parent: ViewGroup,
    genres: Variable<List<GenreEntity>>,
    glide: GlideRequests): this(parent.inflate(R.layout.view_simple_show), genres, glide)

  override fun bind(value: TVShowEntity) {
    // clear and set image
    glide.clear(viewImagePoster)
    glide.load(value.toPosterUri(context))
      .applyRoundedRectCrop(r)
      .into(viewImagePoster)
    // set data
    viewTextRate.text = numberFormat.format(value.voteAverage ?: 0)
    viewTextTitle.text = value.name
    viewTextSpot.text = value.overview

    disposeBag += observeGenreChanges(value).subscribe(viewTextGenre::setText)
    // will send this through
    disposeBag += bindSelectTVShowEvent(value).subscribe(BusManager.Companion::send)
  }

  override fun unbind() = disposeBag.clear()

  private fun observeGenreChanges(value: TVShowEntity): Observable<String> = genres.asObservable()
    .map { list ->
      val array = ArrayList<String>()
      value.genreIds?.forEach { id ->
        val genre = list.firstOrNull { g -> g.id == id } ?: GenreEntity.EMPTY
        if (genre != GenreEntity.EMPTY) {
          array.add(genre.name ?: String.EMPTY)
        }
      }
      return@map array.joinToString(",")
    }

  private fun bindSelectTVShowEvent(value: TVShowEntity): Observable<SelectTVShowEvent> = itemView.clicks()
    .map { SelectTVShowEvent(value) } // maybe later for animations
}