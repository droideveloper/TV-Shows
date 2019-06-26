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

package org.fs.tvshows.model.intent

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.fs.architecture.mvi.common.*
import org.fs.tvshows.common.repo.TVShowRepository
import org.fs.tvshows.model.MainModel
import org.fs.tvshows.model.entity.GenreEntity
import org.fs.tvshows.net.model.Resource
import org.fs.tvshows.util.Operations.Companion.REFRESH
import java.io.IOException

class LoadGenreIntent(private val tvShowRepository: TVShowRepository): ObservableIntent<MainModel>() {

  override fun invoke(): Observable<Reducer<MainModel>> = tvShowRepository.genres()
    .concatMap(::success)
    .onErrorResumeNext(::failure)
    .startWith(initial())
    .subscribeOn(Schedulers.io())

  private fun success(resource: Resource<List<GenreEntity>>): Observable<Reducer<MainModel>> = when(resource) {
    is Resource.Success<List<GenreEntity>> -> Observable.just(
      { o -> o.copy(state = Operation(REFRESH), data = resource.data ?: emptyList()) },
      { o -> o.copy(state = Idle, data = emptyList()) })
    is Resource.Failure<List<GenreEntity>> -> Observable.just(
      { o -> o.copy(state = Failure(IOException(resource.statusMessage))) },
      { o -> o.copy(state = Idle) })
  }

  private fun failure(error: Throwable): Observable<Reducer<MainModel>> = Observable.just(
    { o -> o.copy(state = Failure(error)) },
    { o -> o.copy(state = Idle) })

  private fun initial(): Reducer<MainModel> = { o -> o.copy(state = Operation(REFRESH), data = emptyList()) }
}