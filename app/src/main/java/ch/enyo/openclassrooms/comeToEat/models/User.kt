package ch.enyo.openclassrooms.comeToEat.models

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
            var userId:String?="",
            var username:String?="",
            var imagesUrl:String?="",
            var email:String?="")
