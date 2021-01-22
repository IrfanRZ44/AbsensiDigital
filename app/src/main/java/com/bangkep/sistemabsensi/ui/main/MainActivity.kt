package com.bangkep.sistemabsensi.ui.main

import android.content.Intent
import android.os.CountDownTimer
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.base.BaseActivity
import com.bangkep.sistemabsensi.model.ModelResult
import com.bangkep.sistemabsensi.model.ModelUser
import com.bangkep.sistemabsensi.ui.auth.AuthActivity
import com.bangkep.sistemabsensi.utils.Constant
import com.bangkep.sistemabsensi.utils.RetrofitUtils
import com.bangkep.sistemabsensi.utils.dismissKeyboard
import com.bangkep.sistemabsensi.utils.showMessage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : BaseActivity() {
    override fun getLayoutResource(): Int = R.layout.activity_main
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var view: View
    private var exit = false

    @Suppress("DEPRECATION")
    override fun myCodeHere() {
        setTheme(R.style.CustomTheme)
        drawerLayout.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        setSupportActionBar(toolbar)
        view = findViewById(android.R.id.content)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navBeranda,
                R.id.navRiwayat,
                R.id.navProfil,
                R.id.navAbout
            ), drawerLayout
        )
        navController = findNavController(R.id.navMuballighFragment)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        setData()
    }

    private fun setData() {
        val headerView = navView?.getHeaderView(0)

        headerView?.textJenisUser?.text = savedData.getDataUser()?.level
    }

    override fun onSupportNavigateUp(): Boolean {
        dismissKeyboard(this)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (drawerLayout?.isDrawerOpen(GravityCompat.START)!!) {
            drawerLayout?.closeDrawer(GravityCompat.START)
        }
        if (drawerLayout?.isDrawerOpen(GravityCompat.END)!!) {
            drawerLayout?.closeDrawer(GravityCompat.END)
        } else {
            if (exit) {
                finish()
                return
            } else {
                showMessage(view, "Tekan Cepat 2 Kali untuk Keluar")
                exit = true

                object : CountDownTimer(2000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                    }

                    override fun onFinish() {
                        exit = false
                    }
                }.start()
            }
        }
    }

    private fun alertLogout() {
        val alert = AlertDialog.Builder(this)
        alert.setTitle(Constant.attention)
        alert.setMessage(Constant.alertLogout)
        alert.setPositiveButton(
            Constant.iya
        ) { dialog, _ ->
            val username = savedData.getDataUser()?.username

            if (!username.isNullOrEmpty()){
                progress.visibility = View.VISIBLE
                dialog.dismiss()
                val body = HashMap<String, String>()
                body["username"] = username

                deleteToken(body)
            }
            else{
                showMessage(view, "Error, mohon mulai ulang aplikasi")
            }
        }
        alert.setNegativeButton(
            Constant.tidak
        ) { dialog, _ -> dialog.dismiss() }

        alert.show()
    }

    private fun deleteToken(body: HashMap<String, String>){
        RetrofitUtils.deleteToken(body,
            object : Callback<ModelResult> {
                override fun onResponse(
                    call: Call<ModelResult>,
                    response: Response<ModelResult>
                ) {
                    progress.visibility = View.GONE
                    val result = response.body()

                    if (result?.response == Constant.reffSuccess){
                        successDeleteToken()
                    }
                    else{
                        showMessage(view, result?.response)
                    }
                }

                override fun onFailure(
                    call: Call<ModelResult>,
                    t: Throwable
                ) {
                    progress.visibility = View.GONE
                    showMessage(view, t.message)
                }
            })
    }

    private fun successDeleteToken(){
        savedData.setDataObject(ModelUser(), Constant.reffUser)
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        navView.menu.findItem(R.id.navLogout)?.setOnMenuItemClickListener {
            drawerLayout?.closeDrawer(GravityCompat.START)
            alertLogout()
            true
        }
        return super.onOptionsItemSelected(item)
    }
}