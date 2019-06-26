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
import org.fs.tvshows.model.entity.TVShowDetailEntity
import org.fs.tvshows.model.entity.TVShowEntity
import org.fs.tvshows.net.model.response.GenreResponse
import org.fs.tvshows.net.model.response.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Endpoint {

  @GET("genre/tv/list") fun genres(): Observable<retrofit2.Response<GenreResponse>>
  @GET("tv/{tvType}") fun shows(@Path("tvType") type: String, @Query("page") page: Int): Observable<Response<List<TVShowEntity>>>
  @GET("tv/{tvShowId}") fun showDetail(@Path("tvShowId") tvShowId: Long): Observable<retrofit2.Response<TVShowDetailEntity>>
}