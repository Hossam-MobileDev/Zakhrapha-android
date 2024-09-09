package com.rabapp.zakhrapha

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _favoriteItems = MutableLiveData<MutableList<String>>()
  //  private val _favoriteItems = MutableLiveData<List<String>>()

    // Public LiveData for observing favorite items
    val favoriteItems: MutableLiveData<MutableList<String>> get() = _favoriteItems

    // Function to add a favorite item
    fun addFavorite(item: String) {
        _favoriteItems.value?.let {
            if (!it.contains(item)) {
                it.add(item)
                _favoriteItems.value = it
            }
        }
    }

    // Function to remove a favorite item
   /* fun removeFavorite(item: String) {
        _favoriteItems.value?.let {
            if (it.contains(item)) {
                it.remove(item)
                _favoriteItems.value = it
            }
        }
    }*/
}