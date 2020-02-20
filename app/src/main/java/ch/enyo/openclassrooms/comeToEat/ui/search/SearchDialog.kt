package ch.enyo.openclassrooms.comeToEat.ui.search


import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider


import ch.enyo.openclassrooms.comeToEat.R
import ch.enyo.openclassrooms.comeToEat.databinding.SearchDialogFragmentBinding
import ch.enyo.openclassrooms.comeToEat.ui.main.MainViewModel
import ch.enyo.openclassrooms.comeToEat.ui.recipes.RecipesViewModel
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

    private val shareViewModel: MainViewModel by activityViewModels()
    private val recipesViewModel: RecipesViewModel by activityViewModels()

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

            val q :String? = searchViewModel.searchBasis.value
            val meal: String? = searchViewModel.mealType.value
            val cuisineType: String? = searchViewModel.cuisineType.value
            val dishType :String? =searchViewModel.dishType.value
            val ing :Int? =searchViewModel.maxIngredient.value



            if(q!!.isEmpty()){
                binding.searchBasisTextLayout.error = "Must be set"
                noErrors = false
            }else{
                binding.searchBasisTextLayout.error = null

            }

            if(noErrors){
                val myQueryMap: MutableMap<String,String> = mutableMapOf()
                myQueryMap["q"]=q

                if(meal!=null)myQueryMap["meal"] = meal
                if(cuisineType!=null)myQueryMap["cuisineTyp"]= cuisineType
                if(dishType!=null) myQueryMap["dishTyp"] = dishType
                if(ing!=0)myQueryMap["ing"]=ing.toString()

                Log.d(TAG, " query map ...$myQueryMap")

                shareViewModel.setRecipeQueryMap(myQueryMap)
               // recipesViewModel.setRecipeQueryMap(myQueryMap)

            }


            dismiss()
        }

        binding.actionCancel.setOnClickListener{
            dismiss()
        }

    }

}
