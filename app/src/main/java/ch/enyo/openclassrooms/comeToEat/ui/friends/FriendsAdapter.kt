package ch.enyo.openclassrooms.comeToEat.ui.friends

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ch.enyo.openclassrooms.comeToEat.BR
import ch.enyo.openclassrooms.comeToEat.R
import ch.enyo.openclassrooms.comeToEat.databinding.FragmentFriendsListItemBinding
import ch.enyo.openclassrooms.comeToEat.models.Recipe
import ch.enyo.openclassrooms.comeToEat.models.User
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.common.io.Resources

class FriendsAdapter(val fragment: FriendsFragment,var users:ArrayList<User>) :RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder>(){

    companion object{
        private const val TAG ="RecipesAdapter"
    }
    private lateinit var binding: FragmentFriendsListItemBinding



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        binding =DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.fragment_friends_list_item,parent,false)
        return FriendsViewHolder(fragment,binding)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
      val user :User = users[position]
        holder.bind(user)
        holder.itemRowBinding.fragmentFriendsListItemText.text=user.username
        holder.itemRowBinding.fragmentWorkmatesListItemImage
        val imageView = holder.itemRowBinding.fragmentWorkmatesListItemImage
       // user.imagesUrl?.let { loadImage(imageView, it) }
        if(user.imagesUrl!=null){
            loadImage(imageView, user.imagesUrl!!)
        }

    }

    private fun loadImage(v: ImageView, image:String){

        Glide.with(v.context)
            .load(image)
            .placeholder(R.drawable.ic_anon_user_48dp)
            .apply(RequestOptions().circleCrop())
            .into(v)
    }





    class FriendsViewHolder(var fragment: FriendsFragment,var itemBindingView: FragmentFriendsListItemBinding):RecyclerView.ViewHolder(itemBindingView.root),View.OnClickListener {

        var itemRowBinding=itemBindingView

        init {
            itemRowBinding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            Log.d(TAG, " On user clicked")
        }

        fun bind(obj: Any?){
            itemBindingView.setVariable(BR.user,obj)
            itemRowBinding.executePendingBindings()

        }
    }

}