package ch.enyo.openclassrooms.comeToEat.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import ch.enyo.openclassrooms.comeToEat.BuildConfig
import ch.enyo.openclassrooms.comeToEat.R
import ch.enyo.openclassrooms.comeToEat.models.Recipe
import ch.enyo.openclassrooms.comeToEat.models.Result
import ch.enyo.openclassrooms.comeToEat.models.User
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.firebase.messaging.FirebaseMessaging


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


      var connectedUser: User?=null

    var recipes :ArrayList<Recipe> =ArrayList()
    lateinit var result: Result



    private lateinit var mCoordinatorLayout:CoordinatorLayout
    lateinit var navController:NavController
    lateinit var navView : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        subscribeToNotification()

        mCoordinatorLayout= findViewById(R.id.container_coordinator_layout)

        navView =findViewById(R.id.nav_view)

       // val navView: BottomNavigationView = findViewById(R.id.nav_view)

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
       // initBroadcaster()
        val id :Int =intent.getIntExtra("one",0)

        Log.i(TAG,"message : $id ")
        if (id==1)naviagateToSelectedFragment()



    }

    private fun navigateToFriendsProfile(){
        navController.navigate(R.id.userProfileFragment)

    }
    private fun naviagateToSelectedFragment(){
        navController.navigate(R.id.selectionFragment)
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
        if(item.itemId==R.id.action_settings)
            navigateToFriendsProfile()
        if(item.itemId== android.R.id.home){
            onBackPressed()
        return true}



        return (NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item))
    }

    override fun onSupportNavigateUp(): Boolean {
       // return super.onSupportNavigateUp()
        onBackPressed()
        return true
    }


    fun signOutFromFirebase(){
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
                    finish()
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



   inner class MyReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            navController.navigate(R.id.selectionFragment)

            // an Intent broadcast.
          //  throw UnsupportedOperationException("Not yet implemented")
        }
    }

}