package ch.enyo.openclassrooms.comeToEat

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
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    companion object{

        // 1 - Identifier for Sign-In Activity
        const val SIGN_OUT_TASK = 10
        const val DELETE_USER_TASK = 20
        const val TAG= "MainActivity"

    }

    private lateinit var mCoordinatorLayout:CoordinatorLayout
    private lateinit var navController:NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        mCoordinatorLayout= findViewById(R.id.container_coordinator_layout)


        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.recipesFragment, R.id.selectionFragment, R.id.friendsFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        Log.i(TAG, " MainActivity on create success.")

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.loginFragment||destination.id==R.id.recipeDetailFragment) {
                toolbar.visibility = View.GONE
                navView.visibility = View.GONE
            } else {
                toolbar.visibility = View.VISIBLE
                navView.visibility = View.VISIBLE
            }
        }


    }
    private fun onNavigationConfig(){

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
        if(item.itemId==R.id.action_logout)
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





}