package jhondoe.com.domicilios.ui.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import jhondoe.com.domicilios.R;
import jhondoe.com.domicilios.common.Common;
import jhondoe.com.domicilios.data.model.dao.OrdenCompraDao;
import jhondoe.com.domicilios.data.model.entities.MyResponse;
import jhondoe.com.domicilios.data.model.entities.Notification;
import jhondoe.com.domicilios.data.model.entities.OrdenCompra;
import jhondoe.com.domicilios.data.model.entities.Sender;
import jhondoe.com.domicilios.data.model.entities.Solicitud;
import jhondoe.com.domicilios.data.model.entities.Token;
import jhondoe.com.domicilios.data.preferences.SessionPrefs;
import jhondoe.com.domicilios.provider.FirebaseReferences;
import jhondoe.com.domicilios.remote.APIService;
import jhondoe.com.domicilios.ui.adapter.CartViewHolder;
import jhondoe.com.domicilios.utilies.Uweb;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCart extends AppCompatActivity {

    // Creamos Instancia
    OrdenCompraDao ordenCompraDao ;

    // Referencias UI
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private CartViewHolder mAdapter;

    FirebaseDatabase mDatabase;
    //our database reference object
    DatabaseReference mDbReference;

    TextView txtTotalPrice;
    Button btnPlaceOrder;

    List<OrdenCompra> carts;

    APIService mService;

    int mTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Init service
        mService = Common.getFCMService();

        mDatabase = FirebaseDatabase.getInstance();
        mDbReference = mDatabase.getReference(FirebaseReferences.ORDEN_COMPRA);

        // Init view
        prepararUI();

        onBringData();
    }

    private void onBringData() {
        // Creamos Instancia
        ordenCompraDao = new OrdenCompraDao(ActivityCart.this);

        // Abrimos Base Datos Local
        ordenCompraDao.abrirBaseDatos();

        carts = ordenCompraDao.getCarts();
        loadListProducts();
    }

    private void loadListProducts() {
        mAdapter = new CartViewHolder(ActivityCart.this, carts);
        recyclerView.setAdapter(mAdapter);

        // Calculate total price
        mTotal = 0;

        for (OrdenCompra orden : carts) {
            mTotal += orden.getPrice() != null ? (Integer.parseInt(orden.getQuantity())) * (Integer.parseInt(orden.getPrice())) : 1 *  (Integer.parseInt(orden.getQuantity()));

            Locale locale = new Locale("en","US");
            NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

            txtTotalPrice.setText(fmt.format(mTotal));
            //txtTotalPrice.setText(Integer.toString(total));
        }
    }

    private void prepararUI() {
        recyclerView = (RecyclerView)findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = (TextView)findViewById(R.id.txtTotal);
        btnPlaceOrder = (Button)findViewById(R.id.btnPlaceOrder);

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTotal > 0){
                    showAlertDialog();
                }

            }
        });

    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActivityCart.this);
        alertDialog.setTitle("Un paso más!");
        alertDialog.setMessage("Ingrese su Dirección");
        /*
        final EditText edtAddress = new EditText(ActivityCart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        edtAddress.setLayoutParams(lp);

        */

        LayoutInflater inflater = this.getLayoutInflater();
        View order_address_comment = inflater.inflate(R.layout.order_address_comment, null);

        final EditText edtAddress = (EditText) order_address_comment.findViewById(R.id.edt_address);
        final EditText edtComment = (EditText) order_address_comment.findViewById(R.id.edt_comment);

        alertDialog.setView(order_address_comment); //
        alertDialog.setIcon(R.drawable.ic_shopping_cart);

        alertDialog.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                sendMessagingWhatsapp();

                // Checks if the device has any active internet connection.
                if (Uweb.isNetworkConnected(ActivityCart.this)){
                    sendNotification(edtAddress.getText().toString(),edtComment.getText().toString());
                }
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();

    }

    private void sendNotification(String address, String comment) {
        //getting a unique id using push().getKey() method
        //it will create a unique id and we will use it as the Primary Key for our Order
        String id = mDbReference.push().getKey();
        String phone = SessionPrefs.get(getBaseContext()).getPhone();

        // Create new Request
        Solicitud solicitud = new Solicitud(
                phone,
                "Test Name Client",
                address,
                txtTotalPrice.getText().toString(),
                comment,
                carts);

        // submit to firebase
        // we will using System.CurrentMilli to key
        //Saving
        String order_number = String.valueOf(System.currentTimeMillis());
        mDbReference.child(String.valueOf(System.currentTimeMillis())).setValue(solicitud);
        // Delete cart
        ordenCompraDao.cleanCart();

        sendNotificationOrder(order_number);
        //Toast.makeText(this, "thank you, Order Place", Toast.LENGTH_SHORT).show();
        Toast.makeText(ActivityCart.this, "Hemos recibido, TU PEDIDO", Toast.LENGTH_SHORT).show();
        finish();

    }

    private void sendMessagingWhatsapp() {
        String lcNombre = "Tío";
        String lcNumber = "+34695723938";
        //lcNumber = "+573154280732";
        lcNumber = lcNumber.replace("+", "").replace(" ", "");

        String msj = "Hola " + lcNombre + ", Pedido por colocar. Presentarse 30 minutos antes ";

        Intent sendIntent = new Intent("android.intent.action.MAIN");
        sendIntent.putExtra("jid", lcNumber + "@s.whatsapp.net");
        sendIntent.putExtra(Intent.EXTRA_TEXT, msj);
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setPackage("com.whatsapp");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void sendNotificationOrder(final String order_number) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference(FirebaseReferences.TOKENS);
        Query data = tokens.orderByChild("isServerToken").equalTo(true); // get all node with
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token serverToken = snapshot.getValue(Token.class);

                    // Create raw payload to send
                    Notification notification = new Notification("JHONDOE Dev", "You have new order "+order_number);
                    Sender content = new Sender(serverToken.getToken(), notification);

                    mService.sendNotification(content)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200){
                                        if (response.body().succes == 1){
                                            Toast.makeText(ActivityCart.this, "thank you, Order Place", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(ActivityCart.this, "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                    Log.e("ERROR", t.getMessage());
                                }
                            });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
