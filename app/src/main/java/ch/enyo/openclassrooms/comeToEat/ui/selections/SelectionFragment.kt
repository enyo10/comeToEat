package ch.enyo.openclassrooms.comeToEat.ui.selections

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import ch.enyo.openclassrooms.comeToEat.R
import ch.enyo.openclassrooms.comeToEat.base.BaseFragment
import ch.enyo.openclassrooms.comeToEat.databinding.FragmentSelectionBinding
import ch.enyo.openclassrooms.comeToEat.ui.recipes.RecipesViewModel
import ch.enyo.openclassrooms.comeToEat.utils.getAllSelectedRecipe

import ch.enyo.openclassrooms.comeToEat.utils.SelectedRecipe

class SelectionFragment : BaseFragment() {

    companion object{
        const val TAG :String ="SelectionFragment"
    }
    private lateinit var binding : FragmentSelectionBinding
    private lateinit var mSelectionAdapter: SelectionAdapter

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


    private fun initRecyclerView(){
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.selectionRecyclerView.layoutManager=linearLayoutManager

        mSelectionAdapter  = SelectionAdapter(this,ArrayList())
        binding.selectionRecyclerView.adapter=mSelectionAdapter

        Log.i(TAG,"Recycler view init success in Selection fragment")

    }

    fun navigateToSelectedRecipeDetailsFragment(){
        findNavController().navigate(R.id.selectedRecipeDetail)

    }

    private fun updateUI(list:ArrayList<SelectedRecipe>){
        mSelectionAdapter.updateWithDate(list)

    }

    private fun loadData(){

        getAllSelectedRecipe().addOnSuccessListener { result ->
            run {
                updateUI(result.toObjects(SelectedRecipe::class.java) as ArrayList<SelectedRecipe>)
            }

            Log.d(TAG, " in load data")

        }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }

    }
}