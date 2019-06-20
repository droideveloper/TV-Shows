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
data class TVShowDetailEntity(
  @field:Json(name = "backdrop_path") val backdropPath: String? = null,
  @field:Json(name = "created_by") val creators: List<CreatorEntity>? = null,
  @field:Json(name = "episode_run_time") val episodeRuntime: List<Int>? = null,
  @field:Json(name = "first_air_date") val firstAirDate: Date? = null,
  val genres: List<GenreEntity>? = null,
  val homepage: String? = null,
  val id: Long? = null,
  @field:Json(name = "in_production") val inProduction: Boolean? = null,
  val languages: List<String>? = null,
  @field:Json(name = "last_air_date") val lastAirDate: Date? = null,
  @field:Json(name = "last_episode_to_air") val lastEpisodeAired: EpisodeEntity? = null,
  val name: String? = null,
  @field:Json(name = "next_episode_to_air") val nextEpisodeToAir: EpisodeEntity? = null,
  val networks: List<NetworkEntity>? = null,
  @field:Json(name = "number_of_episodes") val numberOfEpisodes: Int? = null,
  @field:Json(name = "number_of_seasons") val numberOfSeasons: Int? = null,
  @field:Json(name = "origin_country") val originCountry: List<String>? = null,
  @field:Json(name = "original_language") val originalLanguage: String? = null,
  @field:Json(name = "original_name") val originalName: String? = null,
  val overview: String? = null,
  val popularity: Double? = null,
  @field:Json(name = "poster_path") val posterPath: String? = null,
  @field:Json(name = "production_companies") val productionCompanies: List<ProductionCompany>? = null,
  val seasons: List<SeasonEntity>? = null,
  val status: String? = null,
  val type: String? = null,
  @field:Json(name = "vote_average") val voteAverage: Double? = null,
  @field:Json(name = "vote_count") val voteCount: Double? = null): Parcelable {

  companion object {
    val EMPTY = TVShowDetailEntity()
  }
}