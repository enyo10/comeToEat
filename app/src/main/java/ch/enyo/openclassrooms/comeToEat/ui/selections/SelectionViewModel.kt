package ch.enyo.openclassrooms.comeToEat.ui.selections

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.enyo.openclassrooms.comeToEat.utils.SelectedRecipe

class SelectionViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    private  val selectedSelectedRecipe:MutableLiveData<SelectedRecipe?> = MutableLiveData()

    fun setSelectedSelectedRecipe(selectedRecipe: SelectedRecipe){
        selectedSelectedRecipe.value=selectedRecipe

    }

    fun getSelectedSelectedRecipe():MutableLiveData<SelectedRecipe?>{
        return selectedSelectedRecipe
    }
}