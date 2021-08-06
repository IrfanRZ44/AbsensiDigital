package com.kabirland.absensidigital_demo.ui.general.about

import android.annotation.SuppressLint
import android.content.Context
import com.kabirland.absensidigital_demo.R
import com.kabirland.absensidigital_demo.base.BaseFragmentBind
import com.kabirland.absensidigital_demo.databinding.FragmentAboutBinding

class AboutFragment : BaseFragmentBind<FragmentAboutBinding>() {
    private lateinit var viewModel: AboutViewModel

    override fun getLayoutResource(): Int = R.layout.fragment_about

    override fun myCodeHere() {
        init()
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = AboutViewModel()
        bind.viewModel = viewModel
        context?.let { getVersionApps(it) }
    }

    @SuppressLint("SetTextI18n")
    private fun getVersionApps(ctx: Context){
        val manager = ctx.packageManager
        val info = manager?.getPackageInfo(
            ctx.packageName, 0
        )
        bind.textVersi.text = "Versi Aplikasi ${info?.versionName}"
    }
}

