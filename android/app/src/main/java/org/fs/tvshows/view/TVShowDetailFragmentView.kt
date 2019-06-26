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

import io.reactivex.Observable
import android.view.View
import org.fs.tvshows.model.TVShowDetailModel
import org.fs.tvshows.model.event.AddOrRemoveBookmarkEvent

interface TVShowDetailFragmentView : org.fs.architecture.mvi.common.View {
  fun render(model: TVShowDetailModel)
  fun bindBookmark(): Observable<AddOrRemoveBookmarkEvent>
  fun bindBack(): Observable<View>
}