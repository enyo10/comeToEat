package ch.enyo.openclassrooms.comeToEat.ui.recipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.enyo.openclassrooms.comeToEat.models.Recipe

class RecipesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

   private  val selectedRecipe:MutableLiveData<Recipe?> = MutableLiveData<Recipe?>()

    fun setSelectedRecipe(recipe: Recipe){
        selectedRecipe.value=recipe

    }


    fun getSelectedRecipe():MutableLiveData<Recipe?>{
        return selectedRecipe
    }



    val searchBasis: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }


    val maxIngredient: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }


}