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
package org.fs.tvshows.view

import android.content.pm.ActivityInfo
import android.os.Bundle
import io.reactivex.functions.Consumer
import org.fs.architecture.mvi.common.BusManager
import org.fs.architecture.mvi.common.Failure
import org.fs.architecture.mvi.common.Idle
import org.fs.architecture.mvi.common.Operation
import org.fs.architecture.mvi.core.AbstractActivity
import org.fs.architecture.mvi.util.plusAssign
import org.fs.rx.extensions.common.Variable
import org.fs.tvshows.R
import org.fs.tvshows.model.MainModel
import org.fs.tvshows.model.entity.GenreEntity
import org.fs.tvshows.model.entity.TVShowEntity
import org.fs.tvshows.model.event.LoadGenreEvent
import org.fs.tvshows.model.event.SelectTVShowEvent
import org.fs.tvshows.util.Operations.Companion.REFRESH
import org.fs.tvshows.util.showError
import org.fs.tvshows.wm.MainActivityViewModel
import javax.inject.Inject

class MainActivity : AbstractActivity<MainModel, MainActivityViewModel>(), MainActivityView {

  @Inject lateinit var genre: Variable<List<GenreEntity>>

  override val layoutRes: Int get() = R.layout.view_main_activity

  private val isTabletDevice by lazy { resources.getBoolean(R.bool.isTablet) }

  override fun onCreate(savedInstanceState: Bundle?) {
    requestedOrientation = if (isTabletDevice) {
      ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    } else {
      ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
    super.onCreate(savedInstanceState)
  }

  override fun setUp(state: Bundle?) {
    if (!isTabletDevice) {
      supportFragmentManager.beginTransaction()
        .setReorderingAllowed(true)
        .replace(R.id.viewContentFrame, TVShowsFragment.newInstance())
        .addToBackStack(null)
        .commit()
    }
  }

  override fun attach() {
    super.attach()

    disposeBag += BusManager.add(Consumer { evt -> when(evt) {
        is SelectTVShowEvent -> applyDetail(evt.tvShow)
      }
    })

    disposeBag += viewModel.storage()
      .subscribe(::render)

    checkIfInitialLoadNeeded()
  }

  override fun onBackPressed() {
    if (isTabletDevice) {
      finish()
    } else {
      val hasPop = supportFragmentManager.backStackEntryCount != 0
      if (hasPop) {
        supportFragmentManager.popBackStack()
      } else {
        finish()
      }
    }
  }

  override fun render(model: MainModel) = when(model.state) {
    is Idle -> Unit
    is Failure -> showError(model.state.error)
    is Operation -> when(model.state.type) {
      REFRESH -> render(model.data)
      else -> Unit
    }
  }

  private fun render(data: List<GenreEntity>) {
    if (data.isNotEmpty()) {
      genre.value = data
    }
  }

  private fun checkIfInitialLoadNeeded() {
    val value = genre.value
    if (value.isEmpty()) {
      accept(LoadGenreEvent())
    }
  }

  private fun applyDetail(show: TVShowEntity) {
    if (!isTabletDevice) {
      supportFragmentManager.beginTransaction()
        .setReorderingAllowed(true)
        .replace(R.id.viewContentFrame, TVShowDetailFragment.newInstance(show))
        .addToBackStack(null)
        .commit()
    }
  }
} 