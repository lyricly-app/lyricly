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

package com.fjoglar.lyricly.data;

import androidx.annotation.Nullable;

import com.fjoglar.lyricly.data.model.Song;

import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class SongsRepository implements SongsDataSource.LocalDataSource, SongsDataSource.PreferencesDataSource {

    @Nullable
    private static SongsRepository INSTANCE = null;

    private final SongsDataSource.LocalDataSource mSongsLocalDataSource;
    private final SongsDataSource.PreferencesDataSource mPreferencesDataSource;

    private SongsRepository(SongsDataSource.LocalDataSource songsLocalDataSource,
                            SongsDataSource.PreferencesDataSource preferencesDataSource) {
        mSongsLocalDataSource = songsLocalDataSource;
        mPreferencesDataSource = preferencesDataSource;
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param songsLocalDataSource the backend data source
     * @return the {@link SongsRepository} instance
     */
    public static SongsRepository getInstance(SongsDataSource.LocalDataSource songsLocalDataSource,
                                              SongsDataSource.PreferencesDataSource preferencesDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new SongsRepository(songsLocalDataSource, preferencesDataSource);
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link  #getInstance(SongsDataSource.LocalDataSource, SongsDataSource.PreferencesDataSource)}
     * to create a new instance next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void saveSongs(List<Song> songs) {
        mSongsLocalDataSource.saveSongs(songs);
    }

    @Override
    public void saveSong(Song song) {
        mSongsLocalDataSource.saveSong(song);
    }

    @Override
    public Flowable<List<Song>> getTopSongs() {
        return mSongsLocalDataSource.getTopSongs();
    }

    @Override
    public Flowable<List<Song>> getRecentSongs() {
        return mSongsLocalDataSource.getRecentSongs();
    }

    @Override
    public Flowable<List<Song>> getFavoriteSongs() {
        return mSongsLocalDataSource.getFavoriteSongs();
    }

    @Override
    public Flowable<Song> getSongById(int id) {
        return mSongsLocalDataSource.getSongById(id);
    }

    @Override
    public Song getTopSongByNapsterId(String napsterId) {
        return mSongsLocalDataSource.getTopSongByNapsterId(napsterId);
    }

    @Override
    public Song getFavoriteSongByNapsterId(String napsterId) {
        return mSongsLocalDataSource.getFavoriteSongByNapsterId(napsterId);
    }

    @Override
    public void updateTopSongOrder(int id, int order, Date createdAt) {
        mSongsLocalDataSource.updateTopSongOrder(id, order, createdAt);
    }

    @Override
    public Completable updateFavoriteSong(Song song) {
        return mSongsLocalDataSource.updateFavoriteSong(song);
    }

    @Override
    public void updateFavoriteSongByNapsterId(String napsterId) {
        mSongsLocalDataSource.updateFavoriteSongByNapsterId(napsterId);
    }

    @Override
    public void deleteTopSongs() {
        mSongsLocalDataSource.deleteTopSongs();
    }

    @Override
    public void deleteOldTopSongs(Date date) {
        mSongsLocalDataSource.deleteOldTopSongs(date);
    }

    @Override
    public Completable deleteFavoriteSong(Song song) {
        return mSongsLocalDataSource.deleteFavoriteSong(song);
    }

    @Override
    public long getLastUpdatedTimeInMillis() {
        return mPreferencesDataSource.getLastUpdatedTimeInMillis();
    }

    @Override
    public void setLastUpdatedTimeInMillis() {
        mPreferencesDataSource.setLastUpdatedTimeInMillis();
    }
}