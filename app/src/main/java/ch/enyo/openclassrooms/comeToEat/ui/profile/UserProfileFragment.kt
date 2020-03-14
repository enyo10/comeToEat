package ch.enyo.openclassrooms.comeToEat.ui.profile


import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ch.enyo.openclassrooms.comeToEat.R
import ch.enyo.openclassrooms.comeToEat.base.BaseFragment
import ch.enyo.openclassrooms.comeToEat.databinding.UserProfileFragmentBinding
import ch.enyo.openclassrooms.comeToEat.ui.main.MainActivity
import ch.enyo.openclassrooms.comeToEat.models.User
import ch.enyo.openclassrooms.comeToEat.ui.recipes.RecipesFragment
import ch.enyo.openclassrooms.comeToEat.utils.deleteUser
import ch.enyo.openclassrooms.comeToEat.utils.getUser
import ch.enyo.openclassrooms.comeToEat.utils.updateUsername
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser



class UserProfileFragment : BaseFragment() {

    companion object {
        const val TAG :String = "UserProfileFragment"
        fun newInstance() = UserProfileFragment()
    }

    private lateinit var userProfileFragmentBinding: UserProfileFragmentBinding

    private val userProfileViewModel by viewModels<UserProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        userProfileFragmentBinding = DataBindingUtil.inflate(LayoutInflater.from(context),getFragmentLayout(),container,false)
        observeAuthenticationState()

        userProfileFragmentBinding.userProfileViewModel = userProfileViewModel
        userProfileFragmentBinding.lifecycleOwner=this


        return userProfileFragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getConnectedFromFireBase()
        userProfileFragmentBinding.profileButtonDelete.setOnClickListener{
            showDeleteDialog()
        }
        userProfileFragmentBinding.profileButtonSignOut.setOnClickListener{
            logout()
        }
        userProfileFragmentBinding.profileButtonUpdate.setOnClickListener{
            updateUsername()
        }
    }


    private fun updateUserData(user:User){
        userProfileFragmentBinding.profileEditTextUsername.setText(user.username)
        userProfileFragmentBinding.profileUserName.text=user.username
        userProfileFragmentBinding.profileUserEmail.text=user.email

        val view =userProfileFragmentBinding.imageViewProfile

        user.imagesUrl?.let { loadImage(view, it) }


    }

    private fun loadImage(v: ImageView, image:String){

        Glide.with(v.context)
            .load(image)
            .placeholder(R.drawable.ic_anon_user_48dp)
            .apply(RequestOptions().circleCrop())
            .into(v)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        (context as MainActivity).navView.visibility=View.GONE
        menu.clear()
    }


    override fun getFragmentLayout(): Int {
        return R.layout.user_profile_fragment
    }

    override fun loadData() {

    }

    override fun initRecyclerView() {

    }

    private fun logout(){
        (context as MainActivity).signOutFromFirebase()
    }

    private fun updateUsername(){

        val username: String = userProfileFragmentBinding.profileEditTextUsername.text.toString()

        if( username != getString(R.string.info_no_username_found)){
            userProfileFragmentBinding.profileProgressBar.visibility=View.VISIBLE

            updateUsername(username,getCurrentUser()!!.uid)

                    .addOnSuccessListener {
                        getConnectedFromFireBase()
           }
            userProfileFragmentBinding.profileProgressBar.visibility=View.GONE
        }

    }

    private fun getConnectedFromFireBase(){
        getUser(getCurrentUser()!!.uid).addOnSuccessListener { document ->
            if (document != null) {
                Log.d(RecipesFragment.TAG, "DocumentSnapshot usr data: ${document.data}")
              updateUserData (document.toObject(User::class.java) as User)
            } else {
                Log.d(RecipesFragment.TAG, "No such document")
            }
        }
            .addOnFailureListener { exception ->
                Log.d(RecipesFragment.TAG, "get failed with ", exception)
            }
    }




    private  fun deleteUserAccount(){
        deleteUser(getCurrentUser()!!.uid)!!.addOnFailureListener { onFailureListener() }
        activity?.let {
            AuthUI.getInstance()
                .delete(it).addOnSuccessListener { findNavController().navigate(R.id.loginFragment) } }
        }

   private fun showDeleteDialog() {

        val builder = context?.let { AlertDialog.Builder(it) }
        builder!!.setTitle("WARNING")
        .setMessage(R.string.popup_message_confirmation_delete_account)
        .setPositiveButton(android.R.string.yes) { _, _ ->
            Toast.makeText(context, android.R.string.yes, Toast.LENGTH_SHORT).show()

            deleteUserAccount()
        }
        .setNegativeButton(android.R.string.no) { _, _ ->
            Toast.makeText(context,
                android.R.string.no, Toast.LENGTH_SHORT).show()
        }
        builder.show()
    }

    // --------------------
// ERROR HANDLER
// --------------------

    private fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }


}
