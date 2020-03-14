package ch.enyo.openclassrooms.comeToEat.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import ch.enyo.openclassrooms.comeToEat.models.User


class UserProfileViewModel : ViewModel() {

    private val username:MutableLiveData<String> = MutableLiveData()
    fun getUsername(): MutableLiveData<String>{
        return username
    }

    fun setUsername(name:String){
        username.value=name
    }


}
