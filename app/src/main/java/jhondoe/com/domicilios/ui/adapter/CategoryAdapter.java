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
import jhondoe.com.domicilios.data.model.entities.Categoria;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    private Context mContext;
    private List<Categoria> mItems;

    // Instancia de escucha
    private OnItemClickListener mOnItemClickListener;

    public CategoryAdapter(Context context, List<Categoria> items) {
        this.mContext = context;
        this.mItems = items;
    }

    public interface OnItemClickListener{
        void onItemClick(Categoria clickedCategoria);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener = onItemClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.item_category, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Categoria categoria = mItems.get(position);

        String descripcion  = categoria.getDescripcion().toString().trim();
        String imagen       = categoria.getImagen().toString().trim();

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

            imgView = (ImageView)itemView.findViewById(R.id.category_image);
            txtDescripcion = (TextView)itemView.findViewById(R.id.category_name);

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
