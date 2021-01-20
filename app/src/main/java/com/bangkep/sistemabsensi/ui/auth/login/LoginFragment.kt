package com.bangkep.sistemabsensi.ui.auth.login

import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.base.BaseFragmentBind
import com.bangkep.sistemabsensi.databinding.FragmentLoginBinding

class LoginFragment : BaseFragmentBind<FragmentLoginBinding>(){
    override fun getLayoutResource(): Int = R.layout.fragment_login
    lateinit var viewModel: LoginViewModel

    override fun myCodeHere() {
        bind.lifecycleOwner = this
        viewModel = LoginViewModel(findNavController(), savedData, activity)
        bind.viewModel = viewModel

        onClick()
    }

    private fun onClick() {
        bind.etPassword.editText?.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.onClickLogin()
                return@OnEditorActionListener false
            }
            false
        })
    }

}