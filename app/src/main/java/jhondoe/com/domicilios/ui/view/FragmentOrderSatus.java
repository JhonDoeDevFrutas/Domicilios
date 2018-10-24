package jhondoe.com.domicilios.ui.view;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import jhondoe.com.domicilios.R;
import jhondoe.com.domicilios.common.Common;
import jhondoe.com.domicilios.data.model.entities.Solicitud;
import jhondoe.com.domicilios.data.preferences.SessionPrefs;
import jhondoe.com.domicilios.provider.FirebaseReferences;
import jhondoe.com.domicilios.ui.adapter.OrderViewHolder;
import jhondoe.com.domicilios.utilies.Uweb;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOrderSatus extends Fragment {

    // Referencias UI
    private View view;
    private RecyclerView mReciclador;
    private OrderViewHolder mAdapter;

    MaterialSpinner spinner;

    FirebaseDatabase mDatabase;
    //our database reference object
    DatabaseReference mDbReference;

    //a list to store all the orders from firebase database
    List<Solicitud> solicitudList;

    String phone = SessionPrefs.get(getActivity()).getPhone();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order_satus, container, false);

        mReciclador = (RecyclerView)view.findViewById(R.id.reciclador_orders);
        mAdapter    = new OrderViewHolder(getActivity(), new ArrayList<Solicitud>(0));

        mDatabase = FirebaseDatabase.getInstance();
        //getting the reference of node
        mDbReference = mDatabase.getReference(FirebaseReferences.ORDEN_COMPRA);

        //list to store
        solicitudList = new ArrayList<>();

        // Checks if the device has any active internet connection.
        if (!Uweb.isNetworkConnected(getActivity())){
            onShowNetWorkError(getString(R.string.error_network));
        }

        // If we start OrderSatus activity from home activity
        // We will not any extra, so we just loadOrder by phone from common
        /*
        if (getActivity().getIntent() == null){
            //loadOrders(Common.PHONE);
            phone = getActivity().getIntent().getStringExtra("userPhone");
        }
       */

        if (Common.NOTIFICATION == "request"){
            phone = getActivity().getIntent().getStringExtra("userPhone");
        }

        return view;
    }

    private void onShowNetWorkError(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart() {
        super.onStart();

        Query myQuery = mDbReference.orderByChild("phone").equalTo(phone);

        myQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clearing the previous orders list
                solicitudList.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    //getting order
                    Solicitud solicitud = postSnapshot.getValue(Solicitud.class);
                    solicitud.setId(postSnapshot.getKey());
                    //adding order to the list
                    solicitudList.add(solicitud);
                }
                //adapter
                mAdapter = new OrderViewHolder(getActivity(), solicitudList);
                mAdapter.notifyDataSetChanged();
                mAdapter.setOnItemClickListener(new OrderViewHolder.OnItemClickListener() {
                    @Override
                    public void onItemClick(Solicitud clickedOrder) {

                    }
                });

                mReciclador.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        });

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals("Update"))
            showUpdateDialog("");
        else if (item.getTitle().equals("Delete"))
            deleteOrder();

        return super.onContextItemSelected(item);
    }

    private void deleteOrder() {
    }

    private void showUpdateDialog(String key) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Update Order");
        alertDialog.setMessage("Please choose status");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.update_order_layout, null);

        spinner = (MaterialSpinner)view.findViewById(R.id.statusSpinner);
        spinner.setItems("Placed","On My way", "Shipped");

        alertDialog.setView(view);

        final String localKey = key;

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
