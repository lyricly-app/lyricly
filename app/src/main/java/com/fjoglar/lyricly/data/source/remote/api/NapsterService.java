/*
 * Copyright 2018 Felipe Joglar Santos
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

package com.fjoglar.lyricly.data.source.remote.api;

import com.fjoglar.lyricly.data.source.remote.entity.NapsterApiResponse;
import com.fjoglar.lyricly.data.source.remote.entity.NapsterApiSearchResponse;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NapsterService {

    String BASE_URL = "http://api.napster.com/v2.2/";

    @GET("tracks/top")
    Call<NapsterApiResponse> getTopTracks(
            @Query("apikey") String apiKey,
            @Query("limit") int limit);

    @GET("search")
    Call<NapsterApiSearchResponse> searchCurrentlyPlayingTrack(
            @Query("apikey") String apiKey,
            @Query("type") String type,
            @Query("per_type_limit") int perTypeLimit,
            @Query("query") String query);

    static Retrofit retrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }
}
