package io.ecosed.framework.services

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import io.ecosed.framework.ILicenseResultListener
import io.ecosed.framework.ILicenseV2ResultListener
import io.ecosed.framework.ILicensingService

class LicensingService : Service() {

    override fun onBind(intent: Intent?): IBinder {
        return object : ILicensingService.Stub() {

            override fun checkLicense(
                nonce: Long,
                packageName: String?,
                listener: ILicenseResultListener?
            ) {
                Log.d(tag, "checkLicense($nonce, $packageName)")
            }

            override fun checkLicenseV2(
                packageName: String?,
                listener: ILicenseV2ResultListener?,
                extraParams: Bundle?
            ) {
                Log.d(tag, "checkLicenseV2($packageName, $extraParams)")
            }
        }
    }

    companion object {
        private const val tag = "FakeLicenseService"
    }
}