package ch.enyo.openclassrooms.comeToEat.ui.search


import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import ch.enyo.openclassrooms.comeToEat.BuildConfig

import ch.enyo.openclassrooms.comeToEat.main.MainActivity
import ch.enyo.openclassrooms.comeToEat.R
import ch.enyo.openclassrooms.comeToEat.databinding.SearchDialogFragmentBinding
import ch.enyo.openclassrooms.comeToEat.main.MainViewModel
import ch.enyo.openclassrooms.comeToEat.utils.Converter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*

class SearchDialog : DialogFragment() {

    companion object {
        fun newInstance() = SearchDialog()
        private const val TAG :String="SearchDialog"
    }




    private lateinit var searchViewModel: SearchDialogViewModel
    private lateinit var binding:SearchDialogFragmentBinding
   // private val searchViewModel by activityViewModels<RecipesViewModel>()
   private val mainViewModel by activityViewModels<MainViewModel>()
    private val shareViewModel: MainViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
       // return super.onCreateDialog(savedInstanceState)
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.search_dialog_fragment,container,false)
        binding.lifecycleOwner=this

        val materialBuilder = MaterialAlertDialogBuilder(requireContext())
        materialBuilder.setView(binding.root)


        return materialBuilder.create()


    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        searchViewModel= ViewModelProvider(this).get(SearchDialogViewModel::class.java)


        binding.viewModel=searchViewModel
        binding.converter= Converter()

        binding.actionSearch.setOnClickListener{
            Log.i(TAG, " search basis ${searchViewModel.searchBasis.value}")
            Log.i(TAG, " max ingredient ${searchViewModel.maxIngredient.value}")

            var noErrors = true

            var q :String? = searchViewModel.searchBasis.value
            var meal: String? = searchViewModel.mealType.value
            var cuisineType: String? = searchViewModel.cuisineType.value
            var dishType :String? =searchViewModel.dishType.value
            var ing :Int? =searchViewModel.maxIngredient.value



            if(q!!.isEmpty()){
                binding.searchBasisTextLayout.error = "Must be set"
                noErrors = false
            }else{
                binding.searchBasisTextLayout.error = null

            }

            if(noErrors){
                val myQueryMap: MutableMap<String,String> = mutableMapOf()
                myQueryMap["q"]=q
                myQueryMap["app_id"]= "def9003a"
                myQueryMap["app_key"]=BuildConfig.api_key

                if(meal!=null)myQueryMap["meal"]=meal
                if(cuisineType!=null)myQueryMap["cuisineType"]=cuisineType
                if(dishType!=null) myQueryMap["dishType"] = dishType
                if(ing!=0)myQueryMap["ing"]=ing.toString()

                shareViewModel.queryMap.value=myQueryMap


                // ("q",q,"app_id", "def9003a","app_key",BuildConfig.api_key)

              //  val mutableMap1: Map<String, String> = mutableMapOf<String, String>()

              //  if(meal!=null) queryMap.

            }


          //  val map: Map<String, String> = ImmutableMap.of("q","meal","app_id", "def9003a","app_key","5afe494e2a6ed914cb7f64154b6e0203")




            val mainActivity =activity as MainActivity


            mainActivity.loadRecipeData()
            dismiss()
        }

        binding.actionCancel.setOnClickListener{
            dismiss()
        }

    }

}
