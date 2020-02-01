package ch.enyo.openclassrooms.comeToEat.ui.friends
import ch.enyo.openclassrooms.comeToEat.models.Result

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ch.enyo.openclassrooms.comeToEat.R
import ch.enyo.openclassrooms.comeToEat.api.RecipeStream
import ch.enyo.openclassrooms.comeToEat.base.BaseFragment
import ch.enyo.openclassrooms.comeToEat.ui.recipes.RecipesFragment
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver

class FriendsFragment : BaseFragment() {

    companion object{
        const val TAG="FriendsFragment"
    }

    private lateinit var notificationsViewModel: FriendsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loadData()
        notificationsViewModel =
            ViewModelProvider(this).get(FriendsViewModel::class.java)

        val root = inflater.inflate(getFragmentLayout(), container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        notificationsViewModel.text.observe(this, Observer {
            textView.text = it
        })
        observeAuthenticationState()
        return root
    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_friends
    }

    private fun loadData(){
        var disposable : Disposable =
            RecipeStream.getSecond().subscribeWith(object : DisposableObserver<Result>() {
                override fun onNext(result: Result) {
                    Log.i(TAG, " Recipe list 2 downloading...")
                    Log.i(TAG, " Recipe list 2 size: " +result.to)
                    Log.i(TAG, " from"+result.from)
                    Log.i(TAG,"hits "+result.hits)
                    Log.i(TAG,"result to string $result")

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