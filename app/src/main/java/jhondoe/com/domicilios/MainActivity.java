package jhondoe.com.domicilios;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Random;

import jhondoe.com.domicilios.data.preferences.SessionPrefs;
import jhondoe.com.domicilios.ui.view.ActivityRegister;
import jhondoe.com.domicilios.ui.view.Home;

public class MainActivity extends AppCompatActivity {

    TextView txtSlogan, txtLema;
    ImageView imgBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendNotification();
        if (FirebaseInstanceId.getInstance().getToken() != null){
            Log.i("Token MainActivity", FirebaseInstanceId.getInstance().getToken());
        }

        // Init view
        prepararUI();

        startPage();
    }

    private void sendNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification.Builder notificationBuilder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("Hola!")
                .setContentText("30% de DCTO en GEF, Punto Blanco y Baby Fresh, pagando con el cupo de credito de COOMEVA")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String chanelId = getString(R.string.normal_channel_id);
            String chanelName = getString(R.string.normal_channel_name);
            NotificationChannel channel = new NotificationChannel(chanelId, chanelName,
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 200, 50});
            if (notificationManager != null){
                notificationManager.createNotificationChannel(channel);
            }

            notificationBuilder.setChannelId(chanelId);
        }

        if (notificationManager != null){
            notificationManager.notify("", 0, notificationBuilder.build());
        }
    }

    private void prepararUI() {

        imgBackground = (ImageView)findViewById(R.id.img_background);

        txtSlogan   = (TextView)findViewById(R.id.txt_slogan);
        txtLema     = (TextView)findViewById(R.id.txt_lema);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/NABILA.TTF");
        txtSlogan.setTypeface(typeface);
        txtLema.setTypeface(typeface);

        //Generador de numeros aleatorios
        Random generadorAleatorios = new Random();
        //genera un numero entre 1 y 5 y lo guarda en la variable numeroAleatorio
        int numeroAleatorio = 1+generadorAleatorios.nextInt(6);
        switch (numeroAleatorio){
            case 1:
                imgBackground.setImageResource(R.drawable.background01);
                break;
            case 2:
                imgBackground.setImageResource(R.drawable.background02);
                break;
            case 3:
                imgBackground.setImageResource(R.drawable.background03);
                break;
            case 4:
                imgBackground.setImageResource(R.drawable.background04);
                break;
            case 5:
                imgBackground.setImageResource(R.drawable.background05);
                break;
            case 6:
                imgBackground.setImageResource(R.drawable.background06);
                break;
            default:
                imgBackground.setImageResource(R.drawable.background);
                break;
        }

        Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.mytransition);
        txtSlogan.startAnimation(myAnim);
        txtLema.startAnimation(myAnim);

    }

    private void startPage(){
        final Intent intentLogin  = new Intent(this, ActivityRegister.class);
        final Intent intentHome   = new Intent(this, Home.class);

        Thread timer = new Thread(){
            public void run(){
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // Redirección al Login
                    if (!SessionPrefs.get(MainActivity.this).isLoggedIn()){
                        startActivity(intentLogin);
                    } else { // Redirección al Menu
                        startActivity(intentHome);
                    }

                    finish();
                    return;
                }
            }
        };
        timer.start();
    }

    private void startLoginPage() {
        Intent intentRegister = new Intent(MainActivity.this, ActivityRegister.class);
        startActivity(intentRegister);
    }

    private void showRegisterDialog(String phone){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("REGISTRAR");

        LayoutInflater inflater = this.getLayoutInflater();
        View register_layout = inflater.inflate(R.layout.register_layout, null);

        EditText edtName = (EditText)register_layout.findViewById(R.id.edt_name);
        final EditText edtAddress = (EditText)register_layout.findViewById(R.id.edt_address);
        EditText edtBirtDate = (EditText)register_layout.findViewById(R.id.edt_birthdate);

        Button btnRegister = (Button)register_layout.findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtAddress.getText().toString())){
                    Toast.makeText(MainActivity.this, "Please enter your address", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }}
