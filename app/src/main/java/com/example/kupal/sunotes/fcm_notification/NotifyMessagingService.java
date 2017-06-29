package com.example.kupal.sunotes.fcm_notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.kupal.sunotes.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by nikhiljain on 4/4/17.
 */

public class NotifyMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();
        String question_id =remoteMessage.getData().get("ques_id");
        String question_type=remoteMessage.getData().get("ques_type");
        String professor_id =remoteMessage.getData().get("prof_id");
        Log.d("title",title);

        NotificationCompat.Builder b = new NotificationCompat.Builder(this);
        b.setContentTitle(title);
        b.setContentText(message);
        b.setSmallIcon(R.drawable.notify);
        b.setAutoCancel(true);

        Intent intent =new Intent(this,StudentAnswer.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("title",title);
        intent.putExtra("message",message);
        intent.putExtra("question",message);
        intent.putExtra("question_id",question_id);
        intent.putExtra("question_type",question_type);
        intent.putExtra("professor_id",professor_id);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        b.setContentIntent(pendingIntent);
        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,b.build());
    }
}
