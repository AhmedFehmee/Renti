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

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.AgentsHolder> {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<AgencyModel.DataEntity> carsList = new ArrayList<>();

    public SearchAdapter(Context context, ArrayList<AgencyModel.DataEntity> carsObjects) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        carsList = carsObjects;
    }

    @Override
    public AgentsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.row_company, parent, false);

        AgentsHolder viewHolder = new AgentsHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AgentsHolder holder, int position) {

        final AgencyModel.DataEntity agency = carsList.get(position);

        if (!agency.getPhoto().equals("")) {
            Glide.with(context)
                    .load("http://2.extra4it.net/tajeree/" + agency.getPhoto())
                    .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher_round).error(R.drawable.car_img))
                    .thumbnail(0.1f)
                    .into(holder.companyImg);
        }
        holder.companyName.setText(agency.getName());
        holder.companyTime.setText(agency.getStart() + " to " + agency.getEnd());
        holder.companyPhone.setText(agency.getPhone());
        holder.companyRate.setNumStars(5);
        //holder.companyRate.setRating(agency.getRate());
        holder.companyRate.setEnabled(false);
        holder.companyLocation.setText(agency.getAddress());

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
        ImageView companyImg;
        TextView companyName , companyLocation , companyStars , companyPhone , companyTime;
        MaterialRatingBar companyRate;

        public AgentsHolder(View itemView) {
            super(itemView);

            companyImg = (ImageView) itemView.findViewById(R.id.company_img);
            companyName = (TextView) itemView.findViewById(R.id.company_name);
            companyPhone = (TextView) itemView.findViewById(R.id.company_phone);
            companyTime = (TextView) itemView.findViewById(R.id.company_work_time);
            companyLocation = (TextView) itemView.findViewById(R.id.company_address);
            companyStars = (TextView) itemView.findViewById(R.id.textView4);
            companyRate = itemView.findViewById(R.id.company_rate_stars);
        }
    }
}
