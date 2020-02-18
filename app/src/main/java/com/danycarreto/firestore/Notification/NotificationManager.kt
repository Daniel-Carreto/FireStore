package com.danycarreto.firestore.Notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.danycarreto.firestore.MainActivity
import com.danycarreto.firestore.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.io.InputStream
import java.net.URL

/**
 * Clase creada con base de FirebaseMessaging (ocupada por Firebase para recibir las notificaciones)
 */
class NotificationManager : FirebaseMessagingService() {

    /**
     * Método que sobreescribimos para recibir y procesar las notificaciones recibidas de firebase
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        //Log para imprimir lo que venga en el objeto notification
        Log.d(
            NotificationManager::class.java.simpleName,
            remoteMessage.notification.toString()
        )

        //Log para imprimir lo que venga en el objeto data
        Log.d(
            NotificationManager::class.java.simpleName,
            remoteMessage.data.toString()
        )


        //Definimos un style para tener el texto amplio (más de 60 caracteres)
        val style = NotificationCompat.BigPictureStyle()
        style.setBigContentTitle(remoteMessage.data["text"])
        style.setSummaryText(remoteMessage.data["text"])

        //El style tambien nos sirve para mostrar una imagen en nuestra notificación
        //un ejemplo de una imagen (https://1.bp.blogspot.com/-dwL58chu7wo/WvD1RrHln3I/AAAAAAAAFUg/cRTc0IZga_wMPTWr3CI53IZ5BwtnZMeYACLcBGAs/s1600/Screen%2BShot%2B2018-05-05%2Bat%2B11.49.30%2BAMimage1.png)
        val imageNotification = BitmapFactory.decodeStream(
            URL(remoteMessage.data["image"])
                .content as InputStream
        )
        style.bigPicture(imageNotification)

        //Creamos un intent para mostrar una activity en especifico, este intent es utilizado
        //en una action como parte del notificationBuilder
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("body", remoteMessage.data["text"])

        //Necesitamos de un pendingIntent para manipular los intents con las notificaciones
        val pendingIntent = PendingIntent.getActivity(
            this, 201,
            intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        //Creamos un objeto tipo NotificationBuilder, para crear las notificaciones,
        //aquí definimos el style, si queremos que la notificacion tenga sonido, vibración, el
        //título, y una action (botones que ejecutan un evento en nuestra notificación
        val notificationBuilder = NotificationCompat.Builder(this, "FireStore")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(remoteMessage.data["title"])
            .setContentText(remoteMessage.data["smallText"])
            //.setStyle(NotificationCompat.BigTextStyle().bigText(remoteMessage.data["text"]))
            .setStyle(style)
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setDefaults(Notification.DEFAULT_LIGHTS)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setDefaults(Notification.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(R.mipmap.ic_launcher, "VER", pendingIntent)

        //El Manager muestra la notificacion configurada, se utiliza el timestamp para hacer que
        // la notificación sea única y no se sobreescriba, de esta manera no mostramos siempre la misma,
        //para realizar lo anterior basta con escribir un numero y no cambiarlo.
        val notification = NotificationManagerCompat.from(this)
        notification.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())


        //El LocalBroadcast nos ayuda para avisar a una actividad que ha llegado una
        //notificación, esto nos ayuda a conectar con un activity/fragment o ejecutar
        //alguna tarea en segundo plano
        val intentMessage = Intent("Notification_Action")
        intentMessage.putExtra("text", remoteMessage.data["text"])
        LocalBroadcastManager.getInstance(this).sendBroadcast(intentMessage)


    }


}