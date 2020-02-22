package com.danycarreto.firestore

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {


    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseAuthListener:FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(intent.hasExtra("body")){
            txtIntent.text = intent.getStringExtra("body")
        }

        //Se registra un broadcastreceiver para estar escuchado el LocalBroadcast
        // (definido en la notifiación)
        LocalBroadcastManager.getInstance(this).registerReceiver(
            receiver(),
            IntentFilter("Notification_Action")
        )

        try{
            //Hash para facebook
            val info: PackageInfo = getPackageManager().getPackageInfo(
                "com.danycarreto.firestore",
                PackageManager.GET_SIGNATURES
            )
            info.signatures.forEach {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(it.toByteArray())
                Log.d("keyHash: ", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch( e:Exception){
            e.printStackTrace()
        }

        /**
         * Se genera el token para dispositivo único
         */
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(MainActivity::class.java.simpleName, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                Log.d(MainActivity::class.java.simpleName, token)
                Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
            })

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuthListener = FirebaseAuth.AuthStateListener {

            if(it.currentUser!= null){
                startActivity(Intent(this, ExtraActivity::class.java))
            }else{
                //Registrarlo
                val auth = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setIsSmartLockEnabled(false)
                    .setTosAndPrivacyPolicyUrls("","") //Terminos y condiciones, ademas de aviso de privacidad
                    .setLogo(R.mipmap.ic_launcher)
                    .setAlwaysShowSignInMethodScreen(true)
                    .setAvailableProviders(
                        arrayListOf(
                            AuthUI.IdpConfig.EmailBuilder().setRequireName(true).build(),
                            AuthUI.IdpConfig.GoogleBuilder().build(),
                            AuthUI.IdpConfig.FacebookBuilder().setPermissions(
                                listOf("public_profile")
                            ).build()
                        )
                    ).build()
                startActivityForResult(auth, 101)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        firebaseAuth.addAuthStateListener(firebaseAuthListener)
    }

    /**
     * Listener que escucha cuando una notificación es recibida en el dispositivo y ejecuta una acción
     * en este caso actualizamos un TextView con el contenido de la notificación, esto se hace con
     * ayuda de un objeto intent.
     */
    fun receiver() = object: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            if(intent?.action == "Notification_Action"){
                txtUpdate.text = intent.getStringExtra("text")
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Dentro del método onDestroy debemos retirar el registro al broadcast receiver, para evitar
     * un memoryLeak o que siga suscrito el broadcast cuando la vista no se encuentra visible
     */
    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver())
    }
}
