package ch.enyo.openclassrooms.comeToEat.ui.selections

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import ch.enyo.openclassrooms.comeToEat.R
import ch.enyo.openclassrooms.comeToEat.databinding.FragmentRecipeDetailBinding
import ch.enyo.openclassrooms.comeToEat.ui.main.MainActivity
import ch.enyo.openclassrooms.comeToEat.utils.SelectedRecipe
import ch.enyo.openclassrooms.comeToEat.utils.updateRecipeParticipantList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SelectedRecipeDetail : Fragment() {

    companion object {
        private const val TAG :String ="SelectedRecipeDetail"
        fun newInstance() =
            SelectedRecipeDetail()
    }

    private val selectionViewModel by activityViewModels<SelectionViewModel>()
    private lateinit var recipeDetailBinding:FragmentRecipeDetailBinding
    private lateinit var webView: WebView
    private lateinit var mSelectedRecipe: SelectedRecipe

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       // return inflater.inflate(R.layout.fragment_recipe_detail, container, false)
        recipeDetailBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.fragment_recipe_detail,container,false)
        webView= recipeDetailBinding.webView
        webView.webViewClient= WebViewClient()

        recipeDetailBinding.recipeDetailButton.setOnClickListener{subscribeToInvitation()}

        return recipeDetailBinding.root
    }





    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Log.d(TAG, " in activity created")

        selectionViewModel.getSelectedSelectedRecipe().observe(viewLifecycleOwner, Observer { selectedRecipe ->updateUI(selectedRecipe!!) })


    }

    private fun updateUI(selectedRecipe: SelectedRecipe){
        Log.d(TAG, "Selected Recipe $selectedRecipe")
        mSelectedRecipe =selectedRecipe
        webView.loadUrl(selectedRecipe.recipeUrl)
    }

    private fun subscribeToInvitation(){

        updateRecipeParticipantList(mSelectedRecipe.recipeId,getCurrentUser()!!.uid).addOnSuccessListener {
            Toast.makeText(context,"Subscription success",Toast.LENGTH_LONG).show()
        }.addOnFailureListener { exception -> Log.d(TAG, exception.localizedMessage) } }



    private fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        // This callback will only be called when MyFragment is at least Started.
        /*val callback  = requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(false){
            override fun handleOnBackPressed() {
                findNavController().popBackStack(R.id.selectionFragment, true)
            }

        })
        activity!!.onBackPressedDispatcher.addCallback {  }*/



        // The callback can be enabled or disabled here or in the lambda
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        (context as MainActivity).navView.visibility=View.GONE
        menu.clear()
    }

     fun configureToolbar() {

        val ab = (activity as AppCompatActivity).supportActionBar
        ab?.setDisplayHomeAsUpEnabled(true)
    }



}
