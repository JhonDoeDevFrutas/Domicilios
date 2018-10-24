package jhondoe.com.domicilios.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import jhondoe.com.domicilios.R;
import jhondoe.com.domicilios.common.Common;
import jhondoe.com.domicilios.data.model.entities.Solicitud;
import jhondoe.com.domicilios.data.preferences.SessionPrefs;
import jhondoe.com.domicilios.provider.FirebaseReferences;
import jhondoe.com.domicilios.ui.view.Home;

public class ListenOrder extends Service implements ChildEventListener{

    FirebaseDatabase mDatabase;
    //our database reference object
    DatabaseReference mDbReference;

    public ListenOrder() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mDatabase = FirebaseDatabase.getInstance();
        //getting the reference of node
        mDbReference = mDatabase.getReference(FirebaseReferences.ORDEN_COMPRA);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mDbReference.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        // Trigger here
        Solicitud solicitud = dataSnapshot.getValue(Solicitud.class);
        String phone        = SessionPrefs.get(getBaseContext()).getPhone();

        if (solicitud.getPhone().equals(phone)){
            showNotification(dataSnapshot.getKey(), solicitud);
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    private void showNotification(String key, Solicitud solicitud) {
        Common.NOTIFICATION = "request";

        Intent intent = new Intent(getBaseContext(), Home.class);
        intent.putExtra("userPhone", solicitud.getPhone()); // we need put user phone, why ? I'll talk late

        PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(), 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("JHONDOEDev")
                .setContentInfo("Su orden fue actualizada")
                .setContentText("Pedido #" + key+" estado actualizado a " + Common.convertCodeToStatus(solicitud.getStatus()))
                .setContentIntent(contentIntent)
                .setContentInfo("Info")
                .setSmallIcon(R.mipmap.ic_launcher);

        NotificationManager notificationManager = (NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

}
