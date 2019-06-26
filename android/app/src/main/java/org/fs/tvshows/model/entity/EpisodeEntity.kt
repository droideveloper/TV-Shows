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

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@JsonClass(generateAdapter = true)
data class EpisodeEntity(
  @field:Json(name = "air_date") val airDate: Date? = null,
  @field:Json(name = "episode_number") val episodeNumber: Int? = null,
  val id: Long? = null,
  val name: String? = null,
  val overview: String? = null,
  val future: Boolean? = null,
  @field:Json(name = "production_code") val productionCode: String? = null,
  @field:Json(name = "season_number") val seasonNumber: Int? = null,
  @field:Json(name = "show_id") val showNumber: Int? = null,
  @field:Json(name = "still_path") val stillPath: String? = null,
  @field:Json(name = "vote_average") val voteAverage: Double? = null,
  @field:Json(name = "vote_count") val voteCount: Int? = null): Parcelable {

  companion object {
    val EMPTY = EpisodeEntity()
  }
}
