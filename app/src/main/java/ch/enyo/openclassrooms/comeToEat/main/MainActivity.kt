package ch.enyo.openclassrooms.comeToEat.main

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import ch.enyo.openclassrooms.comeToEat.BuildConfig
import ch.enyo.openclassrooms.comeToEat.R
import ch.enyo.openclassrooms.comeToEat.api.RecipeStream
import ch.enyo.openclassrooms.comeToEat.models.Recipe
import ch.enyo.openclassrooms.comeToEat.models.Result
import ch.enyo.openclassrooms.comeToEat.ui.recipes.RecipesFragment
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.common.collect.ImmutableMap
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver


class MainActivity : AppCompatActivity() {

    companion object{
        // 1 - Identifier for Sign-In Activity
        const val SIGN_OUT_TASK = 10
        const val DELETE_USER_TASK = 20
        const val TAG= "MainActivity"
    }

    private val FCMAPI = "https://fcm.googleapis.com/fcm/send"
    private val serverKey =
        "key=" + BuildConfig.SERVER_KEY
    private val contentType = "application/json"
    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(this.applicationContext)
    }

  //  var mainViewModel: MainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    private var myCompositeDisposable: Disposable? = null

    var recipes :ArrayList<Recipe> =ArrayList()
    lateinit var result: Result



    private lateinit var mCoordinatorLayout:CoordinatorLayout
    private lateinit var navController:NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        subscribeToNotification()


        mCoordinatorLayout= findViewById(R.id.container_coordinator_layout)


        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.recipesFragment,
                R.id.selectionFragment,
                R.id.friendsFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        Log.i(TAG, " MainActivity on create success.")

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.loginFragment ||destination.id== R.id.recipeDetailFragment) {
                toolbar.visibility = View.GONE
                navView.visibility = View.GONE
            } else {
                toolbar.visibility = View.VISIBLE
                navView.visibility = View.VISIBLE
            }


        }


    }



    private fun restartActivity(){
        val intent = intent
        finish()
        startActivity(intent)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId== R.id.action_logout)
            signOutFromFirebase()


        return (NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item))
    }


   /* private fun signOut(){
        AuthUI.getInstance().signOut(applicationContext)
    }*/

    private fun signOutFromFirebase(){
        AuthUI.getInstance().signOut(this)
           .addOnSuccessListener(this,updateUIAfterRESTRequestsCompleted(SIGN_OUT_TASK))

    }

    private fun updateUIAfterRESTRequestsCompleted(origin: Int): OnSuccessListener<Void?>{
        return OnSuccessListener {
            when (origin) {
                 SIGN_OUT_TASK -> {Toast.makeText(this, "You are disconnected", Toast.LENGTH_LONG).show()
               //  finish()
                     restartActivity()
                 }

                 DELETE_USER_TASK -> {
                     Toast.makeText(this,"Your account have been deleted",Toast.LENGTH_LONG).show()
                   //  finish()
                 }
                else -> {
                    Toast.makeText(this," Some error happen",Toast.LENGTH_LONG).show()
                }
            }
        }
    }



    private fun subscribeToNotification(){
        FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications")
    }

    private fun unSubscribeToNotification(){
        FirebaseMessaging.getInstance().unsubscribeFromTopic("pushNotifications")
    }


  /*  fun initMessaging(){
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/invitation")
        val textAfournir=" Texte du message à fournir"

        if (!TextUtils.isEmpty(textAfournir)) {
            val topic = "/topics/invitation" //topic has to match what the receiver subscribed to

            val notification = JSONObject()
            val notifcationBody = JSONObject()

            try {
                notifcationBody.put("title", "Firebase Notification")
                notifcationBody.put("message", textAfournir)
                notification.put("to", topic)
                notification.put("data", notifcationBody)
                Log.e("TAG", "try")
            } catch (e: JSONException) {
                Log.e("TAG", "onCreate: " + e.message)
            }

            sendNotification(notification)
        }
    }

    private fun sendNotification(notification: JSONObject) {
        Log.e("TAG", "sendNotification")
        val jsonObjectRequest = object : JsonObjectRequest(FCMAPI, notification,
            Response.Listener<JSONObject> { response ->
                Log.i("TAG", "onResponse: $response")
               // msg.setText("")

            },
            Response.ErrorListener {
                Toast.makeText(this@MainActivity, "Request error", Toast.LENGTH_LONG).show()
                Log.i("TAG", "onErrorResponse: Didn't work")
            }) {

            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["Authorization"] = serverKey
                params["Content-Type"] = contentType
                return params
            }
        }
        requestQueue.add(jsonObjectRequest)
    }

*/


    /*public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }*/


    // ****************************** FIRE BASE *******************************************************

// UTILS
// ------------------------------------
// Method that handles response after SignIn Activity close.


    // --------------------
    // UI
    // --------------------
    //  - Show Snack Bar with a message
    private fun showSnackBar(
        coordinatorLayout: CoordinatorLayout,
        message: String
    ) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show()
    }

    fun updateUIWithResult(result: Result){
        Log.d(TAG, " Recipe date loading...")

        Log.d(TAG," recipes size $recipes.size")
    }


     fun loadRecipeData(){

        "https://api.edamam.com/search?" +"q=meal" + "&app_id=def9003a" + "&app_key=5afe494e2a6ed914cb7f64154b6e0203" + "&from=0" + "&to=20"

        val map: Map<String, String> = ImmutableMap.of("q","meal","app_id", "def9003a","app_key","5afe494e2a6ed914cb7f64154b6e0203")

        myCompositeDisposable = RecipeStream.getRecipeResult(map)

            .subscribeWith(object : DisposableObserver<Result>() {
                override fun onNext(result: Result) {
                    Log.i(TAG, " Recipe list downloading...")
                    // Log.i(TAG, " Recipe list size: " +result.to)
                    //  Log.i(TAG, " from"+result.from)
                    Log.d(TAG,"hits size "+result.hits.size)
                    Log.i(RecipesFragment.TAG,"result to string:  $result")

                    updateUIWithResult(result)
                }

                override fun onError(e: Throwable) {
                    Log.i("TAG", "aie, error in recipe search: " + Log.getStackTraceString(e)
                    )
                }

                override fun onComplete() {
                    Log.i(
                        RecipesFragment.TAG,
                        " Recipes  downloaded "
                    )
                }
            })

    }





}