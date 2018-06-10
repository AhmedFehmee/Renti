package extra4it.fahmy.com.rentei.Activity.Agency.addCar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import extra4it.fahmy.com.rentei.Model.BrandModel;
import extra4it.fahmy.com.rentei.R;

public class BrandsAdapter extends RecyclerView.Adapter<BrandsAdapter.AgentsHolder> {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<BrandModel.BrandsEntity> brandList ;
    private BrandsAdapter.OnItemClick mOnItemClickListener;

    public interface OnItemClick {
        public void onItemClick(View view, int position);
    }

    public BrandsAdapter(Context context, ArrayList<BrandModel.BrandsEntity> brandsArray ,
                         BrandsAdapter.OnItemClick mOnModelClickListener) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.brandList = brandsArray;
        this.mOnItemClickListener = mOnModelClickListener;
    }

    @Override
    public AgentsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.row_city, parent, false);

        AgentsHolder viewHolder = new AgentsHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AgentsHolder holder, final int position) {
        final BrandModel.BrandsEntity brand = brandList.get(position);
        holder.brandName.setText(brand.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(holder.itemView , position);
            }
        });
        holder.itemView.setTag(holder);
    }

    @Override
    public int getItemCount() {
        return brandList.size();
    }

    static class AgentsHolder extends RecyclerView.ViewHolder {
        TextView brandName;

        public AgentsHolder(View itemView) {
            super(itemView);
            brandName = (TextView) itemView.findViewById(R.id.tv_city_name);
        }
    }
}
