package com.app.fundamentalsubmission.di.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavorite(favoriteEvent: FavoriteEvent)

    @Delete
    fun deleteFavorite(favoriteEvent: FavoriteEvent)

    @Query("SELECT * FROM favoriteevent")
    fun getAllFavorite(): LiveData<List<FavoriteEvent>>

    @Query("SELECT * FROM favoriteevent WHERE id = :id")
    fun getFavoriteById(id: Int): LiveData<FavoriteEvent>
}