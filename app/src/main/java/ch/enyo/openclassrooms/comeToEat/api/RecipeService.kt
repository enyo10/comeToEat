package ch.enyo.openclassrooms.comeToEat.api
import ch.enyo.openclassrooms.comeToEat.BuildConfig
import ch.enyo.openclassrooms.comeToEat.models.Result


import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.QueryMap


interface RecipeService {


    companion object{
       /* @JvmStatic
       var retrofit :Retrofit= Retrofit.Builder()
            .baseUrl("https://api.edamam.com/")
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
                )
            )
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()*/
      @JvmStatic
        var retrofit:Retrofit = Retrofit.Builder()
            .baseUrl("https://api.edamam.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }


    @GET("search")
    fun getRecipes(@QueryMap filters: Map<String, String>): Observable<Result>


    @GET("search?q=chicken&app_id=def9003a&app_key="+BuildConfig.api_key)
    fun getRecipesResult():Observable<Result>





}