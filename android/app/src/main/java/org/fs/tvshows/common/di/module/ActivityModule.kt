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

package org.fs.tvshows.common.di.module

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import org.fs.architecture.mvi.common.ForActivity
import org.fs.architecture.mvi.common.ForFragment
import org.fs.tvshows.view.MainActivity
import org.fs.tvshows.view.MainActivityView
import org.fs.tvshows.view.TVShowDetailFragment
import org.fs.tvshows.view.TVShowsFragment

@Module
abstract class ActivityModule {

  @ForActivity @Binds abstract fun bindMainActivityView(activity: MainActivity): MainActivityView

  @ForFragment @ContributesAndroidInjector(modules = [FragmentModule::class, ProviderFragmentModule::class])
  abstract fun contributeTVShowsFragment(): TVShowsFragment

  @ForFragment @ContributesAndroidInjector(modules = [FragmentModule::class, ProviderFragmentModule::class])
  abstract fun contributeTVShowDetailFragment(): TVShowDetailFragment

}