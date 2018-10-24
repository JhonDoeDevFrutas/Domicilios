package jhondoe.com.domicilios.ui.view;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

import jhondoe.com.domicilios.R;
import jhondoe.com.domicilios.data.model.entities.Categoria;
import jhondoe.com.domicilios.provider.FirebaseReferences;
import jhondoe.com.domicilios.ui.adapter.CategoryAdapter;

public class ActivityCategory extends AppCompatActivity {
    public static final String CATEGORIA_ID = "categoriaid";

    // Referencias UI
    private RecyclerView mReciclador;
    private CategoryAdapter mAdapter;

    private String mId;

    //a list to store all the product from firebase database
    List<Categoria> categoriaList;

    // Search Functionality
    List<String> suggestList = new ArrayList<>();

    //our database reference object
    DatabaseReference mDbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Intent intentCategoryList = getIntent();
        mId = intentCategoryList.getStringExtra(FragmentStore.STORE_ID);

        mReciclador = (RecyclerView)findViewById(R.id.recycler_category);
        mAdapter = new CategoryAdapter(this, new ArrayList<Categoria>(0));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //getting the reference of category node
        mDbReference = database.getReference(FirebaseReferences.CATEGORY_REFERENCE).child(mId);

        //list to store
        categoriaList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //attaching value event listener
        mDbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                categoriaList.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting
                    Categoria categoryTmp = postSnapshot.getValue(Categoria.class);

                    //adding section to the list
                    categoriaList.add(categoryTmp);
                }

                //adapter
                mAdapter = new CategoryAdapter(ActivityCategory.this, categoriaList);
                mAdapter.notifyDataSetChanged();;
                mAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Categoria clickedCategoria) {
                        onCategory(clickedCategoria);

                    }
                });
                mReciclador.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void onCategory(Categoria categoria) {
        Intent intentProducts = new Intent(ActivityCategory.this, ActivityProducts.class);
        intentProducts.putExtra(CATEGORIA_ID, categoria.getId());
        startActivity(intentProducts);
    }
}
