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

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import okhttp3.Interceptor
import org.fs.tvshows.App
import org.fs.tvshows.common.interceptor.AuthorizationInterceptor
import org.fs.tvshows.common.repo.TVShowRepository
import org.fs.tvshows.common.repo.TVShowRepositoryImp
import org.fs.tvshows.net.EndpointProxy
import org.fs.tvshows.net.EndpointProxyImp
import javax.inject.Singleton

@Module
abstract class AppModule {

  @Singleton @Binds abstract fun bindApplication(app: App): Application
  @Singleton @Binds abstract fun bindContext(app: Application): Context

  @Singleton @Binds abstract fun bindTVShowRepository(repo: TVShowRepositoryImp): TVShowRepository

  @Singleton @Binds abstract fun bindEndpointProxy(proxy: EndpointProxyImp): EndpointProxy

  @Singleton @Binds abstract fun bindAuthorizationInterceptor(interceptor: AuthorizationInterceptor): Interceptor
}