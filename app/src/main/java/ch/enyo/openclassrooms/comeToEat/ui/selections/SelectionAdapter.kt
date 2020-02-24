package ch.enyo.openclassrooms.comeToEat.ui.selections

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ch.enyo.openclassrooms.comeToEat.BR
import ch.enyo.openclassrooms.comeToEat.R
import ch.enyo.openclassrooms.comeToEat.databinding.FragmentSelectionItemBinding
import ch.enyo.openclassrooms.comeToEat.models.User
import ch.enyo.openclassrooms.comeToEat.utils.SelectedRecipe
import ch.enyo.openclassrooms.comeToEat.utils.getUser
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.*

class SelectionAdapter(var fragment: SelectionFragment,private var selections: List<SelectedRecipe>): RecyclerView.Adapter<SelectionAdapter.SelectionViewHolder>() {


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
        holder.itemRowBinding.itemRecipeName.text=selectedRecipe.recipeLabel
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        holder.itemRowBinding.itemRecipeDate.text= SimpleDateFormat("dd/MM/yyyy", Locale.US).format(selectedRecipe.date)
        holder.itemRowBinding.itemRecipeParticipants.text=selectedRecipe.participants!!.size.toString()
        holder.itemRowBinding.itemRecipeLabel.text=selectedRecipe.recipeLabel

        holder.getRecipeOwner(selectedRecipe.participants[0])

       val imageView = holder.itemRowBinding.itemRecipeImageView
        selectedRecipe.image?.let { loadImage(imageView, it) }


    }



    private fun loadImage(v: ImageView, image:String){

        Glide.with(v.context)
            .load(image)
            .apply(RequestOptions())
            .into(v)
    }

    fun updateWithDate(list:ArrayList<SelectedRecipe>){
        selections=list
        notifyDataSetChanged()
    }




    class SelectionViewHolder( val selectionFragment: SelectionFragment, binding: FragmentSelectionItemBinding) : RecyclerView.ViewHolder(binding.root),
    View.OnClickListener{


        var itemRowBinding : FragmentSelectionItemBinding = binding


        init {
            Log.d(TAG, " in init ")
           // this.itemRowBinding.root.setOnClickListener(this)
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


        fun getRecipeOwner(userId:String){

            getUser(userId).addOnSuccessListener {
                    documentSnapshot ->  val user = documentSnapshot?.toObject(User::class.java)
                itemRowBinding.itemRecipeHost.text=user!!.username

                Log.d(TAG, " user : $user")

            }
        }
 


    }






}