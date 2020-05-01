package com.jekro.lesjardindecaro.mvp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.auchan.uikit.module.ModuleInteractor
import com.jekro.lesjardindecaro.Constants
import com.jekro.lesjardindecaro.R

abstract class AbsFragment<V, P : BasePresenter<V>> : BaseView<P>, Fragment(){
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

    override fun setState(state: String) {
        when (state) {
            Constants.REQUESTING -> {
                getLoadingContainerId()?.let { view!!.findViewById<View>(it).visibility = View.VISIBLE }
                getInitErrorContainerId()?.let { view!!.findViewById<View>(it).visibility = View.GONE }
                getContentContainerId()?.let { view!!.findViewById<View>(it).visibility = View.GONE }
                getEmptyStateContainerId()?.let { view!!.findViewById<View>(it).visibility = View.GONE }
            }
            Constants.ERROR -> {
                getLoadingContainerId()?.let { view!!.findViewById<View>(it).visibility = View.GONE }
                getInitErrorContainerId()?.let { view!!.findViewById<View>(it).visibility = View.VISIBLE }
                getContentContainerId()?.let { view!!.findViewById<View>(it).visibility = View.GONE }
                getEmptyStateContainerId()?.let { view!!.findViewById<View>(it).visibility = View.GONE }
                /*if(isNetworkAvailable()){
                    getInitErrorContainerId()?.let {
                        view!!.findViewById<ImageView>(R.id.error_image)?.setImageResource(R.drawable.image_error)
                    }
                }else{
                    getInitErrorContainerId()?.let {
                        view!!.findViewById<ImageView>(R.id.error_image)?.setImageResource(R.drawable.image_error_network)
                        view!!.findViewById<TextView>(R.id.error_description)?.text = getString(R.string.common_error_network)
                    }
                }*/
            }
            Constants.CONTENT -> {
                getLoadingContainerId()?.let { view!!.findViewById<View>(it).visibility = View.GONE }
                getInitErrorContainerId()?.let { view!!.findViewById<View>(it).visibility = View.GONE }
                getContentContainerId()?.let { view!!.findViewById<View>(it).visibility = View.VISIBLE }
                getEmptyStateContainerId()?.let { view!!.findViewById<View>(it).visibility = View.GONE }
            }
            Constants.EMPTY -> {
                getLoadingContainerId()?.let { view!!.findViewById<View>(it).visibility = View.GONE }
                getInitErrorContainerId()?.let { view!!.findViewById<View>(it).visibility = View.GONE }
                getContentContainerId()?.let { view!!.findViewById<View>(it).visibility = View.GONE }
                getEmptyStateContainerId()?.let { view!!.findViewById<View>(it).visibility = View.VISIBLE }
            }
        }
    }
}