package ch.enyo.openclassrooms.comeToEat

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.enyo.openclassrooms.comeToEat.models.User
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import org.junit.Before
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class FirebaseInstrumentedTest {
    companion object{
        private const val userId="test user id"
        private const val anotherUserId="another user id"
    }


    private val user: User = User(userId,"Rony","")
    private val anotherUser: User =User(anotherUserId,"Roan","")
    private val users: List<User> = listOf(user,anotherUser)


    @Mock
    var mockFirebaseFirestore: FirebaseFirestore? = null

    @Mock
    var mockCollectionReference: CollectionReference? = null

   /* @Mock
    var mockListeners: FirebaseObservableListeners? = null
*/
   // var firebaseUserDatabase: FirebaseUserDatabase? = null
    @Before
    fun setUp(){
       MockitoAnnotations.initMocks(this);

    }


}