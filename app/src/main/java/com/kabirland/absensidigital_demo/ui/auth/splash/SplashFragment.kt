package com.kabirland.absensidigital_demo.ui.auth.splash

import androidx.navigation.fragment.findNavController
import com.kabirland.absensidigital_demo.databinding.FragmentSplashBinding
import com.kabirland.absensidigital_demo.R
import com.kabirland.absensidigital_demo.base.BaseFragmentBind

class SplashFragment : BaseFragmentBind<FragmentSplashBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_splash
    lateinit var viewModel: SplashViewModel

    override fun myCodeHere() {
        bind.lifecycleOwner = this
        viewModel = SplashViewModel(findNavController(), savedData, activity)
        bind.viewModel = viewModel
        viewModel.isShowUpdate.value = false
        viewModel.getInfoApps()
    }
}