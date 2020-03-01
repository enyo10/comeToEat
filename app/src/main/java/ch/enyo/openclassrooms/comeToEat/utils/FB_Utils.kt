package ch.enyo.openclassrooms.comeToEat.utils

import ch.enyo.openclassrooms.comeToEat.models.Recipe
import ch.enyo.openclassrooms.comeToEat.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import java.util.*


//-************************ USER *******************************************************33

const val USER_COLLECTION="users"
val userCollection:CollectionReference=FirebaseFirestore.getInstance().collection(USER_COLLECTION)




// --- CREATE ---

fun createUser(uid: String, username: String, urlPicture: String?, email: String): Task<Void?>? { // 1 - Create User object
    val userToCreate = User(uid, username, urlPicture, email)
    // 2 - Add a new User Document to Firestore
    return userCollection
        .document(uid) // Setting uID for Document
        .set(userToCreate) // Setting object for Document
}
// --- GET ---

fun getUser(uid: String): Task<DocumentSnapshot?> {
    return userCollection.document(uid)
        .get()
}

/* public static DatabaseReference getUserDateReference(String uid){
        return UserHelper.getUsersCollection().g
    }*/

/* public static DatabaseReference getUserDateReference(String uid){
        return UserHelper.getUsersCollection().g
    }*/
fun getAllUsers(): Task<QuerySnapshot> {
    return userCollection.get()

}

// --- UPDATE ---

// --- UPDATE ---
fun updateUsername(username: String, uid: String): Task<Void?> {
    return userCollection.document(uid)
        .update("username", username)
}


// --- DELETE ---
fun deleteUser(uid: String): Task<Void?>? {
    return userCollection.document(uid)
        .delete()
}




const val RECIPE_COLLECTION = "recipes"
val recipeCollection: CollectionReference = FirebaseFirestore.getInstance().collection(RECIPE_COLLECTION)

// --- CREATE ---
fun saveSelectedRecipe(uid:String, recipe: Recipe,date:Date): Task<Void?>? {

    val recipeId: String = recipeCollection.document().id
    val selectedRecipe= SelectedRecipe(recipeId,recipe.label,recipe.uri,recipe.url,recipe.image,date,recipe.dietLabels,recipe.ingredientLines,recipe.healthLabels,arrayListOf(uid))

    return recipeCollection
        .document(recipeId) // Setting uID for Document
        .set(selectedRecipe) // Setting object for Document
}

fun getSelectedRecipe(recipeId:String): Task<DocumentSnapshot?> {
    return recipeCollection.document(recipeId)
        .get()
}


fun getAllSelectedRecipe():Task<QuerySnapshot>{
    return recipeCollection.get()
}

fun deleteSelectedRecipe(recipeId:String):Task<Void?>?{
    return recipeCollection.document(recipeId).delete()
}

fun updateRecipeParticipantList(recipeId:String?,uid: String):Task<Void>{
   return recipeCollection.document(recipeId!!).update("participants",FieldValue.arrayUnion(uid))


}

data class SelectedRecipe(
    val recipeId: String? = null,
    val recipeLabel: String? =null,
    val recipeUri : String? =null,
    val recipeUrl: String? = null,
    val image: String?=null,
    val date: Date? = null,
    val dietLabel: List<String>? = null,
    val ingredients: List<String>? = null,
    val healthLabels:List<String>? = null,
    val participants: List<String>? = null
)/*{
    fun getOwnerId(): String? {
        return participants?.get(0)
    }
}*/


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






