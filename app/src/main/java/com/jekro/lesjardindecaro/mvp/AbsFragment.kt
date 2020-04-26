package com.jekro.lesjardindecaro.mvp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.auchan.uikit.module.ModuleInteractor
import com.auchan.uikit.mvp.BasePresenter

abstract class AbsFragment<V, P : BasePresenter<V>> : Fragment() {
    abstract val presenter: P
    abstract val moduleInteractor: ModuleInteractor

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    abstract fun getLayoutId(): Int

    fun openFragment(container: Int = moduleInteractor.containerId, fragment: Fragment, backstack: String? = null) {
        if (backstack != null) {
            activity!!.supportFragmentManager.beginTransaction()
                .replace(container, fragment)
                .addToBackStack(backstack)
                .commit()
        } else {
            activity!!.supportFragmentManager.beginTransaction()
                .replace(container, fragment)
                .commit()
        }
    }
}