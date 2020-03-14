package ch.enyo.openclassrooms.comeToEat.ui.selections

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import ch.enyo.openclassrooms.comeToEat.R
import ch.enyo.openclassrooms.comeToEat.base.BaseFragment
import ch.enyo.openclassrooms.comeToEat.databinding.FragmentSelectionBinding
import ch.enyo.openclassrooms.comeToEat.models.User
import ch.enyo.openclassrooms.comeToEat.utils.getAllSelectedRecipe

import ch.enyo.openclassrooms.comeToEat.utils.SelectedRecipe
import ch.enyo.openclassrooms.comeToEat.utils.getUser
import java.util.*
import kotlin.collections.ArrayList

class SelectionFragment : BaseFragment() {

    companion object{
        const val TAG :String ="SelectionFragment"
    }
    private lateinit var binding : FragmentSelectionBinding
    private lateinit var mSelectionAdapter: SelectionAdapter
    private var selectedRecipeList :ArrayList<SelectedRecipe> = arrayListOf()

    val selectionViewModel by activityViewModels<SelectionViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

     binding=   DataBindingUtil.inflate(LayoutInflater.from(context),getFragmentLayout(),container,false)

        observeAuthenticationState()
        initRecyclerView()
        loadData()
        return binding.root
    }

    override fun getFragmentLayout(): Int {
      return R.layout.fragment_selection
    }

    override fun loadData() {
        getAllSelectedRecipe().addOnSuccessListener { result ->
            run {
                val myList: ArrayList<SelectedRecipe> = arrayListOf()
                val list = result.toObjects(SelectedRecipe::class.java) as ArrayList<SelectedRecipe>
                for (i in 0 until  list.size){
                    val selectedRecipe :SelectedRecipe = list[i]
                    selectedRecipe.participants?.get(0)?.let {
                        getUser(it).addOnSuccessListener {
                            documentSnapshot ->  val user= documentSnapshot?.toObject(User::class.java)
                            if (user!=null){
                                myList.add(list[i])
                                Log.i(TAG, " list size ${myList.size}")
                                updateUI(myList)
                            }

                        } }
                }

            }

        }.addOnFailureListener { exception ->
            Log.d(TAG, "Error getting documents: ", exception)
        }
    }

    fun navigateToSelectedRecipeDetailsFragment(){
        findNavController().navigate(R.id.selectedRecipeDetail)

    }

    private fun updateUI(list:ArrayList<SelectedRecipe>){

        list.sort()
        mSelectionAdapter.updateWithDate(list)
        binding.selectedRecipesProgressBar.visibility=View.GONE
    }



    override fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.selectionRecyclerView.layoutManager=linearLayoutManager

        mSelectionAdapter  = SelectionAdapter(this,selectedRecipeList)
        binding.selectionRecyclerView.adapter=mSelectionAdapter

    }
}