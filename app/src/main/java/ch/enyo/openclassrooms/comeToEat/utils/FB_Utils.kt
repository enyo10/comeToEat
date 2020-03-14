package ch.enyo.openclassrooms.comeToEat.utils

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import ch.enyo.openclassrooms.comeToEat.models.Recipe
import ch.enyo.openclassrooms.comeToEat.models.User
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


//-************************ USER *******************************************************33
const val TAG= "utils"

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

fun getAllUsers(): Task<QuerySnapshot> {
    return userCollection.get()

}

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

data class SelectedRecipe (
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
):Comparable<SelectedRecipe> {
    override fun compareTo(other: SelectedRecipe): Int {
        return this.date!!.compareTo(other.date)
    }

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

@RequiresApi(Build.VERSION_CODES.M)
  fun hasNetworkAvailable(context: Context?): Boolean {
    val service = Context.CONNECTIVITY_SERVICE
    val manager = context?.getSystemService(service) as ConnectivityManager?
    val network = manager?.activeNetwork

    return (network != null)
}



@BindingAdapter(value = ["pmtOpt", "pmtOptAttrChanged"], requireAll = false)
fun setPmtOpt(
    spinner: AppCompatSpinner,
    selectedPmtOpt: String?,
    changeListener: InverseBindingListener
) {
    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            adapterView: AdapterView<*>?,
            view: View,
            i: Int,
            l: Long
        ) {
            changeListener.onChange()
            Log.i("TAG", " value changed")
        }

        override fun onNothingSelected(adapterView: AdapterView<*>?) {
            changeListener.onChange()
        }
    }
    selectedPmtOpt?.let { getIndexOfItem(spinner, it) }?.let { spinner.setSelection(it) }
}


@InverseBindingAdapter(attribute = "pmtOpt", event = "pmtOptAttrChanged")
fun getPmtOpt(spinner: AppCompatSpinner): String? {
    return spinner.selectedItem as String

}


private fun getIndexOfItem(spinner: AppCompatSpinner, item: String): Int {
    val a: Adapter? = spinner.adapter
    if (a != null) for (i in 0 until a.count) {
        if (a.getItem(i) == item) {
            return i
        }
    }
    return 0
}


@BindingAdapter("date")
fun bindDate(view:TextView,date:Date){

    view.text = SimpleDateFormat("dd/MM/yyyy", Locale.US).format(date)
}

@BindingAdapter("label")
fun bindLabel(view: TextView,list:List<String>){
    view.text=
        formatStringListToNewLine(
            list
        )
}

@BindingAdapter("image")
 fun loadImage(v: ImageView, image:String){
    Glide.with(v.context)
        .load(image)
        .apply(RequestOptions())
        .into(v)
}

@BindingAdapter("username")
fun getRecipeOwner(view: TextView, userId:String){

    getUser(userId).addOnSuccessListener {
            documentSnapshot ->  val user = documentSnapshot?.toObject(User::class.java)

        if (user != null) {
            view.text = user.username

        } else {
            Log.d(TAG, " username not found")
        }

    }
}



