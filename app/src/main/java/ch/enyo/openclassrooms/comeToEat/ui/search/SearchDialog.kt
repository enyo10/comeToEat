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
import ch.enyo.openclassrooms.comeToEat.utils.Converter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.common.base.Strings.isNullOrEmpty
import kotlinx.android.synthetic.main.activity_main.*

class SearchDialog : DialogFragment() {

    companion object {
        fun newInstance() = SearchDialog()
        private const val TAG :String="SearchDialog"
    }

   // private lateinit var searchViewModel: SearchDialogViewModel
    private lateinit var binding:SearchDialogFragmentBinding

    private val mainViewModel: MainViewModel by activityViewModels()
    private val searchDialogViewModel: SearchDialogViewModel by activityViewModels()
    private val myQueryMap: MutableMap<String,String> = mutableMapOf()


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
       // return super.onCreateDialog(savedInstanceState)
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.search_dialog_fragment,container,false)
        //binding.lifecycleOwner=this

        val materialBuilder = MaterialAlertDialogBuilder(requireContext())
        materialBuilder.setView(binding.root)
        return materialBuilder.create()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.viewModel=searchDialogViewModel
        binding.converter= Converter()

        binding.actionSearch.setOnClickListener{

            var noErrors = true
            val q :String? = searchDialogViewModel.getSearchBasis().value
            val meal: String? = searchDialogViewModel.getMealType().value
            val cuisineType: String? = searchDialogViewModel.getCuisineType().value
            val dishType :String? =searchDialogViewModel.getDishType().value
            val ing :Int? =searchDialogViewModel.getMaxIngredient().value

            if(q.isNullOrEmpty()){
                binding.searchBasisTextLayout.error = "Must be set"

                noErrors = false
            }else{
                binding.searchBasisTextLayout.error = null

                myQueryMap["q"]=q
            }

            if(noErrors){

                if(meal!=null) myQueryMap["meal"] = meal
                if(cuisineType!=null) myQueryMap["cuisineTyp"]= cuisineType
                if(dishType!=null)  myQueryMap["dishTyp"] = dishType
                if(ing!=0) myQueryMap["ing"] = ing.toString()

                Log.d(TAG, " query map ...$myQueryMap")
                mainViewModel.setRecipeQueryMap(myQueryMap)
                dismiss()

            }

           // dismiss()
        }

        binding.actionCancel.setOnClickListener{
            dismiss()
        }

    }

}
