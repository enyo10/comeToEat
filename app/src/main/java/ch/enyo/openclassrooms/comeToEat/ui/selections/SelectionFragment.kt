package ch.enyo.openclassrooms.comeToEat.ui.selections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ch.enyo.openclassrooms.comeToEat.R
import ch.enyo.openclassrooms.comeToEat.base.BaseFragment

class SelectionFragment : BaseFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(getFragmentLayout(), container, false)

        observeAuthenticationState()
        return root
    }

    override fun getFragmentLayout(): Int {
      return R.layout.fragment_selection
    }
}