package ch.enyo.openclassrooms.comeToEat.ui.selections

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.enyo.openclassrooms.comeToEat.R
import ch.enyo.openclassrooms.comeToEat.databinding.SelectedRecipeDetailFragmentBinding
import ch.enyo.openclassrooms.comeToEat.models.User
import ch.enyo.openclassrooms.comeToEat.ui.friends.FriendsAdapter
import ch.enyo.openclassrooms.comeToEat.ui.friends.FriendsFragment
import ch.enyo.openclassrooms.comeToEat.ui.main.MainActivity
import ch.enyo.openclassrooms.comeToEat.utils.SelectedRecipe
import ch.enyo.openclassrooms.comeToEat.utils.updateRecipeParticipantList
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SelectedRecipeDetail : FriendsFragment() {

    companion object {
        private const val TAG :String ="SelectedRecipeDetail"
        fun newInstance() =
            SelectedRecipeDetail()
    }

    private val selectionViewModel by activityViewModels<SelectionViewModel>()
    private lateinit var selectedRecipeDetailFragmentBinding:SelectedRecipeDetailFragmentBinding
    private lateinit var mSelectedRecipe: SelectedRecipe
    private  var participantsList : ArrayList<User> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        selectedRecipeDetailFragmentBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.selected_recipe_detail_fragment,container,false)

        selectedRecipeDetailFragmentBinding.choosingButton.setOnClickListener { subscribeToInvitation() }

        initRecyclerView()
        loadData()

        return selectedRecipeDetailFragmentBinding.root
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Log.d(TAG, " in activity created")
        selectionViewModel.getSelectedSelectedRecipe().observe(viewLifecycleOwner, Observer { selectedRecipe ->updateUIWithSelectedRecipe(selectedRecipe!!) })


    }

    override fun initRecyclerView() {
        val recyclerView: RecyclerView = selectedRecipeDetailFragmentBinding.recyclerView
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager

        friendsAdapter = FriendsAdapter(this, users)

        recyclerView.adapter = friendsAdapter
    }


    private fun updateUIWithSelectedRecipe(selectedRecipe: SelectedRecipe){
        Log.d(TAG, "Selected Recipe $selectedRecipe")
        mSelectedRecipe =selectedRecipe

        selectedRecipe.image?.let {
            loadImage( selectedRecipeDetailFragmentBinding.selectedRecipeImage,
                it
            )
        }
        selectedRecipeDetailFragmentBinding.selectedRecipeName.text=selectedRecipe.recipeLabel
        selectedRecipeDetailFragmentBinding.selectedRecipeDate.text=
            SimpleDateFormat("dd/MM/yyyy", Locale.US).format(selectedRecipe.date)



    }

    override fun updateUI(list: ArrayList<User>) {
        for(user in list){
            if (mSelectedRecipe.participants?.contains(user.userId)!!)
                participantsList.add(user)
        }
        super.updateUI(participantsList)
        Log.d(TAG," participant --  $participantsList")
    }

    private fun subscribeToInvitation(){
        val date = Calendar.getInstance().time
        val isValue: Boolean = date.after(mSelectedRecipe.date)
        when {
            mSelectedRecipe.participants?.contains(getCurrentUser()!!.uid)!! -> {
                Toast.makeText(context," You are the Host, :-), are you forger ? aïe,aïe",Toast.LENGTH_LONG).show()
            }
            isValue -> {
                Toast.makeText(context,"  :-) , to late now",Toast.LENGTH_LONG).show()
            }
            else -> {

                updateRecipeParticipantList(mSelectedRecipe.recipeId,getCurrentUser()!!.uid).addOnSuccessListener {
                    Toast.makeText(context,"Subscription success",Toast.LENGTH_LONG).show()
                    loadData()
                }.addOnFailureListener { exception -> Log.d(TAG, exception.localizedMessage) } }
        }
    }

    private fun loadImage(v: ImageView, image:String){

        Glide.with(v.context)
            .load(image)
            .apply(RequestOptions.centerCropTransform())
            .into(v)
    }

    private fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

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
