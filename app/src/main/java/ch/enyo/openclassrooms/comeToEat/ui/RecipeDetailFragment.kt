package ch.enyo.openclassrooms.comeToEat.ui


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer

import ch.enyo.openclassrooms.comeToEat.R
import ch.enyo.openclassrooms.comeToEat.databinding.FragmentRecipeDetailBinding
import ch.enyo.openclassrooms.comeToEat.models.Recipe
import ch.enyo.openclassrooms.comeToEat.models.User
import ch.enyo.openclassrooms.comeToEat.utils.saveSelectedRecipe
import ch.enyo.openclassrooms.comeToEat.ui.recipes.RecipesViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ch.enyo.openclassrooms.comeToEat.utils.getUser
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentSnapshot

/**
 * A simple [Fragment] subclass.
 */
class RecipeDetailFragment : Fragment() {

    companion object{
        const val TAG: String="RecipeDetailFragment"
    }

    private lateinit var recipeDetailBinding: FragmentRecipeDetailBinding

    private lateinit var webView: WebView
    private val recipesViewModel by activityViewModels<RecipesViewModel>()
    private lateinit var mRecipe: Recipe
    private lateinit var mUser: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        recipeDetailBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.fragment_recipe_detail,container,false)

       webView= recipeDetailBinding.webView

        webView.webViewClient= WebViewClient()

        observeSelectedRecipe()
        recipeDetailBinding.recipeDetailButton.setOnClickListener(View.OnClickListener {
            insertRecipeToFireBase()
        })

        return  recipeDetailBinding.root
    }


    private fun updateUI(recipe: Recipe){
        mRecipe= recipe
        webView.loadUrl(recipe.url)

    }

   private fun observeSelectedRecipe(){
        recipesViewModel.getSelectedRecipe().observe(viewLifecycleOwner, Observer {
            recipe ->updateUI(recipe!!)
        })
    }

    private fun getUserId(): String? {
        return if (getCurrentUser() != null) getCurrentUser()!!.uid else null
    }

    private fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    private fun insertRecipeToFireBase(){
        val userId= getUserId()

        getUser(userId!!)?.addOnSuccessListener(OnSuccessListener<DocumentSnapshot?> {
            documentSnapshot ->  val user: User?= documentSnapshot?.toObject(User::class.java)
            Log.d(TAG, " user : $user")
            saveSelectedRecipe(user!!.userId!!,mRecipe)


        })


    }


}
