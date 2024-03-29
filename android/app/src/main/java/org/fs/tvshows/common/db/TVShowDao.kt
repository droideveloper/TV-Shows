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

package org.fs.tvshows.common.db

import android.arch.persistence.room.*
import org.fs.tvshows.model.entity.TVShowEntity

@Dao interface TVShowDao {

  @Insert fun insert(entity: TVShowEntity): Int
  @Update fun update(entity: TVShowEntity): Int
  @Delete fun delete(entity: TVShowEntity): Int

  @Query("SELECT * FROM tv_shows ORDER BY createDate LIMIT :offset,:limit") fun tvShows(offset: Int, limit: Int): List<TVShowEntity>

  @Query("SELECT * FROM tv_shows WHERE id = :id") fun tvShowById(id: Long): List<TVShowEntity>
}