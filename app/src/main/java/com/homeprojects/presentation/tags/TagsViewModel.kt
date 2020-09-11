package com.homeprojects.presentation.tags

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TagsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Tags"
    }
    val text: LiveData<String> = _text
}