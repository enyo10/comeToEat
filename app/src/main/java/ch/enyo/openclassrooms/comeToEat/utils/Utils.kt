package ch.enyo.openclassrooms.comeToEat.utils

import ch.enyo.openclassrooms.comeToEat.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

const val COLLECTION_NAME = "users"
val userCollection:CollectionReference =FirebaseFirestore.getInstance().collection(COLLECTION_NAME)

// --- CREATE ---
fun createUser(uid: String, username: String, urlPicture: String?,email:String): Task<Void?>? {
    // 1 - Create User object
    val userToCreate =
        User(uid, username, urlPicture,email)
    // 2 - Add a new User Document to Firestore
    return userCollection
        .document(uid) // Setting uID for Document
        .set(userToCreate) // Setting object for Document
}


// --- GET ---
fun getUser(uid: String): Task<DocumentSnapshot?>? {
    return userCollection.document(uid).get()
}


// --- UPDATE ---
fun updateUsername(username: String, uid: String): Task<Void?>? {
    return userCollection.document(uid).update("username", username)
}


// --- DELETE ---

fun deleteUser(uid: String): Task<Void?>? {
    return userCollection.document(uid).delete()
}
