package ch.enyo.openclassrooms.comeToEat.auth


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import ch.enyo.openclassrooms.comeToEat.R
import ch.enyo.openclassrooms.comeToEat.databinding.FragmentLoginBinding
import ch.enyo.openclassrooms.comeToEat.ui.main.MainActivity
import ch.enyo.openclassrooms.comeToEat.ui.recipes.RecipesFragment
import ch.enyo.openclassrooms.comeToEat.utils.createUser
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    companion object{
        const val TAG="LoginFragment"
        const val RC_SIGN_IN = 123

    }

    // Get a reference to the ViewModel scoped to activity scope.
    private val viewModel by activityViewModels<LoginViewModel>()

    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<FragmentLoginBinding>(
            inflater, R.layout.fragment_login, container, false
        )

        binding.buttonLogin.setOnClickListener { startSignInFlow() }
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()


        // Observe the authentication state so we can know if the user has logged in successfully.
        // If the user has logged in successfully, bring them back to the settings screen.
        // If the user did not log in successfully, display an error message.
        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {

                LoginViewModel.AuthenticationState.AUTHENTICATED ->{

                    navController.navigate(R.id.recipesFragment)
                }

                else -> Log.e(TAG, "Authentication state that doesn't require any UI change $authenticationState"
                )
            }
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        (context as MainActivity).navView.visibility=View.GONE
        menu.clear()
    }


    private fun startSignInFlow() {

        val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())

        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(
                providers
            ).build(), RC_SIGN_IN
        )


    }

    private fun handleResponseAfterSignIn(requestCode: Int, resultCode: Int, data: Intent?){

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {

                createUserInFireStore()
                // User successfully signed in
                Log.i(RecipesFragment.TAG, "Successfully signed in user ${FirebaseAuth.getInstance().currentUser?.displayName}!"
                )
            } else {

                Log.i(RecipesFragment.TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Handle SignIn Activity response on activity result.
        handleResponseAfterSignIn(requestCode, resultCode, data)
    }


    private fun createUserInFireStore(){
        val username=getCurrentUser()!!.displayName
        val email=getCurrentUser()!!.email
        val urlPicture=getCurrentUser()!!.photoUrl
        val uid=getCurrentUser()!!.uid
        createUser(uid,username!!,urlPicture.toString(),email!!)!!.addOnFailureListener(onFailureListener()!!)
    }

    // --------------------
// ERROR HANDLER
// --------------------
    private fun onFailureListener(): OnFailureListener? {
        return OnFailureListener {
            Toast.makeText(
                context,
                getString(R.string.error_unknown_error),
                Toast.LENGTH_LONG
            ).show()
        }
    }


    private fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }


}
