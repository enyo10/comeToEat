package ch.enyo.openclassrooms.comeToEat.ui.search


import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import ch.enyo.openclassrooms.comeToEat.R
import ch.enyo.openclassrooms.comeToEat.databinding.SearchDialogFragmentBinding
import ch.enyo.openclassrooms.comeToEat.ui.main.MainViewModel
import ch.enyo.openclassrooms.comeToEat.utils.Converter
import ch.enyo.openclassrooms.comeToEat.utils.MyListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*

class SearchDialog : DialogFragment(),MyListener{

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
        binding.lifecycleOwner=this

        val materialBuilder = MaterialAlertDialogBuilder(requireContext())
        materialBuilder.setView(binding.root)
        return materialBuilder.create()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.viewModel = searchDialogViewModel
        binding.converter = Converter()

        initDietSpinner()
        initHealthLabel()

        binding.actionSearch.setOnClickListener {

            var noErrors = true
            val q: String? = searchDialogViewModel.getSearchBasis().value
            val healthLabel: String? = searchDialogViewModel.getHealthLabel().value
            val dietType: String? = searchDialogViewModel.getDietType().value

            if (q.isNullOrEmpty()) {
                binding.searchBasisTextLayout.error = "Must be set"

                noErrors = false
            } else {
                binding.searchBasisTextLayout.error = null
                myQueryMap["q"] = q
            }

            if (noErrors) {

                if(healthLabel !=null){
                    if (healthLabel !="--")
                        myQueryMap["health"]=healthLabel
                }

                if (dietType !=null) {
                    if(dietType != "--")
                    myQueryMap["diet"] = dietType
                }
                Log.d(TAG, " query map ...$myQueryMap")
                mainViewModel.setRecipeQueryMap(myQueryMap)
                dismiss()
            }
        }

        binding.actionCancel.setOnClickListener {
            dismiss()
        }

    }

    private fun initDietSpinner() {
        val dishTypes =
            resources.getStringArray(R.array.diet_type)
        binding.dietSpinnerAdapter=
            context?.let {
                ArrayAdapter<String>(
                    it,
                    android.R.layout.simple_spinner_dropdown_item, dishTypes
                )
            }
    }

    private fun initHealthLabel(){
        val healthLabel =resources.getStringArray(R.array.health)
     binding.healthSpinnerAdapter =
         context?.let { ArrayAdapter(it,android.R.layout.simple_selectable_list_item,healthLabel) }
    }


    override fun onMealTypeSelected(type: String?) {
        TODO("Not yet implemented")
    }

    override fun onChoosePictureButtonClicked(v: View?) {
        TODO("Not yet implemented")
    }

    override fun onRealEstateSaveButtonClicked(v: View?) {
        TODO("Not yet implemented")
    }

    override fun onStartRecording(v: View?) {
        TODO("Not yet implemented")
    }


}