package jhondoe.com.domicilios.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import jhondoe.com.domicilios.R;
import jhondoe.com.domicilios.data.model.entities.Producto;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{

    private Context mContext;
    private List<Producto> mItems;

    // Instancia de escucha
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(Producto clickedProduct);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener = onItemClickListener;
    }

    public ProductAdapter(Context context, List<Producto> items) {
        this.mContext = context;
        this.mItems = items;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.item_product, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Producto producto = mItems.get(position);

        String descripcion  = producto.getDescripcion().toString().trim();
        String imagen       = producto.getImagen().toString().trim();

        Picasso.with(mContext).load(imagen).into(holder.imgView);
        holder.txtDescripcion.setText(descripcion);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView imgView;
        public TextView txtDescripcion;

        public ViewHolder(View view) {
            super(view);

            imgView = (ImageView)itemView.findViewById(R.id.product_image);
            txtDescripcion = (TextView)itemView.findViewById(R.id.product_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            if (position != RecyclerView.NO_POSITION){
                mOnItemClickListener.onItemClick(mItems.get(position));
            }
        }
    }
}
