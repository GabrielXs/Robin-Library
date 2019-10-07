package Service

import Util.Const
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment


class RobinCamera {
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
        @SuppressLint("NewApi")
        get() {
            return act!!.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        }

    @SuppressLint("NewApi")
    fun requestPermission() {
        if (!isPermission) {
            act!!.requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                Const.REQUEST_CAMERA
            )
        }
    }
}
