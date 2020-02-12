package ch.enyo.openclassrooms.comeToEat.utils

import ch.enyo.openclassrooms.comeToEat.models.Recipe
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


const val RECIPE_COLLECTION = "recipes"
val recipeCollection: CollectionReference = FirebaseFirestore.getInstance().collection(RECIPE_COLLECTION)

// --- CREATE ---
fun saveSelectedRecipe(uid:String, recipe: Recipe): Task<Void?>? {

    val id: String = recipeCollection.document().id
    val selectedRecipe= SelectedRecipe(id,uid,recipe)

    return recipeCollection
        .document(id) // Setting uID for Document
        .set(selectedRecipe) // Setting object for Document
}


fun getAllSelectedRecipe():Query{
    return recipeCollection
}

fun deleteSelectedRecipe(recipeId:String):Task<Void?>?{
    return recipeCollection.document(recipeId).delete()
}



data class SelectedRecipe(val id: String, val userId: String,val recipe: Recipe)




