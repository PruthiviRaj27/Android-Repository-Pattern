package com.example.androidrepositorypattern.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.androidrepositorypattern.database.VideosDatabase
import com.example.androidrepositorypattern.database.asDomainModel
import com.example.androidrepositorypattern.domain.DevByteVideo
import com.example.androidrepositorypattern.network.DevByteNetwork
import com.example.androidrepositorypattern.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VideosRepository(private val database: VideosDatabase) {

    val videos: LiveData<List<DevByteVideo>> = Transformations.map(database.videoDao.getVideos()) {
        it.asDomainModel()
    }

    suspend fun refreshVideos() {
        withContext(Dispatchers.IO) {
            val playlist = DevByteNetwork.devbytes.getPlaylist()
            database.videoDao.insertAll(playlist.asDatabaseModel())
        }
    }

}

