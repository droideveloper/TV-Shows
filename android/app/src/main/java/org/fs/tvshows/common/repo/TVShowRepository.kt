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

package org.fs.tvshows.common.repo

import io.reactivex.Observable
import org.fs.tvshows.model.entity.GenreEntity
import org.fs.tvshows.model.entity.TVShowDetailEntity
import org.fs.tvshows.model.entity.TVShowEntity
import org.fs.tvshows.net.model.Resource

interface TVShowRepository {

  fun genres(): Observable<Resource<List<GenreEntity>>>
  fun shows(tvType: String = "popular", page: Int = 0): Observable<Resource<List<TVShowEntity>>>
  fun showDetail(tvShowId: Long): Observable<Resource<TVShowDetailEntity>>
}