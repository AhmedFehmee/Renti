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

import java.util.ArrayList;

import extra4it.fahmy.com.rentei.Activity.User.CompanyDetails.CompanyDetails;
import extra4it.fahmy.com.rentei.Model.AgencyModel;
import extra4it.fahmy.com.rentei.R;

/**
 * Created by Fehoo on 3/21/2018.
 */

public class UserAgencyHomeAdapter extends RecyclerView.Adapter<UserCarsHomeAdapter.AgentsHolder> {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<AgencyModel.DataEntity> companyList = new ArrayList<>();

    public UserAgencyHomeAdapter(Context context, ArrayList<AgencyModel.DataEntity> companyObjects) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        companyList = companyObjects;
    }

    @Override
    public UserCarsHomeAdapter.AgentsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.car_row, parent, false);

        UserCarsHomeAdapter.AgentsHolder viewHolder = new UserCarsHomeAdapter.AgentsHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UserCarsHomeAdapter.AgentsHolder holder, int position) {

        final AgencyModel.DataEntity agency = companyList.get(position);
        if (!agency.getPhoto().equals("") && !agency.getPhoto().contains("http")) {
            Glide.with(context)
                    .load("http://2.extra4it.net/tajeree/" + agency.getPhoto())
                    .apply(new RequestOptions().placeholder(R.drawable.tagere_logo_pic).error(R.drawable.tagere_logo_pic))
                    .thumbnail(0.1f)
                    .into(holder.carImg);
        }

        holder.carName.setText(agency.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailsIntent = new Intent(context, CompanyDetails.class);
                detailsIntent.putExtra("companyId", agency.getId() + "");
                context.startActivity(detailsIntent);
            }
        });
        holder.itemView.setTag(holder);
    }

    @Override
    public int getItemCount() {
        return companyList.size();
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

