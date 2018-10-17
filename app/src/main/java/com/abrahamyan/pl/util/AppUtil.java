package com.abrahamyan.pl.util;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.inputmethod.InputMethodManager;

import com.abrahamyan.pl.R;

/**
 * Created by SEVAK on 15.07.2017.
 */

public class AppUtil {

    public static boolean intToBoolean(int b) {
        return (b != 0);
    }

    public static int booleanToInt(boolean b) {
        return (b) ? 1 : 0;
    }

    public static void closeKeyboard(Activity activity) {
        if (activity != null) {
            if (activity.getCurrentFocus() != null) {
                InputMethodManager inputMethodManager =
                        (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    public static void sendNotification(Context context, Class cls,
                                        String title, String description, String data, int type) {

        Intent intent = new Intent(context, cls);
        intent.putExtra(Constant.Extra.EXTRA_NOTIF_DATA, data);
        intent.putExtra(Constant.Extra.EXTRA_NOTIF_TYPE, type);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        stackBuilder.addParentStack(cls);

        stackBuilder.addNextIntent(intent);

        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(type, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constant.NotifType.NOTIFICATION_CHANNEL_ID);

        builder.setSmallIcon(android.R.drawable.sym_action_chat)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.img_drawer_android))
                .setColor(Color.GRAY)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(title)
                .setContentText(description)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(description))
                .setAutoCancel(true)
                .setContentIntent(notificationPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(Constant.NotifType.NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        assert mNotificationManager != null;
        mNotificationManager.notify(type, builder.build());
    }
}
