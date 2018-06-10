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
import extra4it.fahmy.com.rentei.Model.AgencyModel;
import extra4it.fahmy.com.rentei.R;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class CarsOfCompanyAdapter extends RecyclerView.Adapter<CarsOfCompanyAdapter.AgentsHolder> {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<AgencyModel.CarsEntity> carsList = new ArrayList<>();

    public CarsOfCompanyAdapter(Context context, ArrayList<AgencyModel.CarsEntity> carsObjects) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        carsList = carsObjects;
    }

    @Override
    public AgentsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.row_cars_of_company, parent, false);

        AgentsHolder viewHolder = new AgentsHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AgentsHolder holder, int position) {

        final AgencyModel.CarsEntity agency = carsList.get(position);

        if (!agency.getFirst_photo().equals("")) {
            Glide.with(context)
                    .load("http://2.extra4it.net/tajeree/" + agency.getFirst_photo())
                    .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher_round).error(R.drawable.car_img))
                    .thumbnail(0.1f)
                    .into(holder.carImg);
        }
        holder.carName.setText(agency.getBrand_name() + " " + agency.getModel_name());
        holder.carPrice.setText(agency.getOffice_price()+ "");
        holder.carStars.setText(agency.getRate()+"");
        holder.carRate.setNumStars(5);
        holder.carRate.setRating(agency.getRate());
        holder.carRate.setEnabled(false);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailsIntent = new Intent(context, CarsDetailsActivity.class);
                detailsIntent.putExtra("carID", agency.getId() + "");
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
        TextView carName , carPrice , carStars;
        MaterialRatingBar carRate;

        public AgentsHolder(View itemView) {
            super(itemView);

            carImg = (ImageView) itemView.findViewById(R.id.car_img);
            carName = (TextView) itemView.findViewById(R.id.car_name);
            carPrice = (TextView) itemView.findViewById(R.id.car_price);
            carStars = (TextView) itemView.findViewById(R.id.car_stars);
            carRate = itemView.findViewById(R.id.car_rate_stars);
        }
    }
}