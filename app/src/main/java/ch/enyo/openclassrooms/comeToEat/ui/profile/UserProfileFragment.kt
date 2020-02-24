package ch.enyo.openclassrooms.comeToEat.ui.profile


import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import ch.enyo.openclassrooms.comeToEat.R
import ch.enyo.openclassrooms.comeToEat.base.BaseFragment
import ch.enyo.openclassrooms.comeToEat.databinding.UserProfileFragmentBinding
import ch.enyo.openclassrooms.comeToEat.ui.main.MainActivity
import ch.enyo.openclassrooms.comeToEat.models.User
import ch.enyo.openclassrooms.comeToEat.utils.deleteUser
import ch.enyo.openclassrooms.comeToEat.utils.updateUsername
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class UserProfileFragment : BaseFragment() {

    companion object {
        const val TAG :String = "UserProfileFragment"
        fun newInstance() = UserProfileFragment()
    }

    private lateinit var binding: UserProfileFragmentBinding

   // private lateinit var viewModel: UserProfileViewModel
       val userProfileViewModel by viewModels<UserProfileViewModel>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding= DataBindingUtil.inflate(LayoutInflater.from(context),getFragmentLayout(),container,false)

        observeAuthenticationState()
       // getConnectedFromFireBase()
       /* val user: User? =(context as MainActivity).connectedUser
        Log.d(TAG, " user $user")*/

       /* userProfileViewModel.connectedUser.value=user
        binding.userProfileViewModel=userProfileViewModel

        userProfileViewModel.connectedUser.observe(viewLifecycleOwner, Observer { myUser ->
            Log.d(TAG, "user name ${myUser.username}")
            binding.profileUserName.text= myUser.username } )

        binding.profileButtonDelete.setOnClickListener{
            showDeleteDialog()
        }
        binding.profileButtonSignOut.setOnClickListener{
            logout()
        }
        binding.profileButtonUpdate.setOnClickListener{
            updateUsername()
        }*/

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val user: User? =(context as MainActivity).connectedUser
        Log.d(TAG, " user $user")
        //val userProfileViewModel= ViewModelProvider(this).get(UserProfileViewModel::class.java)
       // userProfileViewModel.connectedUser.value=user
        binding.userProfileViewModel=userProfileViewModel

        userProfileViewModel.connectedUser.observe(this, Observer { myUser -> updateUI(myUser.username)
            } )

        binding.profileButtonDelete.setOnClickListener{
            showDeleteDialog()
        }
        binding.profileButtonSignOut.setOnClickListener{
            logout()
        }
        binding.profileButtonUpdate.setOnClickListener{
            updateUsername()
        }
    }

    private fun updateUI(username:String?){
        Log.i(TAG, "user name $username")
        binding.profileUserName.text=username
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        (context as MainActivity).navView.visibility=View.GONE
        menu.clear()
    }


   /* override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //return super.onOptionsItemSelected(item)
        return true
    }*/


    override fun getFragmentLayout(): Int {
        return R.layout.user_profile_fragment
    }

    override fun loadData() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initRecyclerView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

   /* override fun updateUI(list: List<*>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
*/
    private fun logout(){
        (context as MainActivity).signOutFromFirebase()
    }

    private fun updateUsername(){

        Log.i(TAG, " update user name method call")

        var username: String = binding.profileUserName.text.toString()

        if( username != getString(R.string.info_no_username_found)){
            Log.d(TAG, "username --$username")
            binding.profileProgressBar.visibility=View.VISIBLE
            updateUsername(username,getCurrentUser()!!.uid)

                    .addOnSuccessListener {
                Toast.makeText(context," username update success",Toast.LENGTH_LONG).show()
           }
            binding.profileProgressBar.visibility=View.GONE
        }

    }



  private  fun deleteUserAccount(){
        deleteUser(getCurrentUser()!!.uid)!!.addOnFailureListener { onFailureListener() }
        activity?.let {
            AuthUI.getInstance()
                .delete(it).addOnSuccessListener { findNavController().navigate(R.id.loginFragment) }
        }


        }
   private fun showDeleteDialog() {

        val builder = context?.let { AlertDialog.Builder(it) }
        builder!!.setTitle("WARNING")
        .setMessage(R.string.popup_message_confirmation_delete_account)
        //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        .setPositiveButton(android.R.string.yes) { dialog, which ->
            Toast.makeText(context, android.R.string.yes, Toast.LENGTH_SHORT).show()
            deleteUserAccount()
        }
        .setNegativeButton(android.R.string.no) { dialog, which ->
            Toast.makeText(context,
                android.R.string.no, Toast.LENGTH_SHORT).show()
        }

        builder.show()

    }





    // --------------------
// ERROR HANDLER
// --------------------


    private fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    private fun isCurrentUserLogged(): Boolean{
        return getCurrentUser() != null
    }


   /* private fun getConnectedFromFireBase(){
        if(isCurrentUserLogged())
        getUser(getCurrentUser()!!.uid)!!.addOnSuccessListener {
                documentSnapshot ->  viewModel.connectedUser.value = documentSnapshot!!.toObject(User::class.java) as User

            Log.d(TAG, " Connected User ${viewModel.connectedUser.value}")
        }

    }*/

}
