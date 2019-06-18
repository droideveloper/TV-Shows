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

package org.fs.tvshows.net

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import org.fs.tvshows.model.entity.GenreEntity
import org.fs.tvshows.model.entity.TvShowDetailEntity
import org.fs.tvshows.model.entity.TvShowEntity
import org.fs.tvshows.net.model.Resource
import org.fs.tvshows.net.model.response.GenreResponse
import org.fs.tvshows.net.model.response.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EndpointProxyImp @Inject constructor(private val endpoint: Endpoint): EndpointProxy {

  override fun genres(): Observable<Resource<List<GenreEntity>>> = endpoint.genres().asGenreResource()
  override fun shows(tvType: String, page: Int): Observable<Resource<List<TvShowEntity>>> = endpoint.shows(tvType, page).toResource()
  override fun showDetail(tvShowId: Long): Observable<Resource<TvShowDetailEntity>> = endpoint.showDetail(tvShowId).asTvShowDetailResponse()

  // helper method for genre response to resource
  private fun Observable<retrofit2.Response<GenreResponse>>.asGenreResource(): Observable<Resource<List<GenreEntity>>> = map { response ->
    if (response.isSuccessful) {
      return@map Resource.Success(response.body()?.genres, null, null)
    }
    return@map Resource.Failure<List<GenreEntity>>(statusCode = response.code(), statusMessage = response.message())
  }.retryForWithDelay()

  // tv show detail to resource helper
  private fun Observable<retrofit2.Response<TvShowDetailEntity>>.asTvShowDetailResponse(): Observable<Resource<TvShowDetailEntity>> = map { response ->
    if (response.isSuccessful) {
      return@map Resource.Success(response.body(), null, null)
    }
    return@map Resource.Failure<TvShowDetailEntity>(statusCode = response.code(), statusMessage = response.message())
  }.retryForWithDelay()

  // tv show list to resource helper
  private fun <T> Observable<Response<T>>.toResource(): Observable<Resource<T>> = map { response ->
    if (response.statusCode != null) {
      return@map Resource.Success(response.result, response.page, response.totalPages)
    }
    return@map Resource.Failure<T>(response.statusCode, response.statusMessage)
  }.retryForWithDelay()

  // retry by gradually helper, which is when we fail it will trigger error index * delay later until reaching 3rd and more
  private fun <T> Observable<T>.retryForWithDelay(maxRetry: Int = 3, delay: Long = 3L, unit: TimeUnit = TimeUnit.SECONDS): Observable<T> = retryWhen { errors ->
    return@retryWhen errors.zipWith(Observable.range(0, maxRetry), BiFunction<Throwable, Int, Long> { _, t -> t.toLong() })
      .flatMap { t -> Observable.timer(t * delay, unit) }
  }
}