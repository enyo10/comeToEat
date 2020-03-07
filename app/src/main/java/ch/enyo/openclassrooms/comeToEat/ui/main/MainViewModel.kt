package ch.enyo.openclassrooms.comeToEat.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.enyo.openclassrooms.comeToEat.models.Result

class MainViewModel: ViewModel() {
    companion object{
        private const val TAG: String ="MainViewModel"
    }


   /* val searchBasis: MutableLiveData<String> by lazy {
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
    }*/


    private var recipeQueryMap :MutableLiveData<MutableMap<String,String>> = MutableLiveData()

    fun setRecipeQueryMap(map: MutableMap<String,String>){
        Log.d(TAG, " map set --- $map")
        recipeQueryMap.value=map


    }
    fun getRecipeQueryMap():MutableLiveData<MutableMap<String, String>>{
        if(recipeQueryMap.value==null){
            val map = mutableMapOf<String,String>()
            map["q"]= "Fish"
            map["from"]= "0"
            map["to"]= "20"
            recipeQueryMap.value=map
        }

       return recipeQueryMap
    }


}