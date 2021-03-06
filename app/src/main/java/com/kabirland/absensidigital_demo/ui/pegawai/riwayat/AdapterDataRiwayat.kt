package com.kabirland.absensidigital_demo.ui.pegawai.riwayat

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.kabirland.absensidigital_demo.R
import com.kabirland.absensidigital_demo.model.ModelAbsensi
import com.kabirland.absensidigital_demo.utils.onClickFoto
import kotlinx.android.synthetic.main.item_riwayat.view.*

class AdapterDataRiwayat(
    private val listAfiliasi: ArrayList<ModelAbsensi>,
    private val onClik: (ModelAbsensi) -> Unit,
    private val navController: NavController
) : RecyclerView.Adapter<AdapterDataRiwayat.AfiliasiHolder>() {

    inner class AfiliasiHolder(private val itemV: View) :
        RecyclerView.ViewHolder(itemV) {
        @SuppressLint("SetTextI18n")
        fun bindAfiliasi(
            itemData: ModelAbsensi,
            onClik: (ModelAbsensi) -> Unit) {

            itemV.textTanggal.text = itemData.date_created
            itemV.textJenis.text = "Absen : ${itemData.jenis}"
            when (itemData.status) {
                "1" -> {
                    itemV.textStatus.text = "Status : Dikonfirmasi"
                    itemV.textStatus.setTextColor(Color.GREEN)
                }
                "2" -> {
                    itemV.textStatus.text = "Status : Ditolak"
                    itemV.textStatus.setTextColor(Color.RED)
                }
                else -> {
                    itemV.textStatus.text = "Status : Belum dikonfirmasi"
                    itemV.textStatus.setTextColor(Color.BLUE)
                }
            }
            itemV.imgFoto.load(itemData.img) {
                crossfade(true)
                transformations(CircleCropTransformation())
                placeholder(R.drawable.ic_camera_white)
                error(R.drawable.ic_camera_white)
                fallback(R.drawable.ic_camera_white)
                memoryCachePolicy(CachePolicy.ENABLED)
            }

            itemV.imgFoto.setOnClickListener {
                onClickFoto(itemData.img,
                    navController)
            }

            itemV.setOnClickListener {
                onClik(itemData)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AfiliasiHolder {
        return AfiliasiHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_riwayat,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = listAfiliasi.size
    override fun onBindViewHolder(holder: AfiliasiHolder, position: Int) {
        holder.bindAfiliasi(listAfiliasi[position], onClik)
    }
}
