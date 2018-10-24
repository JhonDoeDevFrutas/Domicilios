package jhondoe.com.domicilios.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.List;

import jhondoe.com.domicilios.R;
import jhondoe.com.domicilios.data.model.entities.Solicitud;

public class OrderViewHolder extends RecyclerView.Adapter<OrderViewHolder.ViewHolder>{
    private Context mContext;
    private List<Solicitud> mItems;

    // Instancia de escucha
    private OnItemClickListener mOnItemClickListener;


    public OrderViewHolder(Context context, List<Solicitud> items){
        this.mContext = context;
        this.mItems = items;
    }

    public interface OnItemClickListener{
        void onItemClick(Solicitud clickedOrder);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.order_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Solicitud order = mItems.get(position);

        holder.txtOrderId.setText(order.getId());
        holder.txtOrderStatus.setText(convertCodeStatus(order.getStatus() != null ? order.getStatus() : "0") );
        holder.txtOrderPhone.setText(order.getPhone());
        holder.txtOrderAddress.setText(order.getAddress());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView txtOrderId, txtOrderStatus, txtOrderPhone, txtOrderAddress;

        public ViewHolder(View itemView) {
            super(itemView);

            txtOrderId = (TextView)itemView.findViewById(R.id.order_id);
            txtOrderStatus = (TextView)itemView.findViewById(R.id.order_status);
            txtOrderPhone = (TextView)itemView.findViewById(R.id.order_phone);
            txtOrderAddress = (TextView)itemView.findViewById(R.id.order_address);

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

    private String convertCodeStatus(String status){
        if (status.equals("0"))
            return "Pedido Realizado";
        else if (status.equals("1"))
            return "En camino";
        else
            return "Enviado";
    }
}
