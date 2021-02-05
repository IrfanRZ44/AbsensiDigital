@file:Suppress("DEPRECATION")

package com.bangkep.sistemabsensi.ui.pegawai.camera

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
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.base.BaseFragmentBind
import com.bangkep.sistemabsensi.databinding.FragmentCameraBinding
import com.bangkep.sistemabsensi.ui.pegawai.absensi.AbsensiFragment
import com.bangkep.sistemabsensi.utils.Constant
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
            camera1.startPreview()
        }
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
                camera1.startFaceDetection()
                camera1.enableShutterSound(true)

                val params = camera1.parameters
                params.focusMode = Camera.Parameters.FOCUS_MODE_AUTO
//                val focusRect = calculateTapArea(event.getX(), event.getY(), 1f)
//                val meteringRect = calculateTapArea(event.getX(), event.getY(), 1.5f)
//                params.setFocusAreas(
//                    Lists.newArrayList(
//                        Camera.Area(
//                            focusRect,
//                            1000
//                        )
//                    )
//                )
//
//                if (meteringAreaSupported) {
//                    parameters.setMeteringAreas(
//                        Lists.newArrayList(
//                            Camera.Area(
//                                meteringRect,
//                                1000
//                            )
//                        )
//                    )
//                }

                camera1.parameters = params
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
            findNavController().navigate(R.id.navAbsen,bundle, navOption)
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