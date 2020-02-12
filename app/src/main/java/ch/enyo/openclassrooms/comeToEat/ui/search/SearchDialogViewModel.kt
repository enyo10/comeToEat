package ch.enyo.openclassrooms.comeToEat.ui.search


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchDialogViewModel : ViewModel() {


    val searchBasis: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }


    val maxIngredient: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val cuisineType: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val mealType: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val dishType: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val excluded: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }


}
