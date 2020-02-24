package ch.enyo.openclassrooms.comeToEat.api
import ch.enyo.openclassrooms.comeToEat.models.Result

import android.util.Log
import ch.enyo.openclassrooms.comeToEat.BuildConfig
import io.reactivex.Observable

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class RecipeStream {


    companion object {

        private const val TAG: String = "RecipeStream"

        @JvmStatic
        fun getRecipeResult(param: MutableMap<String, String>): Observable<Result> {
            param["app_id"]= "def9003a"
            param["app_key"]= BuildConfig.api_key
            param["from"]= "0"
            param["to"]= "20"

            val recipeService: RecipeService =
                RecipeService.retrofit.create(RecipeService::class.java)
            Log.d(TAG, " $param")

            return recipeService.getRecipes(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(15, TimeUnit.SECONDS)


        }



        /*fun getSecond():Observable<Result>{
            val recipeService: RecipeService =
                RecipeService.retrofit.create(RecipeService::class.java)


            return recipeService.getRecipesResult()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(15, TimeUnit.SECONDS)

        }*/
    }


}