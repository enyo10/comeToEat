package ch.enyo.openclassrooms.comeToEat.ui.selections

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.enyo.openclassrooms.comeToEat.utils.SelectedRecipe
import ch.enyo.openclassrooms.comeToEat.utils.getSelectedRecipe

class SelectionViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    private  val selectedSelectedRecipe:MutableLiveData<SelectedRecipe?> = MutableLiveData()
    private val newSelectedRecipeId: MutableLiveData<String?> = MutableLiveData()

    fun setSelectedSelectedRecipe(selectedRecipe: SelectedRecipe){
        selectedSelectedRecipe.value=selectedRecipe

    }

    fun getSelectedSelectedRecipe():MutableLiveData<SelectedRecipe?>{
        return selectedSelectedRecipe
    }

    fun setNewSelectedRecipeId(recipeId:String){
        newSelectedRecipeId.value=recipeId
    }
    fun getNewSelectedRecipeId():MutableLiveData<String?>{

        return newSelectedRecipeId
    }



}