package jhondoe.com.domicilios.ui.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import jhondoe.com.domicilios.R;
import jhondoe.com.domicilios.data.model.entities.Solicitud;
import jhondoe.com.domicilios.data.model.entities.Tienda;
import jhondoe.com.domicilios.provider.FirebaseReferences;
import jhondoe.com.domicilios.ui.adapter.StoreAdapter;
import jhondoe.com.domicilios.utilies.Uweb;

public class ActivityStore extends AppCompatActivity {
    public static final String STORE_ID = "storeId";

    // Referencias UI
    private RecyclerView mReciclador;
    private StoreAdapter mAdapter;

    MaterialSpinner spinner;

    //a list to store all the section from firebase database
    List<Tienda> storeList;

    //our database reference object
    DatabaseReference mDbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Preparar elementos UI
        mReciclador = (RecyclerView)findViewById(R.id.recycler_store);
        mAdapter = new StoreAdapter(getBaseContext(), new ArrayList<Tienda>(0));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //getting the reference of node
        mDbReference = database.getReference(FirebaseReferences.STORE_REFERENCE);

        // Checks if the device has any active internet connection.
        if (!Uweb.isNetworkConnected(getBaseContext())){
            onShowNetWorkError(getString(R.string.error_network));
        }

        //list to store
        storeList = new ArrayList<>();


        prepararFab();
        prepararUI();
    }

    private void onShowNetWorkError(String error) {
        Toast.makeText(getBaseContext(), error, Toast.LENGTH_LONG).show();
    }


    private void prepararUI() {
    }

    private void prepararFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_store);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        //attaching value event listener
        mDbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                storeList.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting
                    Tienda storeTmp = postSnapshot.getValue(Tienda.class);

                    //adding section to the list
                    storeList.add(storeTmp);
                }

                //adapter
                mAdapter = new StoreAdapter(getBaseContext(), storeList);
                mAdapter.notifyDataSetChanged();
                mAdapter.setOnItemClickListener(new StoreAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Tienda clickedStore) {
                        showAlertDialog(clickedStore);
                    }
                });

                mAdapter.setOnItemSelectedListener(new StoreAdapter.OnItemSelectedListener() {
                    @Override
                    public void onMenuAction(Tienda selectStore, MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menu_custom_edit:
                                showUpdateDialog();
                                break;
                            case R.id.menu_custom_delete:
                                deleteOrder();
                                break;
                        }
                    }
                });

                mReciclador.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        
    }

    private void showAlertDialog(final Tienda tienda) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActivityStore.this);
        alertDialog.setTitle("Modificar");
        alertDialog.setMessage("Ingrese Descripción");

        final EditText edtDescription = new EditText(ActivityStore.this);
        edtDescription.setText(tienda.getDescripcion().toString());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        edtDescription.setLayoutParams(lp);
        alertDialog.setView(edtDescription); // Add edit text to alert dialog
        alertDialog.setIcon(R.drawable.ic_edit);

        alertDialog.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String description = edtDescription.getText().toString();

                if (!TextUtils.isEmpty(description)){
                    // Checks if the device has any active internet connection.
                    if (Uweb.isNetworkConnected(ActivityStore.this)){
                        //getting the specified store reference
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        //getting the reference of node
                        String id           = tienda.getId();
                        String imagen       = tienda.getImagen().toString();
                        mDbReference = database.getReference(FirebaseReferences.STORE_REFERENCE).child(id);

                        //updating
                        Tienda tiendaTmp = new Tienda(id, description, imagen);
                        mDbReference.setValue(tiendaTmp);
                        Toast.makeText(ActivityStore.this, "Registro Actualizado", Toast.LENGTH_SHORT).show();
                        //finish();
                        dialog.dismiss();

                    }
                }


            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void deleteOrder() {

    }

    private void showUpdateDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActivityStore.this);
        alertDialog.setTitle("Update Order");
        alertDialog.setMessage("Please choose status");

        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.update_order_layout, null);

        spinner = (MaterialSpinner)view.findViewById(R.id.statusSpinner);
        spinner.setItems("Placed","On My way", "Shipped");

        alertDialog.setView(view);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Solicitud solicitudTmp = new Solicitud();
                solicitudTmp.setStatus(String.valueOf(spinner.getSelectedIndex()));

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

}
