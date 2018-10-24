package jhondoe.com.domicilios.ui.view;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import jhondoe.com.domicilios.R;
import jhondoe.com.domicilios.data.model.entities.Tienda;
import jhondoe.com.domicilios.provider.FirebaseReferences;
import jhondoe.com.domicilios.ui.adapter.StoreAdapter;
import jhondoe.com.domicilios.utilies.Uweb;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentStore extends Fragment {
    public static final String STORE_ID = "storeId";

    // Referencias UI
    private View view;
    private RecyclerView mReciclador;
    private StoreAdapter mAdapter;

    //a list to store all the section from firebase database
    List<Tienda> storeList;

    //our database reference object
    DatabaseReference mDbReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_store, container, false);

        mReciclador = (RecyclerView)view.findViewById(R.id.reciclador_tienda);
        mAdapter    = new StoreAdapter(getActivity(), new ArrayList<Tienda>(0));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //getting the reference of node
        mDbReference = database.getReference(FirebaseReferences.STORE_REFERENCE);

        // Checks if the device has any active internet connection.
        if (!Uweb.isNetworkConnected(getActivity())){
            onShowNetWorkError(getString(R.string.error_network));
        }

        //list to store
        storeList = new ArrayList<>();

        return view;
    }

    private void onShowNetWorkError(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart() {
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
                mAdapter = new StoreAdapter(getActivity(), storeList);
                mAdapter.notifyDataSetChanged();
                mAdapter.setOnItemClickListener(new StoreAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Tienda clickedStore) {
                        onStore(clickedStore);

                    }
                });
                mReciclador.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void onStore(Tienda tienda) {
        Intent intentCategoryList = new Intent(getActivity(), ActivityCategory.class);
        intentCategoryList.putExtra(STORE_ID, tienda.getId());

        Intent intentProductLis = new Intent(getActivity(), ActivityProducts.class);
        intentProductLis.putExtra(STORE_ID, tienda.getId());

        startActivity(intentCategoryList);
    }
}
