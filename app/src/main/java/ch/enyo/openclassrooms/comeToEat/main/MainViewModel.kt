package ch.enyo.openclassrooms.comeToEat.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.enyo.openclassrooms.comeToEat.models.Result

class MainViewModel: ViewModel() {


    val searchBasis: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }


    val maxIngredient: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val requestResult: MutableLiveData<Result> by lazy {
        MutableLiveData<Result>()
    }

    val queryMap: MutableLiveData<Map<String,String>> by lazy {
        MutableLiveData<Map<String,String>>()
    }
}