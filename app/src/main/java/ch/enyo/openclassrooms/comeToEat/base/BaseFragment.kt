package ch.enyo.openclassrooms.comeToEat.base


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import ch.enyo.openclassrooms.comeToEat.R
import ch.enyo.openclassrooms.comeToEat.auth.LoginViewModel
import ch.enyo.openclassrooms.comeToEat.models.User
import ch.enyo.openclassrooms.comeToEat.ui.friends.FriendsFragment
import ch.enyo.openclassrooms.comeToEat.ui.profile.UserProfileFragment
import ch.enyo.openclassrooms.comeToEat.ui.profile.UserProfileViewModel
import ch.enyo.openclassrooms.comeToEat.ui.recipes.RecipeDetailFragment
import ch.enyo.openclassrooms.comeToEat.ui.recipes.RecipesFragment
import ch.enyo.openclassrooms.comeToEat.utils.getUser
import ch.enyo.openclassrooms.comeToEat.utils.saveSelectedRecipe
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


/**
 * A simple [Fragment] subclass.
 */
 abstract class BaseFragment : Fragment() {
    companion object{
        const val TAG :String ="BaseFragment"
    }
    val viewModel by activityViewModels<LoginViewModel>()
  //  val userProfileViewModel by activityViewModels<UserProfileViewModel>()
  //  protected lateinit var authenticatedUser: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        observeAuthenticationState()
        // Inflate the layout for this fragment
        return inflater.inflate(getFragmentLayout(), container, false)
    }

    protected abstract fun getFragmentLayout(): Int
    protected abstract fun loadData()
    protected abstract fun initRecyclerView()
   // protected abstract fun updateUI(list: List<User>)


    /**
     * Observes the authentication state and changes the UI accordingly.
     * If there is a logged in user: (1) show a logout button and (2) display their name.
     * If there is no logged in user: show a login button
     */
    protected fun observeAuthenticationState() {

        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> {
                    Log.i(TAG,"Authentication success")
                  // getConnectedFromFireBase()


                }
                else -> {
                    Log.i(RecipesFragment.TAG, "User not AUTHENTICATED")
                    findNavController().navigate(R.id.loginFragment)

                }
            }
        })
    }

    private fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }



    fun getSomeUser(userId:String){

        getUser(userId).addOnSuccessListener {
                documentSnapshot ->  val user: User?= documentSnapshot?.toObject(User::class.java)
            Log.d( TAG, " user : $user")

        }
    }

/*
    protected fun getConnectedFromFireBase(){
        onFailureListener()?.let {
            getUser(getCurrentUser()!!.uid).addOnFailureListener(it)
                .addOnSuccessListener {
                    documentSnapshot -> run { authenticatedUser = documentSnapshot!!.toObject(User::class.java) as User
                userProfileViewModel.setAuthenticatedUser(authenticatedUser)}

                Log.d(TAG, " Connected User $authenticatedUser")
            }
        }

    }*/






    protected fun onFailureListener(): OnFailureListener? {
        return OnFailureListener { e: Exception? ->
            Toast.makeText(context,getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show()
            e?.printStackTrace()
        }
    }

}
