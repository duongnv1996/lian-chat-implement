package com.skynet.lian.ui;

import android.app.NotificationManager;
import android.content.Intent;

import com.blankj.utilcode.util.LogUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.skynet.lian.application.AppController;
import com.skynet.lian.network.socket.SocketConstants;
import com.skynet.lian.network.socket.SocketResponse;
import com.skynet.lian.utils.AppConstant;
import com.skynet.lian.utils.CommomUtils;

public class FCMService extends FirebaseMessagingService {
    String TAG = "FCM SERVICE ----- ";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        LogUtils.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            LogUtils.d(TAG, "Message data payload: " + remoteMessage.getData());

            String data = remoteMessage.getData().get("data");
            if (data != null) {
                com.skynet.lian.models.Notification notification = new Gson().fromJson(data, com.skynet.lian.models.Notification.class);
                if (notification != null && AppController.getInstance().getmSetting().getBoolean(AppConstant.NOTI_ON, true)) {
                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    Intent intent1 = new Intent();
                    intent1.setAction(SocketConstants.SOCKET_CHAT);
                    intent1.putExtra(AppConstant.MSG, new Gson().toJson(new SocketResponse()));
                    sendBroadcast(intent1);
                    LogUtils.e("push receiver update list from fcm");
                    manager.notify(0, CommomUtils.createNotificationWithMsg(this, notification.getTitle(), notification.getName(), data));
                }
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            LogUtils.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }


}