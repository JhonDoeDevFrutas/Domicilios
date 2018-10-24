package jhondoe.com.domicilios.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import jhondoe.com.domicilios.R;
import jhondoe.com.domicilios.common.Common;
import jhondoe.com.domicilios.data.model.entities.Tienda;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {
    private Context mContext;
    private List<Tienda> mItems;

    // Instancia de escucha
    private OnItemClickListener mOnItemClickListener;
    private OnItemSelectedListener mOnItemSelectedListener;

    public StoreAdapter(Context context, List<Tienda> items) {
        this.mContext = context;
        this.mItems = items;
    }

    public interface OnItemClickListener{
        void onItemClick(Tienda clickedStore);
    }

    public interface  OnItemSelectedListener{
        void onMenuAction(Tienda selectStore, MenuItem item);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener){
        mOnItemSelectedListener = onItemSelectedListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        View view = layoutInflater.inflate(R.layout.item_store, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Tienda store = mItems.get(position);

        String descripcion  = store.getDescripcion().toString().trim();
        String image        = store.getImagen().toString().trim();

        holder.txtDescripcion.setText(descripcion);
        Picasso.with(mContext).load(image).into(holder.imgView);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnCreateContextMenuListener, PopupMenu.OnMenuItemClickListener{
        public ImageView imgView;
        public TextView txtDescripcion;

        public ViewHolder(View itemView) {
            super(itemView);

            imgView         = (ImageView)itemView.findViewById(R.id.store_image);
            txtDescripcion  = (TextView) itemView.findViewById(R.id.text_descripcion);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            if (position != RecyclerView.NO_POSITION){
                mOnItemClickListener.onItemClick(mItems.get(position));
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            popup.getMenuInflater().inflate(R.menu.custom_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(this);
            popup.show();

            /*
            MenuItem edit = menu.add(Menu.NONE, 1, 1, Common.UPDATE);
            MenuItem delete = menu.add(Menu.NONE, 2, 2, Common.DELETE);
            edit.setOnMenuItemClickListener(onEditMenu);
            delete.setOnMenuItemClickListener(onEditMenu);
            */
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int position = getAdapterPosition();

            if (position != RecyclerView.NO_POSITION){
                mOnItemSelectedListener.onMenuAction(mItems.get(position), item);
            }

            return false;
        }

/*
        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){
                    case R.id.menu_custom_edit:
                        Toast.makeText(mContext, "Actualizar", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.menu_custom_delete:
                        Toast.makeText(mContext, "Eliminar", Toast.LENGTH_LONG).show();
                        break;
                }

                return true;
            }
        };
*/
    }


}
