package com.kabirland.absensidigital_demo.ui.auth

import android.view.View
import androidx.navigation.fragment.NavHostFragment
import com.kabirland.absensidigital_demo.R
import com.kabirland.absensidigital_demo.base.BaseActivity
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : BaseActivity() {
    override fun getLayoutResource(): Int = R.layout.activity_auth

    @Suppress("DEPRECATION")
    override fun myCodeHere() {
        NavHostFragment.create(R.navigation.auth_nav)
        viewParent.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }
}
