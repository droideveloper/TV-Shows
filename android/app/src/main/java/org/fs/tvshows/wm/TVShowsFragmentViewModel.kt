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
package org.fs.tvshows.wm

import org.fs.architecture.mvi.common.Event
import org.fs.architecture.mvi.common.ForFragment
import org.fs.architecture.mvi.common.Idle
import org.fs.architecture.mvi.common.Intent
import org.fs.architecture.mvi.core.AbstractViewModel
import org.fs.tvshows.common.repo.TVShowRepository
import org.fs.tvshows.model.TVShowsModel
import org.fs.tvshows.model.event.LoadMoreTVShowEvent
import org.fs.tvshows.model.event.LoadTVShowEvent
import org.fs.tvshows.model.intent.LoadMoreTVShowIntent
import org.fs.tvshows.model.intent.LoadTVShowIntent
import org.fs.tvshows.model.intent.NothingIntent
import org.fs.tvshows.view.TVShowsFragmentView
import javax.inject.Inject

@ForFragment
class TVShowsFragmentViewModel @Inject constructor(
  private val tvShowRepository: TVShowRepository,
  view: TVShowsFragmentView): AbstractViewModel<TVShowsModel, TVShowsFragmentView>(view) {

  override fun initState(): TVShowsModel = TVShowsModel(state = Idle, data = emptyList(), page = 0, totalPage = 0)

  override fun toIntent(event: Event): Intent = when (event) {
    is LoadMoreTVShowEvent -> LoadMoreTVShowIntent(event.page, tvShowRepository)
    is LoadTVShowEvent -> LoadTVShowIntent(tvShowRepository)
    else -> NothingIntent<TVShowsModel>() // if we can not resolve event to intent
  }
} 