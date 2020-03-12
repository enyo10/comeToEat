package ch.enyo.openclassrooms.comeToEat.ui.search


import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchDialogViewModel : ViewModel() {
    companion object {
        private const val TAG: String = "SearchDialogViewModel"
    }


   private val searchBasis: MutableLiveData<String> = MutableLiveData()
    fun getSearchBasis():MutableLiveData<String>{
        return searchBasis
    }
    fun setSearchBasis(basis:String){
        searchBasis.value=basis
    }


    private val  healthLabel: MutableLiveData<String> = MutableLiveData<String>()
    fun getHealthLabel(): MutableLiveData<String>{
        Log.d(TAG, " health : ${healthLabel.value}")
        return healthLabel
    }
    fun setHealthLabel(label:String){
        healthLabel.value = label
    }


    private val dietType: MutableLiveData<String> = MutableLiveData()
    fun getDietType(): MutableLiveData<String>{
        Log.d(TAG, " diet : $dietType.value")
        return dietType
    }
    fun setDietType(diet: String){
        dietType.value=diet
    }


}
