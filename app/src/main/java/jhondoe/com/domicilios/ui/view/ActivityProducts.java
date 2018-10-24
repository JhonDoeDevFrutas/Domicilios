package jhondoe.com.domicilios.ui.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

import jhondoe.com.domicilios.R;
import jhondoe.com.domicilios.data.model.entities.Producto;
import jhondoe.com.domicilios.provider.FirebaseReferences;
import jhondoe.com.domicilios.ui.adapter.ProductAdapter;

public class ActivityProducts extends AppCompatActivity {
    public static final String CATEGORIA_ID = "categoriaid";
    public static final String PRODUCT_ID = "productoid";

    // Referencias UI
    private RecyclerView mReciclador;
    private ProductAdapter mAdapter;

    private String mId;

    //a list to store all the product from firebase database
    List<Producto> productList;
    List<Producto> searchProduct;

    // Search Functionality
    List<String> suggestList = new ArrayList<>();
    ProductAdapter searchAdapter;
    MaterialSearchBar materialSearchBar;

    //our database reference object
    DatabaseReference mDbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        Intent intentProducts = getIntent();
        mId = intentProducts.getStringExtra(ActivityCategory.CATEGORIA_ID);

        mReciclador = (RecyclerView)findViewById(R.id.recycler_products);
        mAdapter = new ProductAdapter(this, new ArrayList<Producto>(0));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //getting the reference of product node
        mDbReference = database.getReference(FirebaseReferences.PRODUCT_REFERENCE).child(mId);

        //list to store
        productList     = new ArrayList<>();
        searchProduct   = new ArrayList<>();

        // Preparar elementos UI
        prepararUI();
    }

    private void prepararUI() {
        // Search
        materialSearchBar = (MaterialSearchBar)findViewById(R.id.productSearchBar);
        materialSearchBar.setHint("Buscar");
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // when user type their text, we will change suggest list
                List<String> suggest = new ArrayList<>();

                for (String search : suggestList) {
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                // When Search Bar is close
                // Restore original suggest adapter
                if (!enabled)
                    mReciclador.setAdapter(mAdapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                // When search finish
                // Show result of search adapter
                startSearch(text);

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    private void startSearch(CharSequence text) {
        Query myQuery = mDbReference.orderByChild("descripcion").equalTo(text.toString());
        myQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                searchProduct.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Producto producto = postSnapshot.getValue(Producto.class);

                    searchProduct.add(producto);
                }

                searchAdapter = new ProductAdapter(ActivityProducts.this, searchProduct);
                searchAdapter.notifyDataSetChanged();
                searchAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Producto clickedProduct) {

                    }
                });
                mReciclador.setAdapter(searchAdapter); // Set adapter for recycler view is search result
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        mDbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clearing the previous categorias list
                productList.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Producto producto = postSnapshot.getValue(Producto.class);

                    //adding product to the list
                    productList.add(producto);
                }

                // Adapter
                mAdapter = new ProductAdapter(getBaseContext(), productList);
                mAdapter.notifyDataSetChanged();
                mAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Producto clickedProduct) {
                        onProducto(clickedProduct);
                    }
                });

                mReciclador.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void onProducto(Producto producto) {
        Intent intentProductDetail = new Intent(getBaseContext(), ActivityProductDetail.class);
        intentProductDetail.putExtra(CATEGORIA_ID, mId);
        intentProductDetail.putExtra(PRODUCT_ID, producto.getId());
        startActivity(intentProductDetail);

    }
}
