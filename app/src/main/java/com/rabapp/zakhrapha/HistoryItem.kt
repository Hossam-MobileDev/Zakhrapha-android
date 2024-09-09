package com.rabapp.zakhrapha

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "history_table")
class HistoryItem(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var textContent: String,
    var date: String
)