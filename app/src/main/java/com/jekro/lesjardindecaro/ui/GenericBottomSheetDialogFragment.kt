package com.jekro.lesjardindecaro.ui

import android.app.Dialog
import android.os.Bundle
import android.os.Parcelable
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jekro.lesjardindecaro.R
import kotlinx.android.synthetic.main.fragment_bottom_sheet.*
import java.io.Serializable

class GenericBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private var height: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflate = inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
        inflate.layoutParams =
            CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, height)
        return inflate
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val metrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(metrics)
        height = (metrics.heightPixels * 0.9).toInt()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        title.text = arguments!!.getString(TITLE)
        arguments!!.getString(FRAGMENT_CLASS)?.let {
            val fragment = Class.forName(it).newInstance() as Fragment
            arguments!!.getSerializable(PARAMS)?.let { params ->
                if (params is HashMap<*, *>) {
                    val bundle = Bundle(params.size)
                    params.forEach { (key, value) ->
                        when (value) {
                            is Parcelable -> bundle.putParcelable(key.toString(), value)
                            is String -> bundle.putString(key.toString(), value)
                            is Int -> bundle.putInt(key.toString(), value)
                            is Serializable -> bundle.putSerializable(key.toString(), value)
                        }
                    }
                    fragment.arguments = bundle
                }
            }
            childFragmentManager.beginTransaction().replace(R.id.bottomSheetMainContainer, fragment)
                .commit()
        }
    }

    fun setTitle(bottomSheetDialogTitle: String) {
        title?.text = bottomSheetDialogTitle
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = object : BottomSheetDialog(activity!!, theme) {
            override fun onBackPressed() {
                if (childFragmentManager.backStackEntryCount > 0) {
                    childFragmentManager.popBackStack()
                } else {
                    super.onBackPressed()
                }
            }
        }

        dialog.setOnShowListener {
            val d = it as BottomSheetDialog

            val bottomSheet =
                d.findViewById(R.id.design_bottom_sheet) as FrameLayout?
            val behavior = BottomSheetBehavior.from(bottomSheet!!)
            behavior.skipCollapsed = true
            behavior.state = arguments?.getInt(STATE)!!
        }
        return dialog
    }

    companion object {
        private const val FRAGMENT_CLASS = "FRAGMENT_CLASS"
        private const val TITLE = "TITLE"
        private const val STATE = "STATE"
        private const val PARAMS = "PARAMS"

        fun newInstance(
            fragmentName: String,
            title: String? = null,
            params: HashMap<String, Any>? = null,
            stateDialog: Int = BottomSheetBehavior.STATE_EXPANDED
        ) = GenericBottomSheetDialogFragment().apply {
            arguments = Bundle().apply {
                putString(FRAGMENT_CLASS, fragmentName)
                putString(TITLE, title)
                putInt(STATE, stateDialog)
                putSerializable(PARAMS, params)
            }
        }
    }
}