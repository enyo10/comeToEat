package ch.enyo.openclassrooms.comeToEat.ui.recipes

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ch.enyo.openclassrooms.comeToEat.BR
import com.bumptech.glide.Glide
import ch.enyo.openclassrooms.comeToEat.R
import ch.enyo.openclassrooms.comeToEat.databinding.RecipeListItemBinding
import ch.enyo.openclassrooms.comeToEat.models.Recipe
import com.bumptech.glide.request.RequestOptions

class RecipesAdapter( val fragment: RecipesFragment,private var recipes:ArrayList<Recipe>) :RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder>() {


    companion object{
        private const val TAG ="RecipesAdapter"
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecipeViewHolder {
       val  binding :RecipeListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recipe_list_item, parent, false
        )
        return RecipeViewHolder(fragment,binding)

    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe:Recipe =recipes[position]
        holder.bind(recipe)
        holder.itemRowBinding.itemRecipeName.text=recipe.label
        val dietLabel =formatString(recipe.dietLabels)

        holder.itemRowBinding.recipeDietLabel.text=dietLabel
        holder.itemRowBinding.itemRecipeHearthLabel.text=formatStringListToNewLine(recipe.healthLabels)

        loadImage(holder.itemRowBinding.recipeImageView,recipe.image)


    }

    private fun formatString(list: List<String>):String{
        var value=""
        val stringSize :Int=list.size-1
        for(a in 0..stringSize){
           value+= list[a]+"\n"
        }
        return value
    }

    private fun formatStringListToNewLine(list: List<String>):String{
        var value=""
        val stringSize :Int=list.size-1
        for(a in 0..stringSize){
            value+= list[a]+"\n"
        }
        return value

    }

    private fun loadImage(v: ImageView, image:String){

         Glide.with(v.context)
                .load(image)
            .apply(RequestOptions())
            .into(v)
    }


  /*  fun updateWithDate(list:ArrayList<Recipe>){
        recipes=list
        notifyDataSetChanged()
    }*/


    class RecipeViewHolder(var recipesFragment: RecipesFragment,binding: RecipeListItemBinding):RecyclerView.ViewHolder(binding.root),
        View.OnClickListener{

         var itemRowBinding :RecipeListItemBinding = binding

        init {
            itemRowBinding.root.setOnClickListener(this)
        }

        fun bind(obj: Any?) {
           itemRowBinding.setVariable(BR.recipe,obj)

            itemRowBinding.executePendingBindings()
        }

        override fun onClick(v: View?) {

           recipesFragment.recipesViewModel.setSelectedRecipe(itemRowBinding.recipe!!)
            recipesFragment.navigateToDetailsFragment()

        }



    }

}