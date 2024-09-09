package com.rabapp.zakhrapha

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface HistoryDao {
    @Insert
    fun insert(historyItem: HistoryItem)

    @Query("SELECT * FROM history_table")
    fun allHistoryItems(): LiveData<List<HistoryItem>>

    @Delete
    fun delete(historyItem: HistoryItem)

    @Query("DELETE FROM history_table")
    suspend fun deleteAll()
}