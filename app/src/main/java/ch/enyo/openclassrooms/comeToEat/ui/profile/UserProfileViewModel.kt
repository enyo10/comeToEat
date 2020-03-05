package ch.enyo.openclassrooms.comeToEat.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import ch.enyo.openclassrooms.comeToEat.models.User


class UserProfileViewModel : ViewModel() {

   /* private  val authenticatedUser:MutableLiveData<User?> = MutableLiveData()

    fun setAuthenticatedUser(user: User){
        authenticatedUser.value=user
    }

    fun getAuthenticatedUser():MutableLiveData<User?>{
        return authenticatedUser

    }*/
    private val username:MutableLiveData<String> = MutableLiveData()
    fun getUsername(): MutableLiveData<String>{
        return username
    }

    fun setUsername(name:String){
        username.value=name
    }


}
