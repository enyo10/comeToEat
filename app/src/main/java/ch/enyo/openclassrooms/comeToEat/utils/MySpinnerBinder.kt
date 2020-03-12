package ch.enyo.openclassrooms.comeToEat.utils

import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener

class MySpinnerBinder {

    companion object{
        @JvmStatic
        @BindingAdapter(value = ["pmtOpt", "pmtOptAttrChanged"], requireAll = false)
        fun setPmtOpt(
            spinner: AppCompatSpinner,
            selectedPmtOpt: String?,
            changeListener: InverseBindingListener
        ) {
            spinner.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View,
                    i: Int,
                    l: Long
                ) {
                    changeListener.onChange()
                    Log.i("TAG", " value changed")
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {
                    changeListener.onChange()
                }
            }
            selectedPmtOpt?.let { getIndexOfItem(spinner, it) }?.let { spinner.setSelection(it) }
        }

        @JvmStatic
        @InverseBindingAdapter(attribute = "pmtOpt", event = "pmtOptAttrChanged")
        fun getPmtOpt(spinner: AppCompatSpinner): String? {
            //return if(spinner.selectedItem!=null)
              //  (spinner.selectedItem) as String
             return spinner.selectedItem as String

        }

        @JvmStatic
        private fun getIndexOfItem(spinner: AppCompatSpinner, item: String): Int {
            val a: Adapter? = spinner.adapter
            if (a != null) for (i in 0 until a.count) {
                if (a.getItem(i) == item) {
                    return i
                }
            }
            return 0
        }
    }
   /* @BindingAdapter(value = ["pmtOpt", "pmtOptAttrChanged"], requireAll = false)
    fun setPmtOpt(
        spinner: AppCompatSpinner,
        selectedPmtOpt: String,
        changeListener: InverseBindingListener
    ) {
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View,
                i: Int,
                l: Long
            ) {
                changeListener.onChange()
                Log.i("TAG", " value changed")
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                changeListener.onChange()
            }
        }
        spinner.setSelection(getIndexOfItem(spinner, selectedPmtOpt))
    }*/

   /* @InverseBindingAdapter(attribute = "pmtOpt", event = "pmtOptAttrChanged")
    fun getPmtOpt(spinner: AppCompatSpinner): String? {
        return spinner.selectedItem as String
    }

    private fun getIndexOfItem(spinner: AppCompatSpinner, item: String): Int {
        val a: Adapter? = spinner.adapter
        if (a != null) for (i in 0 until a.count) {
            if (a.getItem(i) == item) {
                return i
            }
        }
        return 0
    }*/
}