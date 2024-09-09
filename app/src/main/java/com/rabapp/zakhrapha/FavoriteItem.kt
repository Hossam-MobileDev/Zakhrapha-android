package com.rabapp.zakhrapha

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_table")
data class FavoriteItem(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var text: String
)