package com.rabapp.zakhrapha

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favItem: FavoriteItem): Long

    @Query("SELECT * FROM favorite_table") // Ensure table name matches
    fun allfavItems(): LiveData<List<FavoriteItem>>

    @Delete
    suspend fun delete(fav: FavoriteItem): Int

    @Query("SELECT * FROM favorite_table WHERE text = :text")
    suspend fun getFavoriteItemByText(text: String): FavoriteItem?
  /*  @Query("SELECT * FROM favorite_table WHERE text = :text LIMIT 1")
    suspend fun getFavoriteItemByText(text: String): String*/
}