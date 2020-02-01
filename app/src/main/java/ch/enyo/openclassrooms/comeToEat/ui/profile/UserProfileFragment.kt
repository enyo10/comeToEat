package ch.enyo.openclassrooms.comeToEat.ui.profile
import ch.enyo.openclassrooms.comeToEat.models.Result

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels

import ch.enyo.openclassrooms.comeToEat.R
import ch.enyo.openclassrooms.comeToEat.api.RecipeStream
import ch.enyo.openclassrooms.comeToEat.ui.recipes.RecipesFragment
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver

class UserProfileFragment : Fragment() {



    companion object {
        fun newInstance() = UserProfileFragment()


    }

   // private lateinit var viewModel: UserProfileViewModel
   private val viewModel by viewModels<UserProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       // loadData()

        return inflater.inflate(R.layout.user_profile_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
       // viewModel = ViewModelProviders.of(this).get(UserProfileViewModel::class.java)

        // TODO: Use the ViewModel
    }

    private fun loadData(){
        var disposable :Disposable=RecipeStream.getSecond().subscribeWith(object : DisposableObserver<Result>() {
            override fun onNext(result: Result) {
                Log.i(RecipesFragment.TAG, " Recipe list 2 downloading...")
                Log.i(RecipesFragment.TAG, " Recipe list 2 size: " +result.to)
                Log.i(RecipesFragment.TAG, " from"+result.from)
                Log.i(RecipesFragment.TAG,"hits "+result.hits)
                Log.i(RecipesFragment.TAG,"result to string $result")

                // updateUIWithResult(placeDetailsList)
            }

            override fun onError(e: Throwable) {
                Log.i("TAG", "aie, error in recipe search: " + Log.getStackTraceString(e)
                )
            }

            override fun onComplete() {
                Log.i(
                    RecipesFragment.TAG,
                    " Recipes  downloaded "
                )
            }
        })



    }

}
