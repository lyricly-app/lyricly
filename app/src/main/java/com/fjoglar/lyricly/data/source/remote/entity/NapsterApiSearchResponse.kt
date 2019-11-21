/*
 * Copyright 2019 Felipe Joglar Santos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fjoglar.lyricly.data.source.remote.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NapsterApiSearchResponse(@Json(name = "search") val search: Search? = null) {

    @JsonClass(generateAdapter = true)
    data class Search(@Json(name = "data") val data: Data? = null)

    @JsonClass(generateAdapter = true)
    data class Data(@Json(name = "tracks") val tracks: List<Track>? = null)
}