package ch.enyo.openclassrooms.comeToEat.ui.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.enyo.openclassrooms.comeToEat.R
import ch.enyo.openclassrooms.comeToEat.base.BaseFragment
import ch.enyo.openclassrooms.comeToEat.databinding.FragmentFriendsBinding
import ch.enyo.openclassrooms.comeToEat.models.User
import ch.enyo.openclassrooms.comeToEat.utils.getAllUsers


open class FriendsFragment : BaseFragment() {

    companion object {
        const val TAG = "FriendsFragment"
    }

    private lateinit var binding: FragmentFriendsBinding
    private lateinit var notificationsViewModel: FriendsViewModel
    protected lateinit var friendsAdapter: FriendsAdapter
    protected var users: ArrayList<User> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        notificationsViewModel =
            ViewModelProvider(this).get(FriendsViewModel::class.java)


        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.fragment_friends,
            container,
            false
        )
        // val textView: TextView = root.findViewById(R.id.text_notifications)
        // notificationsViewModel.text.observe(this, Observer {
        //     textView.text = it
        //  })
        observeAuthenticationState()
        initRecyclerView()
        loadData()
        binding.swipeRefresh.setOnRefreshListener{
            loadData()
        }

        return binding.root
    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_friends
    }

    override fun loadData() {
        getAllUsers().addOnSuccessListener { result ->
            run {
                updateUI(result.toObjects(User::class.java) as ArrayList<User>)
            }
        }
    }

    override fun initRecyclerView() {
        val recyclerView: RecyclerView = binding.recyclerView
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager

        friendsAdapter = FriendsAdapter(this, users)

        recyclerView.adapter = friendsAdapter
    }

   /* override fun updateUI(list: List<*>) {

        val users : ArrayList<User> = list as ArrayList<User>
    }
*/




   open  fun updateUI(list: ArrayList<User>) {
      //  binding.swipeRefresh.isRefreshing=false
        users.clear()
        users.addAll(list)
        friendsAdapter.notifyDataSetChanged()

    }

}