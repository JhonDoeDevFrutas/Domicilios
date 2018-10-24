package jhondoe.com.domicilios.ui.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;
import jhondoe.com.domicilios.R;
import jhondoe.com.domicilios.data.preferences.SessionPrefs;

public class ActivityRegister extends AppCompatActivity {

    // Referencias UI
    EditText edtNumber;
    Button btnNext;
    CheckBox ckbRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Preparar elementos UI
        prepararUI();
    }

    private void prepararUI() {

        edtNumber = (EditText)findViewById(R.id.edt_number);

        btnNext = (Button)findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = edtNumber.getText().toString();

                // Save user & password
                if (ckbRemember.isChecked()){
                    // Guardar usuario en preferencias
                    SessionPrefs.get(ActivityRegister.this).saveUser(number);

                    //Paper.book().write(SessionPrefs.PHONE, number);
                    //Paper.book().write(SessionPrefs.USER_KEY, "user");
                    //Paper.book().write(SessionPrefs.PWD_KEY, "key");
                }

                Intent intentHome = new Intent(ActivityRegister.this, Home.class);
                startActivity(intentHome);
            }
        });

        ckbRemember = (CheckBox)findViewById(R.id.ckbRemember) ;
    }
}
