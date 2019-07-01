package br.com.core.robin

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var btnCamera: Button
    private val PermissaoCodCamera = 101
    private val RequisicaoSalvarFoto = 102
    private var currentPhotoPath: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnCamera = findViewById(R.id.btncamera)
        btnCamera.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ActivityCompat.requestPermissions(
                        this@MainActivity,
                        arrayOf(Manifest.permission.CAMERA),
                        PermissaoCodCamera
                    )
                }
                Toast.makeText(applicationContext, "texto", Toast.LENGTH_SHORT).show()
            } else {
                getCamera()
            }
        }
    }

    private fun getCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { capturarFoto ->
            capturarFoto.resolveActivity(packageManager)?.also {
                val arquivoFoto: File? = try {
                    criarArquivo()
                } catch (ex: IOException) {
                    Log.e("CriarArquivo", "Erro ao criar Arquivo")
                    null
                }
                arquivoFoto?.also {
                    val photoURI: Uri =
                        FileProvider.getUriForFile(this@MainActivity, "br.com.core.robin.fileprovider", it)
                    capturarFoto.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(capturarFoto, RequisicaoSalvarFoto)
                }

            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == RequisicaoSalvarFoto){
            if(resultCode == RESULT_OK){
                val imageBitmap = BitmapFactory.decodeFile(currentPhotoPath)
                findViewById<ImageView>(R.id.imgCamera).setImageBitmap(imageBitmap)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun criarArquivo(): File? {
        val horaSalva: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val arquivoFoto = File(filesDir, "fotos")
        if (!arquivoFoto.exists()) {
            arquivoFoto.mkdir()
        }

        return File.createTempFile("JPEG_${horaSalva}", ".jpg", arquivoFoto).apply {
            currentPhotoPath = absolutePath
        }
    }
}
