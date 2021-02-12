package com.bangkep.sistemabsensi.ui.auth.splash

import androidx.navigation.fragment.findNavController
import com.bangkep.sistemabsensi.databinding.FragmentSplashBinding
import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.base.BaseFragmentBind

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