package jhondoe.com.domicilios.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FcmIdService extends FirebaseInstanceIdService {
    public FcmIdService() {
    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        
        sendRegistrationToServer(refreshToken);
    }

    private void sendRegistrationToServer(String token) {
        Log.d("Token", token);
    }
}
