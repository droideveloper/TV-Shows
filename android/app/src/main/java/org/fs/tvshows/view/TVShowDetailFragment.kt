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

import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.view_tv_show_detail_fragment.*
import org.fs.architecture.mvi.common.BusManager
import org.fs.architecture.mvi.common.Failure
import org.fs.architecture.mvi.common.Idle
import org.fs.architecture.mvi.common.Operation
import org.fs.architecture.mvi.core.AbstractFragment
import org.fs.architecture.mvi.util.ObservableList
import org.fs.architecture.mvi.util.plusAssign
import org.fs.rx.extensions.util.clicks
import org.fs.rx.extensions.v7.util.navigationClicks
import org.fs.tvshows.R
import org.fs.tvshows.common.glide.GlideApp
import org.fs.tvshows.common.recycler.SafeLinearLayoutManager
import org.fs.tvshows.model.TVShowDetailModel
import org.fs.tvshows.model.entity.TVShowDetailEntity
import org.fs.tvshows.model.entity.TVShowEntity
import org.fs.tvshows.model.event.AddOrRemoveBookmarkEvent
import org.fs.tvshows.model.event.LoadTVShowDetailEvent
import org.fs.tvshows.model.event.SelectTVShowEvent
import org.fs.tvshows.util.*
import org.fs.tvshows.util.C.Companion.RECYCLER_CACHE_SIZE
import org.fs.tvshows.util.Operations.Companion.REFRESH
import org.fs.tvshows.util.Operations.Companion.REMOVE_BOOKMARK
import org.fs.tvshows.util.Operations.Companion.SAVE_BOOKMARK
import org.fs.tvshows.view.adapter.TVShowDetailAdapter
import org.fs.tvshows.wm.TVShowDetailFragmentViewModel
import javax.inject.Inject

