package ch.enyo.openclassrooms.comeToEat.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import ch.enyo.openclassrooms.comeToEat.api.RecipeStream
import ch.enyo.openclassrooms.comeToEat.models.Recipe
import ch.enyo.openclassrooms.comeToEat.models.Result
import ch.enyo.openclassrooms.comeToEat.ui.recipes.RecipesFragment
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import java.util.*

class RecipeRepository {

    companion object{
        private var newsRepository: RecipeRepository? = null

        fun getInstance(): RecipeRepository? {
            if (newsRepository == null) {
                newsRepository =
                    RecipeRepository()
            }
            return newsRepository
        }
    }
    private var myCompositeDisposable: Disposable? = null


    fun getRecipes(mutableMap: MutableMap<String,String>): MutableLiveData<ArrayList<Recipe>>{

        val mutableLiveData : MutableLiveData<ArrayList<Recipe>> =MutableLiveData()

     myCompositeDisposable =   RecipeStream.getRecipeResult(mutableMap).subscribeWith(object : DisposableObserver<Result>() {

            override fun onNext(result: Result) {

                Log.d(RecipesFragment.TAG, " Update UI method call ")
                val recipes :ArrayList<Recipe> = ArrayList()
                val index: Int=result.to-1

                for (value in result.from..index){
                    recipes.add(result.hits[value].recipe)

                    Log.d(RecipesFragment.TAG, " value in update ${recipes[value]}")
                    Log.d(RecipesFragment.TAG, " Hits size in update : ${result.hits.size}")
                }
                mutableLiveData.value=recipes

            }

            override fun onError(e: Throwable) {
                Log.i("TAG", "aie, error in recipe search: " + Log.getStackTraceString(e)
                )
            }

            override fun onComplete() {
                Log.i(RecipesFragment.TAG, " Recipes  downloaded ")

            }

        })

        return mutableLiveData

    }


}