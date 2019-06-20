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

package org.fs.tvshows.model.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(tableName = "tv_shows")
@Parcelize
@JsonClass(generateAdapter = true)
class TVShowEntity: Parcelable {

  companion object {
    val EMPTY = TVShowEntity()
  }

  @field:Json(name = "original_name") var originalName: String? = null
  @field:Json(name = "genre_ids") var genreIds: List<Long>? = null
  var name: String? = null
  var popularity: Double? = null
  @field:Json(name = "origin_country") var originCountry: List<String>? = null
  @field:Json(name = "vote_count") var voteCount: Int? = null
  @field:Json(name = "first_air_date") var firstAirDate: Date? = null
  @field:Json(name = "backdrop_path") var backdropPath: String? = null
  @field:Json(name = "original_language") var originalLanguage: String? = null
  @PrimaryKey(autoGenerate = false) var id: Long? = null
  @field:Json(name = "vote_average") var voteAverage: Double? = null
  var overview: String? = null
  @field:Json(name = "poster_path") var posterPath: String? = null
  var createDate: Date? = null
}