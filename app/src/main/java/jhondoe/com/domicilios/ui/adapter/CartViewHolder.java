package jhondoe.com.domicilios.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import jhondoe.com.domicilios.R;
import jhondoe.com.domicilios.data.model.entities.OrdenCompra;

public class CartViewHolder extends RecyclerView.Adapter<CartViewHolder.ViewHolder>{
    private Context mContext;
    private List<OrdenCompra> mItems;

    public CartViewHolder(Context context, List<OrdenCompra> items){
        this.mContext = context;
        this.mItems = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView imageView;
        public TextView txtName;
        public TextView txtPrice;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView   = (ImageView)itemView.findViewById(R.id.img_cart_item_count);
            txtName     = (TextView)itemView.findViewById(R.id.cart_item_name);
            txtPrice    = (TextView)itemView.findViewById(R.id.cart_item_price);
        }

        @Override
        public void onClick(View v) {

        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.cart_layout, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrdenCompra order = mItems.get(position);

        //TextDrawable drawable = TextDrawable.builder().buildRound(""+mItems.get(position).getQuantity(), Color.RED);
        TextDrawable drawable = TextDrawable.builder().buildRound(""+order.getQuantity(), Color.RED);

        holder.txtName.setText(order.getProductName().toString());
        //holder.txtPrice.setText(order.getPrice());
        holder.imageView.setImageDrawable(drawable);

        Locale locale = new Locale("en","US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        holder.txtPrice.setText(fmt.format(Integer.parseInt(order.getPrice() != null ? order.getPrice() : "1")));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


}
