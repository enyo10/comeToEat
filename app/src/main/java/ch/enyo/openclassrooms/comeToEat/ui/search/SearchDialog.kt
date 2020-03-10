package ch.enyo.openclassrooms.comeToEat.ui.search


import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import ch.enyo.openclassrooms.comeToEat.R
import ch.enyo.openclassrooms.comeToEat.databinding.SearchDialogFragmentBinding
import ch.enyo.openclassrooms.comeToEat.ui.main.MainViewModel
import ch.enyo.openclassrooms.comeToEat.utils.Converter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*

class SearchDialog : DialogFragment(), AdapterView.OnItemSelectedListener {

    companion object {
        fun newInstance() = SearchDialog()
        private const val TAG: String = "SearchDialog"
    }

    // private lateinit var searchViewModel: SearchDialogViewModel
    private lateinit var binding: SearchDialogFragmentBinding

    private val mainViewModel: MainViewModel by activityViewModels()
    private val searchDialogViewModel: SearchDialogViewModel by activityViewModels()
    private val myQueryMap: MutableMap<String, String> = mutableMapOf()


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // return super.onCreateDialog(savedInstanceState)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.search_dialog_fragment,
            container,
            false
        )
        //binding.lifecycleOwner=this

        val materialBuilder = MaterialAlertDialogBuilder(requireContext())
        materialBuilder.setView(binding.root)
        return materialBuilder.create()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.viewModel = searchDialogViewModel
        binding.converter = Converter()
        initDietSpinner()
        initMealTypeSpinner()

        binding.actionSearch.setOnClickListener {

            var noErrors = true
            val q: String? = searchDialogViewModel.getSearchBasis().value
            val meal: String? = searchDialogViewModel.getMealType().value
            val cuisineType: String? = searchDialogViewModel.getCuisineType().value
            val dishType: String? = searchDialogViewModel.getDishType().value
            val ing: Int? = searchDialogViewModel.getMaxIngredient().value

            if (q.isNullOrEmpty()) {
                binding.searchBasisTextLayout.error = "Must be set"

                noErrors = false
            } else {
                binding.searchBasisTextLayout.error = null

                myQueryMap["q"] = q
            }

            if (noErrors) {

                if (meal != null) myQueryMap["meal"] = meal
                if (cuisineType != null) myQueryMap["cuisineTyp"] = cuisineType
                if (dishType != null) myQueryMap["dishTyp"] = dishType
                if (ing != 0) myQueryMap["ing"] = ing.toString()

                Log.d(TAG, " query map ...$myQueryMap")
                mainViewModel.setRecipeQueryMap(myQueryMap)
                dismiss()
            }

            // dismiss()
        }

        binding.actionCancel.setOnClickListener {
            dismiss()
        }

    }

    private fun initDietSpinner() {
        val spinner: Spinner = binding.dishTypeSpinner
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.diet,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinner.adapter = adapter
            }
        }

    }

    private fun initMealTypeSpinner() {
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.diet,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                binding.mealTypeSpinner.adapter = adapter
            }
        }


    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }
}