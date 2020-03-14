package ch.enyo.openclassrooms.comeToEat.ui.main

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import ch.enyo.openclassrooms.comeToEat.R
import ch.enyo.openclassrooms.comeToEat.models.Recipe
import ch.enyo.openclassrooms.comeToEat.models.Result
import ch.enyo.openclassrooms.comeToEat.ui.selections.SelectionViewModel
import ch.enyo.openclassrooms.comeToEat.utils.SelectedRecipe
import ch.enyo.openclassrooms.comeToEat.utils.getSelectedRecipe
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.crashlytics.android.Crashlytics
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : AppCompatActivity() {

    companion object{
        // 1 - Identifier for Sign-In Activity
        const val SIGN_OUT_TASK = 10
        const val DELETE_USER_TASK = 20
        const val TAG= "MainActivity"
    }
    private val selectionViewModel by viewModels<SelectionViewModel>()

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
            //    ||destination.id== R.id.recipeDetailFragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.loginFragment ) {
                toolbar.visibility = View.GONE
                navView.visibility = View.GONE
            } else {
                toolbar.visibility = View.VISIBLE
                navView.visibility = View.VISIBLE
            }

        }
        updateUIAfterNotificationOnClicked()
    }

    private fun navigateToFriendsProfile(){
        navController.navigate(R.id.userProfileFragment)

    }

    private fun updateUIAfterNotificationOnClicked(){
        val selectedRecipeId :String? = intent.getStringExtra("recipeId")
        Log.d(TAG, " new selected recipe Id  $selectedRecipeId")
        if (selectedRecipeId!=null){
            getNewSelectedRecipe(selectedRecipeId)
        }
    }

    private fun getNewSelectedRecipe(selectedRecipeId:String){
        getSelectedRecipe(selectedRecipeId).addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot!=null){
            selectionViewModel.setSelectedSelectedRecipe((documentSnapshot.toObject(SelectedRecipe::class.java))as SelectedRecipe)
                navController.navigate(R.id.selectedRecipeDetail)
        } else{
                Log.d(TAG, " SelectedRecipe not found")
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

}