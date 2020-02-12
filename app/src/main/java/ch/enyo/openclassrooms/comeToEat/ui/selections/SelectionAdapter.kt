package ch.enyo.openclassrooms.comeToEat.ui.selections

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ch.enyo.openclassrooms.comeToEat.ui.recipes.RecipesAdapter
import ch.enyo.openclassrooms.comeToEat.utils.SelectedRecipe

class SelectionAdapter(context: Context,private var selection: SelectedRecipe): RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder>() {

    companion object{
        private const val TAG ="SelectionAdapter"
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecipesAdapter.RecipeViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: RecipesAdapter.RecipeViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}