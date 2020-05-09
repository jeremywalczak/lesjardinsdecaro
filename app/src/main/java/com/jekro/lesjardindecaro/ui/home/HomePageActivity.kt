package com.jekro.lesjardindecaro.ui.home

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.jekro.lesjardindecaro.R
import com.jekro.lesjardindecaro.showCartDialog
import com.jekro.lesjardindecaro.ui.contact.ContactFragment
import com.jekro.lesjardindecaro.ui.user.UserFragment
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.app_bar_home_page.*

class HomePageActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var handler = Handler()

    private var initX = 0F
    private var initY = 0F
    private var newCartScaleX = 0.7F
    private var newCartScaleY = 0.7F
    private var shouldMoveCart = false
    private var isCartListenerInitilised = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        supportFragmentManager.beginTransaction().replace(
            R.id.mainContainer,
            HomePageFragment()
        ).commit()

        initX = panierImageView.x
        initY = panierImageView.y

        if (!isCartListenerInitilised)
            initListenerCart()
    }

    fun moveCart() {
        panierImageView.visibility = View.VISIBLE
        val currentFragment = supportFragmentManager.findFragmentById(R.id.mainContainer)
        if (currentFragment is HomePageFragment) {
            shouldMoveCart = false
            handler.postDelayed({
                val cardAnimX = ObjectAnimator.ofFloat(
                    panierImageView,
                    View.TRANSLATION_X,
                    initX
                ).apply { duration = 1000 }

                val cardAnimY = ObjectAnimator.ofFloat(
                    panierImageView,
                    View.TRANSLATION_Y,
                    initY
                ).apply { duration = 1000 }

                val subAnimStartSet = AnimatorSet()

                val cardAnimStartX = ObjectAnimator
                    .ofFloat(panierImageView, View.SCALE_X, 1F)
                    .setDuration(200)
                    .apply { DecelerateInterpolator() }

                val cardAnimStartY = ObjectAnimator
                    .ofFloat(panierImageView, View.SCALE_Y, 1F)
                    .setDuration(200)
                    .apply { interpolator = DecelerateInterpolator() }

                subAnimStartSet.playTogether(cardAnimStartX, cardAnimStartY)

                AnimatorSet().apply {
                    playTogether(cardAnimX, cardAnimY, subAnimStartSet)
                    start()
                }
            }, 1)
        } else {
            shouldMoveCart = true
            handler.postDelayed({
                val cardAnimBounce = ObjectAnimator.ofFloat(
                    panierImageView,
                    View.TRANSLATION_X,
                    (mainContainer.width.toFloat() / 2) - (panierImageView.width / 2)
                ).apply { duration = 1000 }

                val subAnimStartSet = AnimatorSet()

                val cardAnimStartX = ObjectAnimator
                    .ofFloat(panierImageView, View.SCALE_X, newCartScaleX)
                    .setDuration(200)
                    .apply { DecelerateInterpolator() }

                val cardAnimStartY = ObjectAnimator
                    .ofFloat(panierImageView, View.SCALE_Y, newCartScaleY)
                    .setDuration(200)
                    .apply { interpolator = DecelerateInterpolator() }

                subAnimStartSet.playTogether(cardAnimStartX, cardAnimStartY)

                AnimatorSet().apply {
                    playTogether(cardAnimBounce, subAnimStartSet)
                    start()
                }
            }, 1)
        }
    }

    private fun initListenerCart() {
        isCartListenerInitilised = true
        val initialY = 0F
        var lastX = 0F
        var lastY = 0F
        var dx = 0F
        var dy = 0F
        panierImageView.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_MOVE) {
                if (shouldMoveCart) {
                    panierImageView.alpha = 0.9F
                    panierImageView.scaleX = 0.9F
                    panierImageView.scaleY = 0.9F
                    panierImageView.x = motionEvent.rawX + dx
                    if ((motionEvent.rawY + dy) >= initialY && (motionEvent.rawY + dy) <= (mainContainer.height))
                        panierImageView.y = motionEvent.rawY + dy
                }
            }
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                panierImageView.performHapticFeedback(
                    HapticFeedbackConstants.VIRTUAL_KEY,
                    HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
                )
                lastY = panierImageView.y
                lastX = panierImageView.x
                dx = view.x - motionEvent.rawX
                dy = view.y - motionEvent.rawY
            }
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                panierImageView.alpha = 1F
                panierImageView.scaleX = if (shouldMoveCart) newCartScaleX else 1F
                panierImageView.scaleY = if (shouldMoveCart) newCartScaleY else 1F
                if ((panierImageView.y <= lastY + 4 && panierImageView.y >= lastY - 4) && (panierImageView.x <= lastX + 4 && panierImageView.x >= lastX - 4)) {
                    panierImageView.y = lastY
                    panierImageView.x = lastX
                    panierImageView.isEnabled = false
                    boundFabZoom()
                    showCartDialog()
                    Handler().postDelayed({
                        panierImageView.isEnabled = true
                    }, 1000)
                } else {
                    boundLoyaltyFabSticky(false)
                }
            }
            true
        }
    }


    private fun boundLoyaltyFabSticky(stickByLeft: Boolean = false) {
        handler.postDelayed({
            val cardAnimBounce = ObjectAnimator.ofFloat(
                panierImageView,
                View.TRANSLATION_X,
                if (stickByLeft) -mainContainer.width.toFloat() + panierImageView.width - 10F else(mainContainer.width.toFloat() / 2) - (panierImageView.width / 2) - 10F
            ).apply { duration = 200 }

            val cardAnimFinal = ObjectAnimator.ofFloat(
                panierImageView,
                View.TRANSLATION_X,
                if (stickByLeft) -mainContainer.width.toFloat() + panierImageView.width else (mainContainer.width.toFloat() / 2) - (panierImageView.width / 2)
            ).apply { duration = 200 }

            AnimatorSet().apply {
                playSequentially(cardAnimBounce, cardAnimFinal)
                start()
            }
        }, 1)
    }

    private fun boundFabZoom() {
        val scaleX = panierImageView.scaleX
        val scaleY = panierImageView.scaleY
        val animSet = AnimatorSet()
        val subAnimStartSet = AnimatorSet()
        val subAnimFinalSet = AnimatorSet()

        val cardAnimStartX = ObjectAnimator
            .ofFloat(panierImageView, View.SCALE_X, 1.1F)
            .setDuration(200)
            .apply { DecelerateInterpolator() }

        val cardAnimStartY = ObjectAnimator
            .ofFloat(panierImageView, View.SCALE_Y, 1.1F)
            .setDuration(200)
            .apply { interpolator = DecelerateInterpolator() }

        val cardAnimFinalX = ObjectAnimator
            .ofFloat(panierImageView, View.SCALE_X, scaleX)
            .apply { duration = 200 }

        val cardAnimFinalY = ObjectAnimator
            .ofFloat(panierImageView, View.SCALE_Y, scaleY)
            .apply { duration = 200 }

        subAnimStartSet.playTogether(cardAnimStartX, cardAnimStartY)
        subAnimFinalSet.playTogether(cardAnimFinalX, cardAnimFinalY)
        animSet.playSequentially(subAnimStartSet, subAnimFinalSet)
        animSet.start()
    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
        moveCart()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home_page, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_myinformations -> {
                supportFragmentManager.beginTransaction().add(
                    R.id.mainContainer,
                    UserFragment()
                ).addToBackStack(UserFragment::class.java.toString()).commit()
            }
            R.id.nav_contact -> {
                supportFragmentManager.beginTransaction().add(
                    R.id.mainContainer,
                    ContactFragment()
                ).addToBackStack(ContactFragment::class.java.toString()).commit()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
