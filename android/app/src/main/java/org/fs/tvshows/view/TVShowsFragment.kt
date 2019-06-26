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
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import io.reactivex.Observable
import kotlinx.android.synthetic.main.view_tv_show_fragment.*
import org.fs.architecture.mvi.common.BusManager
import org.fs.architecture.mvi.common.Failure
import org.fs.architecture.mvi.common.Idle
import org.fs.architecture.mvi.common.Operation
import org.fs.architecture.mvi.core.AbstractFragment
import org.fs.architecture.mvi.util.ObservableList
import org.fs.architecture.mvi.util.plusAssign
import org.fs.rx.extensions.v4.util.refreshes
import org.fs.rx.extensions.v7.util.loadMore
import org.fs.rx.extensions.v7.util.menuItemClicks
import org.fs.tvshows.R
import org.fs.tvshows.common.recycler.SafeLinearLayoutManager
import org.fs.tvshows.model.TVShowsModel
import org.fs.tvshows.model.entity.TVShowEntity
import org.fs.tvshows.model.event.LoadMoreTVShowEvent
import org.fs.tvshows.model.event.LoadTVShowEvent
import org.fs.tvshows.model.event.SelectTVShowEvent
import org.fs.tvshows.util.C.Companion.RECYCLER_CACHE_SIZE
import org.fs.tvshows.util.Operations.Companion.LOAD_MORE
import org.fs.tvshows.util.Operations.Companion.REFRESH
import org.fs.tvshows.util.bind
import org.fs.tvshows.util.recyclerDivider
import org.fs.tvshows.util.showError
import org.fs.tvshows.view.adapter.TVShowAdapter
import org.fs.tvshows.wm.TVShowsFragmentViewModel
import javax.inject.Inject

class TVShowsFragment : AbstractFragment<TVShowsModel, TVShowsFragmentViewModel>(),
  TVShowsFragmentView {

  companion object {

    @JvmStatic fun newInstance(): TVShowsFragment = TVShowsFragment()
  }

  override val layoutRes: Int get() = R.layout.view_tv_show_fragment

  @Inject lateinit var tvShowAdapter: TVShowAdapter
  @Inject lateinit var dataSet: ObservableList<TVShowEntity>

  private val isTabletDevice by lazy { resources.getBoolean(R.bool.isTablet) }

  private val verticalDivider by lazy { ResourcesCompat.getDrawable(resources, R.drawable.ic_vertical_divider, context?.theme) }

  private var page = 0
  private var totalPage = 0

  override fun setUp(state: Bundle?) {
    viewRecycler.apply {
      setItemViewCacheSize(RECYCLER_CACHE_SIZE)
      layoutManager = SafeLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
      adapter = tvShowAdapter
      verticalDivider?.recyclerDivider(this, DividerItemDecoration.VERTICAL)
    }
    viewSwipeRefreshLayout.setColorSchemeResources(R.color.color_green, R.color.color_orange)
  }

  override fun attach() {
    super.attach()

    disposeBag += viewModel.state()
      .map { state ->
        if (state is Operation) {
          return@map state.type == REFRESH
        }
        return@map false
      }
      .subscribe(viewSwipeRefreshLayout::bind)

    disposeBag += bindLoadMoreState().subscribe(viewSwipeRefreshLayout::bind) // shows progress

    disposeBag += bindLoadMoreState().subscribe(::loadMore) // appends loading indicator at the end with its relative entity

    disposeBag += bindLoadMore().subscribe(::accept) // bind load more event for pipe

    disposeBag += bindSwipeToRefresh().subscribe(::accept) // bind swipe refresh here

    disposeBag += viewModel.storage()
      .subscribe(::render)

    checkIfInitialLoadNeeded()
  }

  override fun render(model: TVShowsModel)  = when(model.state) {
    is Idle -> Unit
    is Failure -> showError(model.state.error)
    is Operation -> when(model.state.type) {
      REFRESH, LOAD_MORE -> render(model.data, model.page, model.totalPage)
      else -> Unit
    }
  }

  override fun bindLoadMore(): Observable<LoadMoreTVShowEvent> = viewRecycler.loadMore()
    .filter { page < totalPage }
    .doOnNext {
      page += 1
      totalPage = page // for now assign it same collection so we won't go over over again
    }
    .map { LoadMoreTVShowEvent(page) }

  override fun bindLoadMoreState(): Observable<Boolean> = viewModel.state()
    .map { state ->
      if (state is Operation) {
        return@map state.type == LOAD_MORE
      }
      return@map false
    }

  override fun bindSwipeToRefresh(): Observable<LoadTVShowEvent> = viewSwipeRefreshLayout.refreshes()
    .doOnNext {
      page = 0
      totalPage = 0
      dataSet.clear()
    }
    .map { LoadTVShowEvent() }

  override fun showBookmarks(): Observable<Boolean> = viewToolbar.menuItemClicks()
    .map { item ->
      return@map item.itemId == R.id.ic_action_bookmark
    }

  private fun render(data: List<TVShowEntity>, page: Int, totalPage: Int) {
    this.page = page
    this.totalPage = totalPage
    if (data.isNotEmpty()) {
      if (dataSet.isEmpty() && isTabletDevice) {
        val init = data.firstOrNull() ?: TVShowEntity.EMPTY
        BusManager.send(SelectTVShowEvent(init))
      }
      dataSet.addAll(data)
    }
  }

  private fun loadMore(loadMore: Boolean) {
    val position = dataSet.indexOfFirst { t -> t == TVShowEntity.EMPTY }
    val hasRemoveFromDataSet = position != -1 && !loadMore
    if (hasRemoveFromDataSet) {
      dataSet.remove(TVShowEntity.EMPTY)
    }
    val hasAppendToDataSet = position == -1 && loadMore
    if (hasAppendToDataSet) {
      dataSet.add(TVShowEntity.EMPTY)
    }
  }

  private fun checkIfInitialLoadNeeded() {
    if (dataSet.isEmpty()) {
      accept(LoadTVShowEvent())
    }
  }
}