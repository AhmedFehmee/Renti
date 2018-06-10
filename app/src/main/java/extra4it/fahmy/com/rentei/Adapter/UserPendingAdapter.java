package extra4it.fahmy.com.rentei.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import extra4it.fahmy.com.rentei.Model.UserPendingModel;
import extra4it.fahmy.com.rentei.R;

public class UserPendingAdapter extends RecyclerView.Adapter<UserPendingAdapter.AgentsHolder> {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<UserPendingModel.DataEntity> pendingList = new ArrayList<>();

    public UserPendingAdapter(Context context, ArrayList<UserPendingModel.DataEntity> pendingArray) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        pendingList = pendingArray;
    }

    @Override
    public UserPendingAdapter.AgentsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.row_user_pending, parent, false);

        UserPendingAdapter.AgentsHolder viewHolder = new UserPendingAdapter.AgentsHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UserPendingAdapter.AgentsHolder holder, int position) {
        final UserPendingModel.DataEntity pendingItem = pendingList.get(position);
        holder.carName.setText(pendingItem.getCar().getName());
        holder.carDetails.setText(pendingItem.getCar().getDetails());
        holder.carPrice.setText(pendingItem.getCar().getOffice_price());
        holder.pickDate.setText(pendingItem.getBick_up_date());
        holder.dropDate.setText(pendingItem.getDrop_off_date());
        holder.carCompany.setText(pendingItem.getStatus());

        Glide.with(context)
                .load("http://2.extra4it.net/tajeree/"+pendingItem.getCar().getMain_photo())
                .apply(new RequestOptions().placeholder(R.drawable.placeholder).error(R.drawable.placeholder))
                .thumbnail(0.1f)
                .into(holder.carImage);
        holder.itemView.setTag(holder);
    }

    @Override
    public int getItemCount() {
        return pendingList.size();
    }

    static class AgentsHolder extends RecyclerView.ViewHolder {
        TextView carName;
        TextView carPrice;
        TextView carDetails ;
        TextView pickDate;
        TextView dropDate;
        TextView carCompany;
        ImageView carImage;

        public AgentsHolder(View itemView) {
            super(itemView);
            carName = (TextView) itemView.findViewById(R.id.car_name);
            carPrice = (TextView) itemView.findViewById(R.id.car_price);
            carDetails = (TextView) itemView.findViewById(R.id.car_details);
            pickDate = (TextView) itemView.findViewById(R.id.pick_up_date);
            dropDate = (TextView) itemView.findViewById(R.id.drop_off_date);
            carCompany = (TextView) itemView.findViewById(R.id.car_company);
            carImage =  (ImageView) itemView.findViewById(R.id.main_photo);

        }
    }
}

