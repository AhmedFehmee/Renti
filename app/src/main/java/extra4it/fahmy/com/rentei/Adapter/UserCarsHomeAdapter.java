package extra4it.fahmy.com.rentei.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import extra4it.fahmy.com.rentei.Activity.User.CarDetails.CarsDetailsActivity;
import extra4it.fahmy.com.rentei.Model.AgencyModel;
import extra4it.fahmy.com.rentei.R;

import java.util.ArrayList;

/**
 * Created by Fehoo on 12/17/2017.
 */

public class UserCarsHomeAdapter extends RecyclerView.Adapter<UserCarsHomeAdapter.AgentsHolder> {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<AgencyModel.CarsEntity> carsList = new ArrayList<>();

    public UserCarsHomeAdapter(Context context, ArrayList<AgencyModel.CarsEntity> carsObjects) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        carsList = carsObjects;
    }

    @Override
    public AgentsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.car_row, parent, false);

        AgentsHolder viewHolder = new AgentsHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AgentsHolder holder, int position) {

        final AgencyModel.CarsEntity agency = carsList.get(position);

        if (!agency.getFirst_photo().equals("") && !agency.getFirst_photo().contains("http")) {
            Glide.with(context)
                    .load("http://2.extra4it.net/tajeree/" + agency.getFirst_photo())
                    .apply(new RequestOptions().placeholder(R.drawable.car_img).error(R.drawable.car_img))
                    .thumbnail(0.1f)
                    .into(holder.carImg);
        }
        holder.carName.setText(agency.getBrand_name() + "\n" + agency.getModel_name() + "");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailsIntent = new Intent(context, CarsDetailsActivity.class);
                detailsIntent.putExtra("carID", agency.getId() +"");
                context.startActivity(detailsIntent);
            }
        });
        holder.itemView.setTag(holder);
    }

    @Override
    public int getItemCount() {
        return carsList.size();
    }

    static class AgentsHolder extends RecyclerView.ViewHolder {
        ImageView carImg;
        TextView carName;

        public AgentsHolder(View itemView) {
            super(itemView);

            carImg = (ImageView) itemView.findViewById(R.id.car_img);
            carName = (TextView) itemView.findViewById(R.id.car_name);
            // bind focus listener
            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        // run scale animation and make it bigger
                        new ScaleAnimation(1, 1.1F, 1, 1,
                                Animation.RELATIVE_TO_SELF, (float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5);

                    } else {
                        // run scale animation and make it smaller
                    }
                }
            });
        }
    }
}
