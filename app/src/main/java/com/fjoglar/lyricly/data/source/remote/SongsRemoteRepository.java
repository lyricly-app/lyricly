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

package com.fjoglar.lyricly.data.source.remote;

import androidx.annotation.Nullable;

import com.fjoglar.lyricly.BuildConfig;
import com.fjoglar.lyricly.data.source.remote.api.NapsterService;
import com.fjoglar.lyricly.data.source.remote.api.OvhLyricsService;
import com.fjoglar.lyricly.data.source.remote.entity.NapsterApiResponse;
import com.fjoglar.lyricly.data.source.remote.entity.NapsterApiSearchResponse;
import com.fjoglar.lyricly.data.source.remote.entity.OvhLyricsApiResponse;
import com.fjoglar.lyricly.data.source.remote.entity.Track;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class SongsRemoteRepository implements SongsRemoteDataSource {

    @Nullable
    private static volatile SongsRemoteRepository INSTANCE;

    private final NapsterService mNapsterService;
    private final OvhLyricsService mOvhLyricsService;

    // Prevent direct instantiation.
    private SongsRemoteRepository() {
        mNapsterService = NapsterService.retrofit().create(NapsterService.class);
        mOvhLyricsService = OvhLyricsService.retrofit().create(OvhLyricsService.class);
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @return the {@link SongsRemoteRepository} instance
     */
    public static SongsRemoteRepository getInstance() {
        if (INSTANCE == null) {
            synchronized (SongsRemoteRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SongsRemoteRepository();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance()} to create a new instance next
     * time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public List<Track> fetchTopSongs(int limit) {
        List<Track> topSongs = new ArrayList<>();
        Call<NapsterApiResponse> call =
                mNapsterService.getTopTracks(BuildConfig.NAPSTER_API_KEY, limit);
        try {
            NapsterApiResponse napsterApiResponse = call.execute().body();
            if (napsterApiResponse != null) {
                topSongs = napsterApiResponse.getTracks();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return topSongs;
    }

    @Override
    public Track searchCurrentlyPlayingSong(String query) {
        Track searchedSong = null;
        List<Track> searchedSongs;
        Call<NapsterApiSearchResponse> call =
                mNapsterService.searchCurrentlyPlayingTrack(
                        BuildConfig.NAPSTER_API_KEY,
                        "track",
                        1,
                        query);
        try {
            NapsterApiSearchResponse napsterApiSearchResponse = call.execute().body();
            if (napsterApiSearchResponse != null) {
                searchedSongs = napsterApiSearchResponse.getSearch().getData().getTracks();
                searchedSong = searchedSongs.get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchedSong;
    }

    @Override
    public String fetchSongLyrics(String artist, String title) {
        String lyrics = null;
        Call<OvhLyricsApiResponse> call =
                mOvhLyricsService.getSongLyrics(artist, title);
        try {
            OvhLyricsApiResponse ovhLyricsApiResponse = call.execute().body();
            if (ovhLyricsApiResponse != null) {
                lyrics = ovhLyricsApiResponse.getLyrics();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lyrics;
    }
}