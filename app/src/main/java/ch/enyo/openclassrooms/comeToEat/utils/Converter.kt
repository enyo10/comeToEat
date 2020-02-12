package ch.enyo.openclassrooms.comeToEat.utils

import android.text.TextUtils
import androidx.databinding.InverseMethod

class Converter {

    @InverseMethod("convertIntToString")
   fun convertStringToInt(value: String): Int {
        var a = 0
        if (!TextUtils.isEmpty(value)) a = value.toInt()
        return a
    }


   fun convertIntToString(value: Int): String? {
        return value.toString()
    }

}