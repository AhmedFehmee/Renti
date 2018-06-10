package extra4it.fahmy.com.rentei.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import extra4it.fahmy.com.rentei.Activity.User.CarDetails.CarsDetailsActivity;
import extra4it.fahmy.com.rentei.Activity.User.OrderDetails.OrderDetails;
import extra4it.fahmy.com.rentei.Model.AgencyPendingOrder;
import extra4it.fahmy.com.rentei.R;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class AgencyPendingOrdersAdapter extends RecyclerView.Adapter<AgencyPendingOrdersAdapter.AgentsHolder> {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<AgencyPendingOrder.DataEntity> pendingList = new ArrayList<>();

    public AgencyPendingOrdersAdapter(Context context, ArrayList<AgencyPendingOrder.DataEntity> pendingArray) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        pendingList = pendingArray;
    }

    @Override
    public AgencyPendingOrdersAdapter.AgentsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.row_agency_orders, parent, false);

        AgencyPendingOrdersAdapter.AgentsHolder viewHolder = new AgencyPendingOrdersAdapter.AgentsHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AgencyPendingOrdersAdapter.AgentsHolder holder, int position) {
        final AgencyPendingOrder.DataEntity pendingItem = pendingList.get(position);

        if (!pendingItem.getCar().getFirst_photo().equals("")) {
            Glide.with(context)
                    .load("http://2.extra4it.net/tajeree/" + pendingItem.getCar().getFirst_photo())
                    .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher_round).error(R.drawable.car_img))
                    .thumbnail(0.1f)
                    .into(holder.carImg);
        }
        holder.userName.setText("From: " + pendingItem.getType() + "");
        holder.orderDate.setText("Date From: " + pendingItem.getBick_up_date() + "\nTo: " + pendingItem.getDrop_off_date());
        if (pendingItem.getBick_up_time() != null) {
            holder.orderTime.setText("Time From: " + pendingItem.getBick_up_time() + "\nTo: " + pendingItem.getDrop_off_time());
        }
        holder.orderPlace.setText("Place From: " + pendingItem.getBick_up_place() + "\nTo: " + pendingItem.getDrop_off_place());

        holder.carName.setText(pendingItem.getCar().getBrand_name() + " " + pendingItem.getCar().getModel_name());
        holder.carPrice.setText(pendingItem.getCar().getOffice_price() + "");
        holder.carStars.setText(pendingItem.getCar().getRate() + "");
        holder.carRate.setNumStars(5);
        holder.carRate.setRating(pendingItem.getCar().getRate());
        holder.carRate.setEnabled(false);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailsIntent = new Intent(context, OrderDetails.class);
                detailsIntent.putExtra("orderID", pendingItem.getId() + "");
                detailsIntent.putExtra("carImage1", "http://2.extra4it.net/tajeree/" + pendingItem.getCar().getFirst_photo() + "");
                detailsIntent.putExtra("carImage2", "http://2.extra4it.net/tajeree/" + pendingItem.getCar().getMain_photo() + "");
                detailsIntent.putExtra("carImage3", "http://2.extra4it.net/tajeree/" + pendingItem.getCar().getSecond_photo() + "");
                detailsIntent.putExtra("carName", pendingItem.getCar().getBrand_name() + " " +
                        pendingItem.getCar().getModel_name());
                detailsIntent.putExtra("carOfficePrice", pendingItem.getCar().getOffice_price() + "");
                detailsIntent.putExtra("carHomePrice", pendingItem.getCar().getHome_price() + "");
                detailsIntent.putExtra("carDrivePrice", pendingItem.getCar().getDriver_price() + "");
                detailsIntent.putExtra("carDetails", pendingItem.getCar().getDetails() + "");
                detailsIntent.putExtra("carRate", pendingItem.getCar().getRate() + "");

                detailsIntent.putExtra("pickTime", pendingItem.getBick_up_time() + "");
                detailsIntent.putExtra("pickPlace", pendingItem.getBick_up_place() + "");
                detailsIntent.putExtra("pickDate", pendingItem.getBick_up_date() + "");

                detailsIntent.putExtra("dropTime", pendingItem.getDrop_off_time() + "");
                detailsIntent.putExtra("dropPlace", pendingItem.getDrop_off_place() + "");
                detailsIntent.putExtra("dropDate", pendingItem.getDrop_off_date() + "");

                detailsIntent.putExtra("userName", pendingItem.getUser_name() + "");
                detailsIntent.putExtra("userPic", pendingItem.getCar().getRate() + "");
                detailsIntent.putExtra("reservationType", pendingItem.getType() + "");

                context.startActivity(detailsIntent);
            }
        });
        holder.itemView.setTag(holder);
    }

    @Override
    public int getItemCount() {
        return pendingList.size();
    }

    static class AgentsHolder extends RecyclerView.ViewHolder {
        ImageView carImg;
        TextView carName, carPrice, carStars;
        MaterialRatingBar carRate;
        TextView userName, orderDate, orderTime, orderPlace;

        public AgentsHolder(View itemView) {
            super(itemView);
            carImg = (ImageView) itemView.findViewById(R.id.car_img);
            carName = (TextView) itemView.findViewById(R.id.car_name);
            carPrice = (TextView) itemView.findViewById(R.id.car_price);
            carStars = (TextView) itemView.findViewById(R.id.car_stars);
            carRate = itemView.findViewById(R.id.car_rate_stars);

            userName = (TextView) itemView.findViewById(R.id.user_name);
            orderDate = (TextView) itemView.findViewById(R.id.car_date);
            orderTime = (TextView) itemView.findViewById(R.id.car_time);
            orderPlace = (TextView) itemView.findViewById(R.id.car_place);

        }
    }
}
