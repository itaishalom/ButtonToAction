package com.androrier.buttontoaction.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.androrier.buttontoaction.R
import com.androrier.buttontoaction.databinding.ActivityMainBinding
import com.androrier.buttontoaction.interfaces.OnActionSelectedListener
import com.androrier.buttontoaction.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnActionSelectedListener {
    val TAG = "MainActivity"
    val PERMISSIONS_REQUEST_READ_CONTACTS = 100
    val START_CALL = "startCall"
    val NOTIFICATION_ID = 123
    private val PICK_CONTACT: Int = 1234

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main
        )

        checkPermissionsForContacts()
        viewModel.initViewModel(this)
        binding.viewmodel = viewModel
        viewModel.noActionFound.observe(this, Observer {
            toast(getString(R.string.no_action_available))
        })
        if (intent?.extras?.getBoolean(START_CALL) == true){
            Log.i(TAG, "START_CALL true" )
            startContacts()
        }
    }

    override fun onAnimationAction() {
        Log.i(TAG, "onAnimationAction" )
        val rotateAnimation = RotateAnimation(
            0.0f, 360.0f,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f
        )
        rotateAnimation.duration = 500
        button.startAnimation(rotateAnimation)
    }

    override fun onToastAction() {
        Log.i(TAG, "onToastAction" )
        toast(getString(R.string.action_is_toast))
    }

    private fun startContacts() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, PICK_CONTACT)
    }

    override fun onCallAction() {
        Log.i(TAG, "startContacts" )
        startContacts()
    }

    override fun onActivityResult(reqCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(reqCode, resultCode, data)
        when (reqCode) {
            PICK_CONTACT -> if (resultCode == RESULT_OK) {
                val contactUri = data?.getData()!!;
                val projection =
                    arrayOf(ContactsContract.CommonDataKinds.Contactables.DISPLAY_NAME);
                val cursor = this.getContentResolver().query(
                    contactUri, projection,
                    null, null, null
                );
                // If the cursor returned is valid, get the name
                if (cursor != null && cursor.moveToFirst()) {
                    val name: String =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.DISPLAY_NAME))
                    toast(getString(R.string.calling) + " {" + name + "}")
                }
                cursor?.close();
            }
        }
    }


    /**
     * Toasts a message to the user
     *
     * @param message a message to toast
     */
    private fun toast(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun checkPermissionsForContacts() {
        if (checkSelfPermission(
                android.Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(android.Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "permission granted" )
            } else {
                toast(getString(R.string.app_wont_work))
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        Log.i(TAG, "App new intent" )
        super.onNewIntent(intent)
        if (intent?.extras?.getBoolean(START_CALL) == true)
            startContacts()
    }

    override fun onNotificationAction() {
        val CHANNEL_ID = "NA"
        createNotificationChannel(CHANNEL_ID)

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        }
        intent.putExtra(START_CALL, true);
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.sym_def_app_icon)
            .setContentTitle(getString(R.string.action_notification))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun createNotificationChannel(CHANNEL_ID: String) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


}