package ch.enyo.openclassrooms.comeToEat.ui.selections

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ch.enyo.openclassrooms.comeToEat.BR
import ch.enyo.openclassrooms.comeToEat.R
import ch.enyo.openclassrooms.comeToEat.databinding.FragmentSelectionItemBinding
import ch.enyo.openclassrooms.comeToEat.utils.SelectedRecipe
import kotlin.collections.ArrayList

class SelectionAdapter(var fragment: SelectionFragment,private var selections: ArrayList<SelectedRecipe>): RecyclerView.Adapter<SelectionAdapter.SelectionViewHolder>() {


    companion object{
        private const val TAG ="SelectionAdapter"
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectionViewHolder {
        val  binding : FragmentSelectionItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.fragment_selection_item, parent, false)

        return SelectionViewHolder(fragment,binding)


    }

    override fun getItemCount(): Int {
        return selections.size
    }

    override fun onBindViewHolder(holder:SelectionViewHolder, position: Int) {
        val selectedRecipe :SelectedRecipe = selections[position]
       holder.bind(selectedRecipe)

    }


    fun updateWithDate(list:ArrayList<SelectedRecipe>){
        selections=list
        notifyDataSetChanged()
    }


    class SelectionViewHolder(private val selectionFragment: SelectionFragment, binding: FragmentSelectionItemBinding) : RecyclerView.ViewHolder(binding.root),
    View.OnClickListener{

        var itemRowBinding : FragmentSelectionItemBinding = binding

        init {
            itemRowBinding.root.setOnClickListener(this)
        }

        fun bind(obj: Any?) {
           itemRowBinding.setVariable(BR.selectedRecipe,obj)
            itemRowBinding.executePendingBindings()
        }

        override fun onClick(v: View?) {
            selectionFragment.selectionViewModel.setSelectedSelectedRecipe(itemRowBinding.selectedRecipe!!)
            selectionFragment.navigateToSelectedRecipeDetailsFragment()
        }
    }

}