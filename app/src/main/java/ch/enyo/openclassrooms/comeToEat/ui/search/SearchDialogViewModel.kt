package ch.enyo.openclassrooms.comeToEat.ui.search


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchDialogViewModel : ViewModel() {


   private val searchBasis: MutableLiveData<String> = MutableLiveData()
    fun getSearchBasis():MutableLiveData<String>{
        return searchBasis
    }
    fun setSearchBasis(basis:String){
        searchBasis.value=basis
    }


   private val maxIngredient: MutableLiveData<Int> = MutableLiveData<Int>()
   fun getMaxIngredient():MutableLiveData<Int>{
       return maxIngredient
    }
    fun setMaxIngredient(maxIng:Int){
        maxIngredient.value=maxIng
    }

   private val cuisineType: MutableLiveData<String> = MutableLiveData<String>()
    fun getCuisineType():MutableLiveData<String>{
        return cuisineType
    }
    fun setCuisineType(cuisine:String){
        cuisineType.value=cuisine
    }

    private val  mealType: MutableLiveData<String> = MutableLiveData<String>()
    fun getMealType(): MutableLiveData<String>{
        return mealType
    }
    fun setMealType(meal:String){
        mealType.value=meal
    }

    private val dishType: MutableLiveData<String> = MutableLiveData<String>()
    fun getDishType():MutableLiveData<String>{
        return dishType
    }
    fun setDishType(dish:String){
        dishType.value=dish
    }

    private val excluded: MutableLiveData<String> =MutableLiveData<String>()
    fun getExcluded():MutableLiveData<String>{
        return excluded
    }
    fun setExcluded(exc:String){
        excluded.value=exc
    }


}
