package ch.enyo.openclassrooms.comeToEat.ui.recipes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.enyo.openclassrooms.comeToEat.models.Recipe
import ch.enyo.openclassrooms.comeToEat.repositories.RecipeRepository

class RecipesViewModel : ViewModel() {
    companion object{
        const val TAG :String ="RecipesViewModel"
    }

    private var recipes:MutableLiveData<ArrayList<Recipe>> = MutableLiveData<ArrayList<Recipe>>()



    private var recipeRepository: RecipeRepository = RecipeRepository.getInstance()!!

    fun getRecipes():MutableLiveData<ArrayList<Recipe>>{
        return recipes
    }


    private  val selectedRecipe:MutableLiveData<Recipe?> = MutableLiveData()

    fun setSelectedRecipe(recipe: Recipe){
        selectedRecipe.value=recipe

    }


    fun getSelectedRecipe():MutableLiveData<Recipe?>{
        return selectedRecipe
    }


    private var recipeQueryMap :MutableLiveData<MutableMap<String,String>> = MutableLiveData()

    fun setRecipeQueryMap(map: MutableMap<String,String>){
        Log.d(TAG, " Query map set ---")
        recipeQueryMap.value=map
       recipes= recipeRepository.getRecipes(getRecipeQueryMap().value!!)


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