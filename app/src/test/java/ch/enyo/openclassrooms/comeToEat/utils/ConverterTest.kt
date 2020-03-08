package ch.enyo.openclassrooms.comeToEat.utils

import org.junit.Test

import org.junit.Assert.*


 class ConverterTest {

     @Test
    fun convertStringToInt() {
         val myString ="1"
         val myInt = 1
         assertTrue(Converter().convertStringToInt(myString) ==myInt)
    }

     @Test
    fun convertIntToString() {
        // val myString ="1"
         val myInt = 1
         val myString= Converter().convertIntToString(myInt)
         assertTrue(myString is String)
         assertTrue(myString.equals("1"))

    }
}