package ch.enyo.openclassrooms.comeToEat.ui.recipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ch.enyo.openclassrooms.comeToEat.R
import ch.enyo.openclassrooms.comeToEat.auth.LoginViewModel
import ch.enyo.openclassrooms.comeToEat.databinding.FragmentRecipesBinding
import ch.enyo.openclassrooms.comeToEat.ui.main.MainActivity
import ch.enyo.openclassrooms.comeToEat.ui.main.MainViewModel
import ch.enyo.openclassrooms.comeToEat.models.Recipe
import ch.enyo.openclassrooms.comeToEat.models.User
import ch.enyo.openclassrooms.comeToEat.ui.profile.UserProfileViewModel
import ch.enyo.openclassrooms.comeToEat.ui.search.SearchDialog
import ch.enyo.openclassrooms.comeToEat.utils.getUser
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.disposables.Disposable


class RecipesFragment : Fragment() {

    companion object{
        const val TAG= "RecipesFragment"
    }

    private lateinit var binding: FragmentRecipesBinding
    private var myCompositeDisposable: Disposable? = null
    private lateinit var mRecipeAdapter:RecipesAdapter
    private  var mMap: MutableMap<String,String> = mutableMapOf()
    private var mRecipes: ArrayList<Recipe> = arrayListOf()


    // Get a reference to the ViewModel scoped to this Fragment
    private val viewModel by activityViewModels<LoginViewModel>()
    private val mainViewModel by activityViewModels<MainViewModel>()

     val recipesViewModel by activityViewModels<RecipesViewModel>()

    private var myStaticValue =0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.fragment_recipes,container,false)


        observeAuthenticationState()
        initRecyclerView()
        initRefreshLayout()


        binding.searchButton.setOnClickListener {

            initAndShowSearchDialog()
        }

        mainViewModel.getRecipeQueryMap().observe(this,Observer<MutableMap<String,String>>{
                map:MutableMap<String,String>  -> updateQueryMapAndGetRecipes(map)
        })

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

       /* mainViewModel.getRecipeQueryMap().observe(this,Observer<MutableMap<String,String>>{
            map:MutableMap<String,String>  -> updateQueryMapAndGetRecipes(map)
        })*/


    }

    private fun initRecyclerView(){
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL

        binding.recipesRecyclerView.layoutManager = linearLayoutManager
        mRecipeAdapter = RecipesAdapter(this,mRecipes )
        binding.recipesRecyclerView.adapter = mRecipeAdapter

    }

    private fun initRefreshLayout() {
        binding.recipesSwipeRefreshLayout.setOnRefreshListener {

          if  (binding.recipesProgressBar.visibility==View.VISIBLE)
                                binding.recipesProgressBar.visibility=View.GONE
            myStaticValue +=20

            var from = myStaticValue
            var  to = from +20

            Log.d(TAG, " to --- $to")

            Log.d(TAG, " from --- $from")

            mMap["from"]= "$from"
            mMap["to"]= "$to"

            updateWithSwipeRefresh(mMap)
            Log.d(TAG, " map --- $mMap")

            binding.recipesSwipeRefreshLayout.isRefreshing = false
        }
    }

    private fun updateQueryMapAndGetRecipes(map: MutableMap<String, String>){
        binding.recipesProgressBar.visibility=View.VISIBLE
        this.mMap = map

        recipesViewModel.getRecipes(map).observe(
            this,
            Observer<ArrayList<Recipe>> {
                list: ArrayList<Recipe> -> updateRecipes(list)
            }

        )
      if(binding.recipesProgressBar.visibility==View.VISIBLE)
        binding.recipesProgressBar.visibility=View.INVISIBLE

    }

    private fun updateWithSwipeRefresh(map: MutableMap<String, String>){
        recipesViewModel.getRecipes(map).observe(
            this,
            Observer<ArrayList<Recipe>> {
                    list: ArrayList<Recipe> -> updateRecipes(list)
            }
        )
    }


    private fun updateRecipes(list:ArrayList<Recipe>){
        Log.d(TAG, " update list: list size ${list.size}")
        this.mRecipes.clear()
        this.mRecipes.addAll(list)
        mRecipeAdapter.notifyDataSetChanged()
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
                    getConnectedFromFireBase()

                }
                else -> {
                    Log.i(TAG, "User not AUTHENTICATED")
                    findNavController().navigate(R.id.loginFragment)

                }
            }
        })
    }


    fun navigateToDetailsFragment(){
        findNavController().navigate(R.id.recipeDetailFragment)
    }


  /* private fun loadRecipeData(map:MutableMap<String,String>){


       "https://api.edamam.com/search?" +"q=meal" + "&app_id=def9003a" + "&app_key=5afe494e2a6ed914cb7f64154b6e0203" + "&from=0" + "&to=20"

      // val map: Map<String, String> = ImmutableMap.of("q","meal","app_id", "def9003a","app_key","5afe494e2a6ed914cb7f64154b6e0203")

        myCompositeDisposable = RecipeStream.getRecipeResult(map)

            .subscribeWith(object : DisposableObserver<Result>() {
               override fun onNext(result: Result) {

                    updateUIWithResult(result)
                }

                override fun onError(e: Throwable) {
                    Log.i("TAG", "aie, error in recipe search: " + Log.getStackTraceString(e)
                    )
                }

                override fun onComplete() {
                    Log.i(TAG, " Recipes  downloaded ")
                    *//*if(binding.recipesProgressBar.visibility==View.VISIBLE)
                    binding.recipesProgressBar.visibility=View.GONE*//*
                }

            })

    }*/

    /**
     * This method to init and show search dialog fragment.
     */
    private fun initAndShowSearchDialog(){
        val fm =childFragmentManager
        val mySearchDialogB=SearchDialog.newInstance()
        mySearchDialogB.show(fm,"My Search dialogB")

    }

    override fun onDestroy() {
        super.onDestroy()

        myCompositeDisposable?.dispose()

    }


    private fun getConnectedFromFireBase(){
                     /* getUser(getCurrentUser()!!.uid)!!.addOnSuccessListener {
                          documentSnapshot -> setAuthenticatedUser(documentSnapshot!!.toObject(User::class.java) as User )}
                    */
       val viewModel= ViewModelProvider(this).get(UserProfileViewModel::class.java)

        getUser(getCurrentUser()!!.uid).addOnSuccessListener { document ->
            if (document != null) {
                Log.d(TAG, "DocumentSnapshot usr data: ${document.data}")
               viewModel.connectedUser.value= (document.toObject(User::class.java) as User)
            } else {
                Log.d(TAG, "No such document")
            }
        }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }


    private fun onFailureListener(): OnFailureListener? {
        return OnFailureListener { e: Exception? ->
            Toast.makeText(context,getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show()
            e?.printStackTrace()
        }
    }

    private fun setAuthenticatedUser(user: User){
        Log.d(TAG, " authenticated user $user")
        (context as MainActivity).connectedUser=user

    }

    private fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

}