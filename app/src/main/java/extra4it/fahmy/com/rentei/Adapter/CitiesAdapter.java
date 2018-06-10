package extra4it.fahmy.com.rentei.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Timer;

import extra4it.fahmy.com.rentei.Model.CityModel;
import extra4it.fahmy.com.rentei.R;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.AgentsHolder> implements RecyclerView.OnItemTouchListener {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<CityModel.CitiesEntity> citiesList = new ArrayList<>();
    private OnItemClick mOnItemClickListener;

    public interface OnItemClick {
        public void onCityClick(View view, int position);
        }

    public CitiesAdapter(Context context, ArrayList<CityModel.CitiesEntity> citiesArray ,
                         OnItemClick mOnItemClickListener) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        citiesList = citiesArray;
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public CitiesAdapter.AgentsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.row_city, parent, false);

        CitiesAdapter.AgentsHolder viewHolder = new CitiesAdapter.AgentsHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CitiesAdapter.AgentsHolder holder, final int position) {

        final CityModel.CitiesEntity city = citiesList.get(position);
        holder.cityName.setText(city.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onCityClick(holder.itemView , position);
            }
        });
        holder.itemView.setTag(holder);
    }

    @Override
    public int getItemCount() {
        return citiesList.size();
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    static class AgentsHolder extends RecyclerView.ViewHolder {
        TextView cityName;

        public AgentsHolder(View itemView) {
            super(itemView);
            cityName = (TextView) itemView.findViewById(R.id.tv_city_name);
        }
    }
}