class TVShowDetailFragment : AbstractFragment<TVShowDetailModel, TVShowDetailFragmentViewModel>(),
  TVShowDetailFragmentView {

  companion object {
    private const val BUNDLE_ARGS_TV_SHOW = "bundle.args.tv.show"

    @JvmStatic fun newInstance(tvShow: TVShowEntity): TVShowDetailFragment = TVShowDetailFragment().apply {
      arguments = Bundle().apply {
        putParcelable(BUNDLE_ARGS_TV_SHOW, tvShow)
      }
    }
  }

  @Inject lateinit var tvShowDetailAdapter: TVShowDetailAdapter
  @Inject lateinit var dataSet: ObservableList<TVShowDetailEntity>

  override val layoutRes: Int get() = R.layout.view_tv_show_detail_fragment

  private val verticalDivider by lazy { ResourcesCompat.getDrawable(resources, R.drawable.ic_vertical_divider, context?.theme) }
  private val r by lazy { resources.getDimensionPixelSize(R.dimen.dp4) }

  private val elevation1dp by lazy { resources.getDimension(R.dimen.dp1) }
  private val elevation2dp by lazy { resources.getDimension(R.dimen.dp2) }

  private val glide by lazy { GlideApp.with(this) }

  private var tvShow = TVShowEntity.EMPTY

  override fun setUp(state: Bundle?) {
    tvShow = state?.getParcelable(BUNDLE_ARGS_TV_SHOW) ?: TVShowEntity.EMPTY
    viewRecycler.apply {
      setItemViewCacheSize(RECYCLER_CACHE_SIZE)
      layoutManager = SafeLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
      adapter = tvShowDetailAdapter
      verticalDivider?.recyclerDivider(this, DividerItemDecoration.VERTICAL)
    }
    viewSwipeRefreshLayout.setColorSchemeResources(R.color.color_green, R.color.color_orange)
    viewSwipeRefreshLayout.isEnabled = false

    ViewCompat.setElevation(viewImagePoster, elevation2dp)
    ViewCompat.setElevation(viewOverlay, elevation1dp)

    applyTvShow(tvShow)
  }

  override fun attach() {
    super.attach()

    disposeBag += BusManager.add(Consumer { evt -> when(evt) {
        is SelectTVShowEvent -> applyTvShow(evt.tvShow).also {
          tvShow = evt.tvShow
          dataSet.clear()
          checkIfInitialLoadNeeded()
        }
      }
    })

    disposeBag += viewModel.state()
      .map { state ->
        if (state is Operation) {
          return@map state.type == REFRESH
        }
        return@map false
      }
      .subscribe(viewSwipeRefreshLayout::bind)

    disposeBag += viewModel.state()
      .map { state ->
        if (state is Operation) {
          return@map state.type == SAVE_BOOKMARK || state.type == REMOVE_BOOKMARK
        }
        return@map false
      }
      .subscribe(::toggleState)

    disposeBag += viewModel.storage()
      .subscribe(::render)

    checkIfInitialLoadNeeded()
  }

  override fun render(model: TVShowDetailModel) = when(model.state) {
    is Idle -> Unit
    is Failure -> showError(model.state.error)
    is Operation -> when(model.state.type) {
      REFRESH -> render(model.data)
      SAVE_BOOKMARK, REMOVE_BOOKMARK -> saveState(model.saveState)
      else -> Unit
    }
  }

  override fun bindBack(): Observable<View> = viewToolbar.navigationClicks()

  override fun bindBookmark(): Observable<AddOrRemoveBookmarkEvent> = viewBookmarkButton.clicks()
    .map { AddOrRemoveBookmarkEvent(tvShow, !it.isSelected) }

  private fun render(entity: TVShowDetailEntity) {
    if (entity != TVShowDetailEntity.EMPTY) {
      // append spot
      val spot = entity.copy()
      dataSet.add(spot)
      // append season
      val season = TVShowDetailEntity(seasons = entity.seasons)
      if (season.seasons?.isNullOrEmpty() == false) {
        dataSet.add(season)
      }
      // append network
      val network = TVShowDetailEntity(networks = entity.networks)
      if (network.networks?.isNullOrEmpty() == false) {
        dataSet.add(network)
      }
      // append created by
      val createdBy = TVShowDetailEntity(creators = entity.creators)
      if (createdBy.creators?.isNullOrEmpty() == false) {
        dataSet.add(createdBy)
      }
      // append production
      val production = TVShowDetailEntity(productionCompanies = entity.productionCompanies)
      if (production.productionCompanies?.isNullOrEmpty() == false) {
        dataSet.add(production)
      }
      // append last episode
      val lastEpisode = TVShowDetailEntity(lastEpisodeAired = entity.lastEpisodeAired)
      if (lastEpisode.lastEpisodeAired != null) {
        dataSet.add(lastEpisode)
      }
      // append next episode
      val nextEpisode = TVShowDetailEntity(nextEpisodeToAir = entity.nextEpisodeToAir)
      if (nextEpisode.nextEpisodeToAir != null) {
        dataSet.add(nextEpisode)
      }
    }
  }

  private fun saveState(saveState: Boolean) { viewBookmarkButton.isSelected = saveState }

  private fun toggleState(showProgress: Boolean) {
    viewProgress.visibility = if (showProgress) View.VISIBLE else View.GONE
    viewProgress.isIndeterminate = showProgress
    viewBookmarkButton.isEnabled = !showProgress
  }

  private fun checkIfInitialLoadNeeded() {
    if (dataSet.isEmpty() && tvShow != TVShowEntity.EMPTY) {
      accept(LoadTVShowDetailEvent(tvShow.id ?: 0L))
    }
  }

  private fun applyTvShow(tvShow: TVShowEntity) {
    if (tvShow != TVShowEntity.EMPTY) {
      context?.let { ctx ->
        val backdropUri = tvShow.toBackdropUri(ctx)

        glide.clear(viewImageBackground)
        glide.load(backdropUri)
          .applyCrop()
          .into(viewImageBackground)

        val posterUri = tvShow.toPosterUri(ctx)
        glide.clear(viewImagePoster)
        glide.load(posterUri)
          .applyRoundedRectCrop(r)
          .into(viewImagePoster)
      }
    }
  }
}