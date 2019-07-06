package Service


import Util.*
import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.util.Log
import android.widget.Toast
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class RobinCamera {
    private val TAG_CLASS = RobinCamera::class.java.simpleName
    private var AUTORITY: String? = null

    private var act: Activity? = null
        get() {
            return if (field == null) frag.activity else field
        }

    private lateinit var frag: Fragment

    constructor(activity: Activity) {
        this.act = act
    }

    constructor(fragment: Fragment) {
        this.frag = fragment
    }


    val isPermission: Boolean
        get() {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                act!!.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
            } else {
                ActivityCompat.checkSelfPermission(
                    act!!.applicationContext,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            }

        }
    var currentPhotoPath: String? = null
        get() {
            return if (field.isNullOrEmpty()) {
                Toast.makeText(act!!.applicationContext, "Caminho nao encontrado!", Toast.LENGTH_LONG).show()
                null
            } else {
                field
            }
        }

    fun requestPermission() {
        if (!isPermission) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                act!!.requestPermissions(
                    arrayOf(Manifest.permission.CAMERA),
                    PERMISSAO_CAMERA
                )
            } else {
                ActivityCompat.requestPermissions(act!!, arrayOf(Manifest.permission.CAMERA), PERMISSAO_CAMERA)
            }
        }
    }

    fun getCamera(caminhoArquivo: File, prefix: String) {
        if (AUTORITY == null) throw  Exception("Desenvolvedor ")
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { capturarFoto ->
            capturarFoto.resolveActivity(act!!.packageManager)?.also {
                val arquivoTemp = try {
                    createArquivo(caminhoArquivo, prefix)
                } catch (ex: Exception) {
                    Log.e(TAG_CLASS, ex.message)
                    //Todo criar Class Robin-SnackBar
                    null
                }

                arquivoTemp?.also{
                    val photoUri = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        FileProvider.getUriForFile(act!!.applicationContext,AUTORITY!!, it)
                    }else{
                        Uri.fromFile(it)
                    }

                    capturarFoto.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    act!!.startActivityForResult(capturarFoto, REQUEST_CAMERA)
                }
            }

        }
    }


    private fun createArquivo(caminhoArquivo: File, prefix: String): File? {
        if (caminhoArquivo.isDirectory) {
            if (!caminhoArquivo.exists()) {
                caminhoArquivo.mkdir()
            }

            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            return File.createTempFile(
                String.format(".%s", prefix.toLowerCase()),
                String.format("%s_$timeStamp", prefix.toUpperCase()),
                caminhoArquivo
            ).apply {
                currentPhotoPath = absolutePath
            }

        } else {
            Log.e(TAG_CLASS, "***** CAMINHO INFORMADO NAO E UM DIRETORIO ****** ")
            //todo Criar AlertDialog

            return null
        }
    }
}
