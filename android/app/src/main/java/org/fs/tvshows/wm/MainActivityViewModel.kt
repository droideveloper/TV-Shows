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
import org.fs.architecture.mvi.common.ForActivity
import org.fs.architecture.mvi.common.Idle
import org.fs.architecture.mvi.common.Intent
import org.fs.architecture.mvi.core.AbstractViewModel
import org.fs.tvshows.common.repo.TVShowRepository
import org.fs.tvshows.model.MainModel
import org.fs.tvshows.model.event.LoadGenreEvent
import org.fs.tvshows.model.intent.LoadGenreIntent
import org.fs.tvshows.model.intent.NothingIntent
import org.fs.tvshows.view.MainActivityView
import javax.inject.Inject

@ForActivity
class MainActivityViewModel @Inject constructor(
  private val tvShowRepository: TVShowRepository,
  view: MainActivityView): AbstractViewModel<MainModel, MainActivityView>(view) {

  override fun initState(): MainModel = MainModel(state = Idle, data = emptyList())

  override fun toIntent(event: Event): Intent = when (event) {
    is LoadGenreEvent -> LoadGenreIntent(tvShowRepository)
    else -> NothingIntent<MainModel>() // if we can not resolve event to intent
  }

} 