package ch.enyo.openclassrooms.comeToEat.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.enyo.openclassrooms.comeToEat.auth.firebase.FirebaseUserLiveData
import androidx.lifecycle.map
import ch.enyo.openclassrooms.comeToEat.models.User


class LoginViewModel :ViewModel(){
    companion object{
        private const val TAG :String ="LoginViewModel"
    }

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }

    private  val authenticatedUser:MutableLiveData<User?> = MutableLiveData()

    fun setAuthenticatedUser(user: User){
        authenticatedUser.value=user
    }

}