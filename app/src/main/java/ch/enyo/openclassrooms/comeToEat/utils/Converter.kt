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


     fun formatString(list: List<String>):String{
        var value=""
        val stringSize :Int=list.size-1
        for(a in 0..stringSize){
            value+= list[a]+"\n"
        }
        return value
    }

     fun formatStringListToNewLine(list: List<String>):String{
        var value=""
        val stringSize :Int=list.size-1
        for(a in 0..stringSize){
            value+= list[a]+"\n"
        }
        return value

    }

}