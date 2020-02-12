package ch.enyo.openclassrooms.comeToEat.ui



import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import ch.enyo.openclassrooms.comeToEat.R
import ch.enyo.openclassrooms.comeToEat.databinding.FragmentRecipeDetailBinding
import ch.enyo.openclassrooms.comeToEat.models.Recipe
import ch.enyo.openclassrooms.comeToEat.models.User
import ch.enyo.openclassrooms.comeToEat.ui.recipes.RecipesViewModel
import ch.enyo.openclassrooms.comeToEat.utils.getUser
import ch.enyo.openclassrooms.comeToEat.utils.saveSelectedRecipe
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class RecipeDetailFragment : Fragment() {

    companion object{
        const val TAG: String="RecipeDetailFragment"
    }

   /* private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val serverKey =
        "key=" + "Enter_your_server_key"
    private val contentType = "application/json"
    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue()
    }
    */

    private lateinit var recipeDetailBinding: FragmentRecipeDetailBinding

    private lateinit var webView: WebView
    private val recipesViewModel by activityViewModels<RecipesViewModel>()
    private lateinit var mRecipe: Recipe
    private lateinit var mDatePicker: DatePickerDialog
    private lateinit var dateString: String
    private lateinit var textView: MaterialTextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        recipeDetailBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.fragment_recipe_detail,container,false)

       webView= recipeDetailBinding.webView

        webView.webViewClient= WebViewClient()

        observeSelectedRecipe()
        setInvitationDate()
        recipeDetailBinding.recipeDetailButton.setOnClickListener {
            insertRecipeToFireBase()

        }

        return  recipeDetailBinding.root
    }


    private fun updateUI(recipe: Recipe){
        mRecipe= recipe
        webView.loadUrl(recipe.url)

    }

   private fun observeSelectedRecipe(){
        recipesViewModel.getSelectedRecipe().observe(viewLifecycleOwner, Observer {
            recipe ->updateUI(recipe!!)
        })
    }

    private fun getUserId(): String? {
        return if (getCurrentUser() != null) getCurrentUser()!!.uid else null
    }

    private fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    private fun insertRecipeToFireBase(){
        val userId= getUserId()
        showDialog("Choose date")

        getUser(userId!!)?.addOnSuccessListener {
            documentSnapshot ->  val user: User?= documentSnapshot?.toObject(User::class.java)
            Log.d(TAG, " user : $user")
            saveSelectedRecipe(user!!.userId!!,mRecipe)


        }


    }

    private fun showDialog(title: String) {

        val dialog = Dialog(context!!)
       // dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setTitle("Invitation date")
        dialog .setCancelable(false)
        dialog .setContentView(R.layout.date_dialog_box)
        textView = dialog .findViewById(R.id.date_picker) as MaterialTextView
        textView.setOnClickListener { mDatePicker.show() }
        textView.text = title

        val yesBtn = dialog .findViewById(R.id.yesBtn) as AppCompatButton
        val noBtn = dialog .findViewById(R.id.noBtn) as AppCompatButton

        yesBtn.setOnClickListener {
           // body.text=dateString
            dialog .dismiss()
        }
        noBtn.setOnClickListener { dialog .dismiss() }

        dialog .show()

    }

    private fun setInvitationDate(){
        // Create a DatePickerDialog and manage it
        // Create a DatePickerDialog and manage it

        Log.i(TAG, " set invitation date call ")
       val newCalendar: Calendar= Calendar.getInstance()
        mDatePicker = DatePickerDialog(
            context!!,
            OnDateSetListener { _, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val newDate = Calendar.getInstance()
                newDate[year, monthOfYear] = dayOfMonth
              //  mRealEstateViewModel.beginPeriodDate.setValue(newDate.time)
                // Display date selected
              /*  mRealEstateSearchDialogBinding.searchBeginPeriodButton.setText(
                    displayDateFormatter.format(
                        newDate.time
                    )
                )*/
               dateString= SimpleDateFormat("dd/MM/yyyy", Locale.US).format(newDate.time)
                Log.i(TAG, "date string --> $dateString")
                textView.text=dateString


            },
            newCalendar.get(Calendar.YEAR),
            newCalendar.get(Calendar.MONTH),
            newCalendar.get(Calendar.DAY_OF_MONTH)
        )
        mDatePicker.setButton(
            DialogInterface.BUTTON_NEGATIVE,
            getString(R.string.cancel),
            DialogInterface.OnClickListener { _, which: Int ->
                if (which == DialogInterface.BUTTON_NEGATIVE) {
                    dateString=""
                    Log.i(
                        TAG, " Date is cancel "
                    )
                   // mRealEstateViewModel.beginPeriodDate.setValue(null)


                }
            }
        )
    }


}
