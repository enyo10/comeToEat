package ch.enyo.openclassrooms.comeToEat.utils

import android.view.View

interface MyListener {
    fun onMealTypeSelected(type: String?)
    fun onChoosePictureButtonClicked(v: View?)
    fun onRealEstateSaveButtonClicked(v: View?)
    fun onStartRecording(v: View?)
}