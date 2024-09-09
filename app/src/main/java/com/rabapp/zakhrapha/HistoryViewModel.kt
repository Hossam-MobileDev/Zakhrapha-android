package com.rabapp.zakhrapha

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class HistoryViewModel(application: Application)
    : AndroidViewModel(application) {
    private val historyDao: HistoryDao = AppDatabase.getDatabase(application).historyDao()
    private val favoriteDao: FavoriteDao = AppDatabase.getDatabase(application).favoriteDao()

    val allHistoryItems: LiveData<List<HistoryItem>> = historyDao.allHistoryItems()
    val allFavoriteItems: LiveData<List<FavoriteItem>> = favoriteDao.allfavItems()
    private val _favoriteItems = MediatorLiveData<List<String>>()
    val favoriteItems: LiveData<List<String>> get() = _favoriteItems

    init {
        _favoriteItems.addSource(allFavoriteItems) { favorites ->
            _favoriteItems.value = favorites.map { it.text }
        }
    }

    fun toggleFavorite(item: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // Check if the item is already a favorite
            val existingFavorite = favoriteDao.getFavoriteItemByText(item)
            if (existingFavorite != null) {
                // If it is a favorite, remove it
                favoriteDao.delete(existingFavorite)
            } else {
                // If it is not a favorite, add it
                val newFavorite = FavoriteItem(text = item) // Replace with your actual fields
                favoriteDao.insert(newFavorite)
            }
        }
    }

    // Function to check if an item is a favorite
    fun isFavorite(item: String): Boolean {
        val favorites = favoriteItems.value
        return favorites?.contains(item) ?: false
    }

    // Function to delete a specific favorite item
    fun deleteFavoriteItem(item: FavoriteItem) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteDao.delete(item)
        }
    }

    // Function to insert a favorite item
    fun insertFavoriteItem(favItem: FavoriteItem) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteDao.insert(favItem)
        }
    }

    // Function to delete all history items
    fun deleteAllItems() {
        viewModelScope.launch(Dispatchers.IO) {
            historyDao.deleteAll()
        }
    }
    }


