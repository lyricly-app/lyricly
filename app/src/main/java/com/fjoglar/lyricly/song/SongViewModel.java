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

package com.fjoglar.lyricly.song;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fjoglar.lyricly.data.SongsDataSource;
import com.fjoglar.lyricly.data.model.Song;
import com.fjoglar.lyricly.core.Injection;
import com.fjoglar.lyricly.core.SingleLiveEvent;
import com.fjoglar.lyricly.core.schedulers.SchedulerProvider;

import io.reactivex.disposables.CompositeDisposable;

class SongViewModel extends ViewModel {

    private final SongsDataSource mSongsDataSource;
    private final int mSongId;

    private final SingleLiveEvent<Void> mAddedToFavorites = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> mDeletedFromFavorites = new SingleLiveEvent<>();

    private final CompositeDisposable mDisposables = new CompositeDisposable();

    private final MutableLiveData<SongResponse> mResponse = new MutableLiveData<>();


    SongViewModel(SongsDataSource songsDataSource, int songId) {
        mSongsDataSource = songsDataSource;
        mSongId = songId;
    }

    @Override
    protected void onCleared() {
        mDisposables.clear();
    }

    LiveData<SongResponse> response() {
        return mResponse;
    }

    SingleLiveEvent<Void> addedToFavorites() {
        return mAddedToFavorites;
    }

    SingleLiveEvent<Void> deletedFromFavorites() {
        return mDeletedFromFavorites;
    }

    void getSong() {
        mDisposables.add(Injection.provideGetSongByIdUseCase().execute(mSongsDataSource, mSongId)
                .subscribeOn(SchedulerProvider.getInstance().io())
                .observeOn(SchedulerProvider.getInstance().ui())
                .subscribe(
                        song -> mResponse.setValue(SongResponse.success(song)),
                        throwable -> mResponse.setValue(SongResponse.error(throwable))
                )
        );
    }

    void onFavoriteClicked(Song song) {
        if (song.isFavorite()) {
            deleteFavorite(song);
        } else {
            saveFavorite(song);
        }
    }

    private void saveFavorite(Song song) {
        mDisposables.add(Injection.provideAddSongToFavoriteUseCase().execute(mSongsDataSource, song)
                .subscribeOn(SchedulerProvider.getInstance().io())
                .observeOn(SchedulerProvider.getInstance().ui())
                .subscribe(mAddedToFavorites::call,
                        throwable -> Log.d("SongViewModel", throwable.toString())));
    }

    private void deleteFavorite(Song song) {
        mDisposables.add(Injection.provideDeleteSongFromFavoriteUseCase().execute(mSongsDataSource, song)
                .subscribeOn(SchedulerProvider.getInstance().io())
                .observeOn(SchedulerProvider.getInstance().ui())
                .subscribe(mDeletedFromFavorites::call,
                        throwable -> Log.d("SongViewModel", throwable.toString())));
    }
}
