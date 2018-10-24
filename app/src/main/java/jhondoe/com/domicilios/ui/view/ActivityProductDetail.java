package jhondoe.com.domicilios.ui.view;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import jhondoe.com.domicilios.R;
import jhondoe.com.domicilios.data.model.dao.OrdenCompraDao;
import jhondoe.com.domicilios.data.model.entities.OrdenCompra;
import jhondoe.com.domicilios.data.model.entities.Producto;
import jhondoe.com.domicilios.provider.FirebaseReferences;

public class ActivityProductDetail extends AppCompatActivity {

    // Referencias UI
    TextView txtProductName, txtProductPrice, txtProductDescription;
    ImageView imgProduct;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    private String mIdCategoria;
    private String mId;

    FirebaseDatabase mDatabase;
    DatabaseReference mReferenceProduct;

    Producto mProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Intent intentProductDetail = getIntent();
        mIdCategoria = intentProductDetail.getStringExtra(ActivityProducts.CATEGORIA_ID);
        mId = intentProductDetail.getStringExtra(ActivityProducts.PRODUCT_ID);

        //Firebase
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceProduct = mDatabase.getReference(FirebaseReferences.PRODUCT_REFERENCE).child(mIdCategoria).child(mId);

        // Init view
        prepararUI();

        getDetailProduct(mIdCategoria, mId);

    }

    private void getDetailProduct(String mIdCategoria, String mId) {
        mReferenceProduct.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProducto = dataSnapshot.getValue(Producto.class);

                // Set Image
                Picasso.with(getBaseContext()).load(mProducto.getImagen()).into(imgProduct);

                collapsingToolbarLayout.setTitle(mProducto.getNombre());

                txtProductPrice.setText(mProducto.getPrecio());

                txtProductName.setText(mProducto.getNombre());
                txtProductDescription.setText(mProducto.getDescripcion());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void prepararUI() {
        numberButton = (ElegantNumberButton)findViewById(R.id.number_button);
        btnCart = (FloatingActionButton)findViewById(R.id.btnCart);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creamos Instancia
                OrdenCompraDao ordenCompraDao = new OrdenCompraDao(ActivityProductDetail.this);

                // Abrimos Base Datos Local
                ordenCompraDao.abrirBaseDatos();

                OrdenCompra ordenCompra = new OrdenCompra();

                ordenCompra.setProductId(mProducto.getId());
                ordenCompra.setProductName(mProducto.getNombre());
                ordenCompra.setQuantity(numberButton.getNumber());
                ordenCompra.setPrice(mProducto.getPrecio());
                ordenCompra.setDiscount("0");

                boolean insert = ordenCompraDao.addToCard(ordenCompra);

                if (insert){
                    Toast.makeText(ActivityProductDetail.this, "Added to Cart ", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ActivityProductDetail.this, "Error Cart ", Toast.LENGTH_SHORT).show();
                }

                //Cerrar BaseDatos
                ordenCompraDao.cerrar();
            }
        });

        txtProductDescription = (TextView)findViewById(R.id.product_description);
        txtProductName = (TextView)findViewById(R.id.product_name);
        txtProductPrice = (TextView)findViewById(R.id.product_price);

        imgProduct = (ImageView)findViewById(R.id.img_product);
        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);
    }
}
