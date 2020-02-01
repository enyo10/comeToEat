package ch.enyo.openclassrooms.comeToEat.base


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import ch.enyo.openclassrooms.comeToEat.R
import ch.enyo.openclassrooms.comeToEat.auth.LoginViewModel
import ch.enyo.openclassrooms.comeToEat.models.User
import ch.enyo.openclassrooms.comeToEat.ui.recipes.RecipesFragment


/**
 * A simple [Fragment] subclass.
 */
 abstract class BaseFragment : Fragment() {
    private val viewModel by viewModels<LoginViewModel>()
    protected lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        observeAuthenticationState()
        // Inflate the layout for this fragment
        return inflater.inflate(getFragmentLayout(), container, false)
    }

    protected abstract fun getFragmentLayout(): Int


    /**
     * Observes the authentication state and changes the UI accordingly.
     * If there is a logged in user: (1) show a logout button and (2) display their name.
     * If there is no logged in user: show a login button
     */
    protected fun observeAuthenticationState() {

        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> {
                    Log.i(RecipesFragment.TAG,"Authentication success")



                    /* binding.authButton.setOnClickListener {
                         AuthUI.getInstance().signOut(requireContext())
                     }*/
                }
                else -> {
                    Log.i(RecipesFragment.TAG, "User not AUTHENTICATED")
                    findNavController().navigate(R.id.loginFragment)

                    /* binding.authButton.setOnClickListener {
                         launchSignInFlow()
                     }*/
                }
            }
        })
    }


}
