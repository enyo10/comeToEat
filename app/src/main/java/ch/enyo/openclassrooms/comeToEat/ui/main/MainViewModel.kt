package ch.enyo.openclassrooms.comeToEat.ui.main

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

    val queryMap: MutableLiveData<MutableMap<String,String>> by lazy {
        MutableLiveData<MutableMap<String,String>>()
    }


    private var recipeQueryMap :MutableLiveData<MutableMap<String,String>> = MutableLiveData()

    fun setRecipeQueryMap(map: MutableMap<String,String>){
        recipeQueryMap.value=map


    }
    fun getRecipeQueryMap():MutableLiveData<MutableMap<String, String>>{
        if(recipeQueryMap.value==null){
            val map = mutableMapOf<String,String>()
            map["q"]= "Fish"
            recipeQueryMap.value=map
        }

       return recipeQueryMap
    }






}