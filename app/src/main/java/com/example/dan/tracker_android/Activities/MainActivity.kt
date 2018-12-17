package com.example.dan.tracker_android.Activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.location.Location
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.dan.tracker_android.Models.Locat
import com.example.dan.tracker_android.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import kotlinx.android.synthetic.main.activity_main.*
import com.example.dan.tracker_android.Networking.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalTime

private const val PERMISSION_REQUEST = 10

class MainActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    lateinit var locationManager: LocationManager
    private var hasGps = false
    private var hasNetwork = false
    private var locationGps: Location? = null
    private var locationNetwork: Location? = null
    var userEmail=""
    var userToken=""


    private var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        disableView()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(permissions)) {
                enableView()
            } else {
                requestPermissions(permissions, PERMISSION_REQUEST)
            }
        } else {

            enableView()
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun disableView() {
        button2.isEnabled = false
        button2.alpha = 0.5F
    }

    private fun enableView() {
        if (getIntent().hasExtra("user_email")){
            userEmail = intent.getStringExtra("user_email")
        }

        if (getIntent().hasExtra("user_token")){
            userToken = intent.getStringExtra("user_token")
        }

        button2.isEnabled = true
        button2.alpha = 1F
        button2.setOnClickListener { getLocation()}

        test.setOnClickListener{
            postLocation(editTextLat.text.toString().toDouble(),editTextLong.text.toString().toDouble())
        }
    }

    private fun postLocation(lat:Double, long:Double){
        var apiServices = APIClient.client.create(ApiInterface::class.java)
        val call = apiServices.postLocation(user_email = userEmail, user_token=userToken, latitude = lat, longitude = long)
        if (lat==null || long==null)
            return
        call.enqueue(object : Callback<Locat> {
            override fun onResponse(call: Call<Locat>, response: Response<Locat>) {
                Toast.makeText(this@MainActivity, "Location Posted", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<Locat>?, t: Throwable?) {
            }
        })

    }
    @SuppressLint("MissingPermission")
    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (hasGps || hasNetwork) {

            if (hasGps) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0F, object :
                    LocationListener {
                    override fun onLocationChanged(location: Location?) {
                        if (location != null) {
                            locationGps = location
                            textView4.append("\n" + locationNetwork!!.latitude )
                            textView3.append("\n" + locationNetwork!!.longitude )
                        }
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                    }

                    override fun onProviderEnabled(provider: String?) {
                    }

                    override fun onProviderDisabled(provider: String?) {
                    }
                })

                val localGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (localGpsLocation != null) {
                    this.locationGps = localGpsLocation
                }
            }
            if (hasNetwork) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0F, object :
                    LocationListener {

                    override fun onLocationChanged(location: Location?) {
                        if (location != null) {
                            locationNetwork = location
                            textView4.append("\n" + locationNetwork!!.latitude )
                            textView3.append("\n" + locationNetwork!!.longitude )

//                            textView4.text =  locationNetwork!!.latitude.toString()
//                            textView3.text = locationNetwork!!.longitude.toString()
                        }
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                    }

                    override fun onProviderEnabled(provider: String?) {

                    }

                    override fun onProviderDisabled(provider: String?) {

                    }
                })

                val localNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (localNetworkLocation != null)
                    locationNetwork = localNetworkLocation
            }

            if(locationGps!= null && locationNetwork!= null){
                if(locationGps!!.accuracy > locationNetwork!!.accuracy){
                    tv_result.append("\nNetwork ")
                    tv_result.append("\nLatitude : " + locationNetwork!!.latitude)
                    tv_result.append("\nLongitude : " + locationNetwork!!.longitude)
                }else{
                    tv_result.append("\nGPS ")
                    tv_result.append("\nLatitude : " + locationGps!!.latitude)
                    tv_result.append("\nLongitude : " + locationGps!!.longitude)
                }
            }

        } else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }


    private fun checkPermission(permissionArray: Array<String>): Boolean {
        var allSuccess = true
        for (i in permissionArray.indices) {
            if (checkCallingOrSelfPermission(permissionArray[i]) == PackageManager.PERMISSION_DENIED)
                allSuccess = false
        }
        return allSuccess
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST) {
            var allSuccess = true
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    allSuccess = false
                    val requestAgain = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(permissions[i])
                    if (requestAgain) {
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Go to settings and enable the permission", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            if (allSuccess)
                enableView()

        }
    }
}
