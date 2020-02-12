package ch.enyo.openclassrooms.comeToEat.ui.recipes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ch.enyo.openclassrooms.comeToEat.R
import ch.enyo.openclassrooms.comeToEat.api.RecipeStream
import ch.enyo.openclassrooms.comeToEat.auth.LoginViewModel
import ch.enyo.openclassrooms.comeToEat.databinding.FragmentRecipesBinding
import ch.enyo.openclassrooms.comeToEat.main.MainViewModel
import ch.enyo.openclassrooms.comeToEat.models.Recipe
import ch.enyo.openclassrooms.comeToEat.models.Result
import ch.enyo.openclassrooms.comeToEat.ui.search.SearchDialog
import com.google.common.collect.ImmutableMap
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver


class RecipesFragment : Fragment() {

    companion object{
        const val TAG= "RecipesFragment"
    }



    private lateinit var binding: FragmentRecipesBinding

    private var myCompositeDisposable: Disposable? = null
    private lateinit var mRecipeAdapter:RecipesAdapter


    // Get a reference to the ViewModel scoped to this Fragment
   // private val viewModel by viewModels<LoginViewModel>()
    private val viewModel by activityViewModels<LoginViewModel>()
    private val mainViewModel by activityViewModels<MainViewModel>()

  //  private val recipesViewModel = ViewModelProvider(this).get(RecipesViewModel::class.java)
     val recipesViewModel by activityViewModels<RecipesViewModel>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.fragment_recipes,container,false)

        observeAuthenticationState()

        initRecyclerView()

        binding.searchButton.setOnClickListener {
            // body.text=dateString
            initAndShowSearchDialog()
        }



       /* val textView: TextView = root.findViewById(R.id.text_home)

        recipesViewModel.text.observe(this, Observer {
            textView.text = it
       })*/
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mainViewModel.queryMap.observe(viewLifecycleOwner,Observer{
            map:Map<String,String> -> loadRecipeData(map)
        })
    }

    private fun initRecyclerView(){
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL

        binding.recipesRecyclerView.layoutManager = linearLayoutManager
        mRecipeAdapter = RecipesAdapter(this,ArrayList())
        binding.recipesRecyclerView.adapter = mRecipeAdapter

        Log.i(TAG,"Recycler view init success")

    }


    private fun updateUIWithResult(result: Result){


        Log.d(TAG, " Update UI method call ")
        val recipes :ArrayList<Recipe> =ArrayList()
        val index: Int=result.to-1

        for (value in result.from..index){
            recipes.add(result.hits[value].recipe)

            Log.d(TAG, " value in update ${recipes[value]}")
            Log.d(TAG, " Hits size in update : ${result.hits.size}")
        }
        mRecipeAdapter.updateWithDate(recipes)

    }


    /**
     * Observes the authentication state and changes the UI accordingly.
     * If there is a logged in user: (1) show a logout button and (2) display their name.
     * If there is no logged in user: show a login button
     */
    private fun observeAuthenticationState() {

        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> {
                    Log.i(TAG,"Authentication success")

                   /* binding.authButton.setOnClickListener {
                        AuthUI.getInstance().signOut(requireContext())
                    }*/
                }
                else -> {
                    Log.i(TAG, "User not AUTHENTICATED")
                    findNavController().navigate(R.id.loginFragment)

                   /* binding.authButton.setOnClickListener {
                        launchSignInFlow()
                    }*/
                }
            }
        })
    }


    fun navigateToDetailsFragment(){
        findNavController().navigate(R.id.recipeDetailFragment)
    }


   private fun loadRecipeData(map:Map<String,String>){

       "https://api.edamam.com/search?" +"q=meal" + "&app_id=def9003a" + "&app_key=5afe494e2a6ed914cb7f64154b6e0203" + "&from=0" + "&to=20"

      // val map: Map<String, String> = ImmutableMap.of("q","meal","app_id", "def9003a","app_key","5afe494e2a6ed914cb7f64154b6e0203")

        myCompositeDisposable = RecipeStream.getRecipeResult(map)

            .subscribeWith(object : DisposableObserver<Result>() {
               override fun onNext(result: Result) {
                    Log.i(TAG, " Recipe list downloading...")
                   // Log.i(TAG, " Recipe list size: " +result.to)
                  //  Log.i(TAG, " from"+result.from)
                    Log.d(TAG,"hits size "+result.hits.size)
                   Log.i(TAG,"result to string:  $result")

                    updateUIWithResult(result)
                }

                override fun onError(e: Throwable) {
                    Log.i("TAG", "aie, error in recipe search: " + Log.getStackTraceString(e)
                    )
                }

                override fun onComplete() {
                    Log.i(
                        TAG,
                        " Recipes  downloaded "
                    )
                }
            })

    }

    /**
     * This method to init and show search dialog fragment.
     */
    private fun initAndShowSearchDialog(){
        val fm =childFragmentManager
       // val searchDialogFragment =SearchDialog()
      //  searchDialogFragment.show(fm,"Search dialog")
        val mySearchDialogB=SearchDialog()
        mySearchDialogB.show(fm,"My Search dialogB")

    }

    override fun onDestroy() {
        super.onDestroy()

        myCompositeDisposable?.dispose()

    }

}