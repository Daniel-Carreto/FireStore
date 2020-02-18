package com.danycarreto.firestore

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.NotificationChannel
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import com.danycarreto.firestore.Notification.NotificationManager

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * Registramos un canal de notificaciones, debido a que en versiones de android 8, es obligatorio
     * tener al menos un canal registrado para recibir las notificaciones, en caso contrario no se mostrarán,
     * en este caso se llama FireStore, este canal lo podemos ver en
     * (Configuracion>Notificaciones>Firestore>Notificación (literalmente aparecerá la opciíon de
     * deshabilitar la notificacion de nombre "FireStore")
     */
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("WrongConstant")
    fun createChannel(){
        val  androidChannel =  NotificationChannel("FireStore",
            "FireStore", NotificationManagerCompat.IMPORTANCE_HIGH)
        androidChannel.enableLights(true)
        androidChannel.enableVibration(true)
        androidChannel.lightColor = Color.GREEN
        (getSystemService(Context.NOTIFICATION_SERVICE) as? android.app.NotificationManager)
            ?.createNotificationChannel(androidChannel)
    }
}
