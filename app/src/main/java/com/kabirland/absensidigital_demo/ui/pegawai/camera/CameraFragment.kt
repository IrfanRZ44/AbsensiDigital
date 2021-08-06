@file:Suppress("DEPRECATION")

package com.kabirland.absensidigital_demo.ui.pegawai.camera

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PixelFormat
import android.hardware.Camera
import android.hardware.Camera.PictureCallback
import android.hardware.Camera.ShutterCallback
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.kabirland.absensidigital_demo.R
import com.kabirland.absensidigital_demo.base.BaseFragmentBind
import com.kabirland.absensidigital_demo.databinding.FragmentCameraBinding
import com.kabirland.absensidigital_demo.ui.pegawai.absensi.AbsensiFragment
import com.kabirland.absensidigital_demo.utils.Constant
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class CameraFragment : BaseFragmentBind<FragmentCameraBinding>(), SurfaceHolder.Callback {
    private lateinit var viewModel: CameraViewModel
    private lateinit var camera1: Camera
    private lateinit var surfaceView: SurfaceView
    private lateinit var surfaceHolder: SurfaceHolder
    private var previewing = false
    private var modeCamera = 1
    private var modeFlash = false

    override fun getLayoutResource(): Int = R.layout.fragment_camera

    override fun myCodeHere() {
        init()
        initCamera()
        bind.framePreview.addView(surfaceView)

        bind.fabFoto.setOnClickListener {
            camera1.takePicture(myShutterCallback, pictureCallbackRAW, pictureCallbackBMP)
        }

        bind.fabRotate.setOnClickListener {
            modeCamera = if (modeCamera == 1){
                0
            } else{
                1
            }
            camera1.stopPreview()
            camera1.release()
            previewing = false
            showCamera()
        }

        bind.fabFlash.setOnClickListener {
            try{
                if (hasFlash()){
                    val params = camera1.parameters
                    if (!modeFlash){
                        bind.fabFlash.setImageResource(R.drawable.ic_flash_off_black)
                        params.flashMode = Camera.Parameters.FLASH_MODE_TORCH
                        modeFlash = true
                    }
                    else{
                        bind.fabFlash.setImageResource(R.drawable.ic_flash_on_black)
                        params.flashMode = Camera.Parameters.FLASH_MODE_OFF
                        modeFlash = false
                    }
                    camera1.parameters = params
                }
                else{
                    viewModel.message.value = "Error, Device Anda tidak support menggunakan blits"
                    Toast.makeText(
                        context,
                        "Error, Device Anda tidak support menggunakan blits",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception){
                viewModel.message.value = "Error, Device Anda tidak support menggunakan blits"
                Toast.makeText(
                    context,
                    "Error, Device Anda tidak support menggunakan blits",
                    Toast.LENGTH_LONG
                ).show()
            } catch (e: RuntimeException){
                viewModel.message.value = "Error, Device Anda tidak support menggunakan blits"
                Toast.makeText(
                    context,
                    "Error, Device Anda tidak support menggunakan blits",
                    Toast.LENGTH_LONG
                ).show()
            }
            camera1.startPreview()
        }
    }

    private fun hasFlash(): Boolean {
        val parameters = try { camera1.parameters }
        catch (ignored: RuntimeException) {
            return false
        }

        if (parameters.flashMode == null) {
            return false
        }

        val supportedFlashModes = parameters.supportedFlashModes
        return !(supportedFlashModes == null || supportedFlashModes.isEmpty() ||
                supportedFlashModes.size == 1 && supportedFlashModes[0] == Camera.Parameters.FLASH_MODE_OFF)
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = CameraViewModel()
        bind.viewModel = viewModel
    }

    private fun initCamera(){
        activity?.window?.setFormat(PixelFormat.TRANSPARENT)
        surfaceView = SurfaceView(context)
        surfaceHolder = surfaceView.holder
        surfaceHolder.addCallback(this)
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)

        showCamera()
    }

    private fun showCamera(){
        if (!previewing) {
            camera1 = Camera.open(modeCamera)
            try {
                camera1.setDisplayOrientation(90)
                camera1.setPreviewDisplay(surfaceHolder)
                camera1.enableShutterSound(true)
                camera1.startPreview()
                previewing = true
            } catch (e: IOException) {
                viewModel.message.value = e.message
            }
        }
    }

    private var myShutterCallback = ShutterCallback {
    }

    private var pictureCallbackRAW = PictureCallback { _, _ -> }

    @SuppressLint("SimpleDateFormat")
    private fun saveImage(finalBitmap: Bitmap) {
        val storageDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val timeStamp = SimpleDateFormat(Constant.dateFormat3).format(Date())
        val fname = "$timeStamp.jpg"
        val file = File(storageDir, fname)
        if (file.exists()) file.delete()

        try {
            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()

            val imagePath = Uri.fromFile(file)

            val bundle = Bundle()
            val fragmentTujuan = AbsensiFragment()
            bundle.putString(Constant.reffIdHari, this.arguments?.getString(Constant.reffIdHari))
            bundle.putString(Constant.reffIdAbsen, this.arguments?.getString(Constant.reffIdAbsen))
            bundle.putString(Constant.reffFoto2, imagePath.path)
            bundle.putString(Constant.reffJenis, this.arguments?.getString(Constant.reffJenis))
            fragmentTujuan.arguments = bundle
            val navOption = NavOptions.Builder().setPopUpTo(R.id.navAbsen, true).build()
            findNavController().navigate(R.id.navAbsen, bundle, navOption)
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private var pictureCallbackBMP =
        PictureCallback { dataBitmap, _ ->
            val bitmapPicture = BitmapFactory.decodeByteArray(dataBitmap, 0, dataBitmap.size)
            val correctBmp = Bitmap.createBitmap(
                bitmapPicture,
                0,
                0,
                bitmapPicture.width,
                bitmapPicture.height,
                null,
                true
            )

//
//            val compressedImageFile = Compressor.compress(context, correctBmp) {
//                quality(80) // combine with compressor constraint
//                format(Bitmap.CompressFormat.WEBP)
//            }

            saveImage(correctBmp)
        }

    override fun surfaceChanged(holder: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        if (previewing) {
            camera1.stopPreview()
            previewing = false
        }

        try {
            camera1.setPreviewDisplay(surfaceHolder)
            camera1.startPreview()
            previewing = true
        } catch (e: IOException) {
            viewModel.message.value = e.message
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        camera1.stopPreview()
        camera1.release()
        previewing = false
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
    }
}
