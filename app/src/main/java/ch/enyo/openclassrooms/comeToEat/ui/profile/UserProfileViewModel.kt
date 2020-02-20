package ch.enyo.openclassrooms.comeToEat.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import ch.enyo.openclassrooms.comeToEat.models.User


class UserProfileViewModel : ViewModel() {
    var username :MutableLiveData<String> = MutableLiveData()

    val connectedUser: MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }

    private  val authenticatedUser:MutableLiveData<User?> = MutableLiveData()

    fun setAuthenticatedUser(user: User){
        authenticatedUser.value=user

    }


}
