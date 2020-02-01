package ch.enyo.openclassrooms.comeToEat.auth

import androidx.lifecycle.ViewModel
import ch.enyo.openclassrooms.comeToEat.auth.firebase.FirebaseUserLiveData
import androidx.lifecycle.map

class LoginViewModel :ViewModel(){

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

}