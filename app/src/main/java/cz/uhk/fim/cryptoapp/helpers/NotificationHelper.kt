package cz.uhk.fim.cryptoapp.helpers

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import cz.uhk.fim.cryptoapp.MainActivity
import cz.uhk.fim.cryptoapp.R

class NotificationHelper(private val context: Context) {

    //pomocí companion object vytvoříme statické proměnné
    companion object {
        const val CHANNEL_ID = "crypto_channel"
        var notificationCounter = 1
    }

    //při vzniku nové instance (zavolání konstruktoru) se provede inicializace
    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //kanály jsou k dispozici až od API 26
            val name = "Crypto Notifications"
            val descriptionText = "Notifications about crypto prices"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(title: String, message: String) {

        ///umožníme, aby se při kliknutí na notifikaci otevřela naše aplikace, konkrétně MainActivity
        //intenty se typicky používají pro otevírání jiných apliakcí či systémových aplikací jako kamera, galerie, správce souborů atd.
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            //pokud neběží spustí se jako nová task, pokud běží vyčistí stack a vytvoří novou instanci
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0, //používáme se pokud chceme posléze z intentu zpracovat nějaký výsledek,
            // zkontrolujeme shodu request kodu a patřičně zareagujeme, zde nepotřebujeme, takže dáme 0
            intent, //předáem vytvořený intent
            PendingIntent.FLAG_IMMUTABLE
        )


        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(context, CHANNEL_ID)
        } else {
            NotificationCompat.Builder(context)
        }
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {//opět zkontrolujeme oprávnění
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
            ) {
                return //pokud nemáme ukončíme vykonávání funkce
            }
            notify(notificationCounter++, builder.build()) //jinak notifikujeme
        }
    }
}