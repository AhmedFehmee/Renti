package extra4it.fahmy.com.rentei.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import extra4it.fahmy.com.rentei.Activity.Agency.addCar.adapter.ModelAdapter;
import extra4it.fahmy.com.rentei.Model.Model;
import extra4it.fahmy.com.rentei.R;

public class YearAdapter extends RecyclerView.Adapter<YearAdapter.ModelsHolder> {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Model.ModelsEntity> modelsList = new ArrayList<>();
    private YearAdapter.OnItemClick mOnItemClickListener;

    public interface OnItemClick {
        public void onYearClick(View view, int position);
    }

    public YearAdapter(Context context, ArrayList<Model.ModelsEntity> modelsArray ,
                        YearAdapter.OnItemClick mOnItemClickListener) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.modelsList = modelsArray;
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public YearAdapter.ModelsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.row_city, parent, false);

        YearAdapter.ModelsHolder viewHolder = new YearAdapter.ModelsHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final YearAdapter.ModelsHolder holder, final int position) {

        final Model.ModelsEntity model = modelsList.get(position);
        holder.modelName.setText(model.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onYearClick(holder.itemView , position);
            }
        });
        holder.itemView.setTag(holder);
    }

    @Override
    public int getItemCount() {
        return modelsList.size();
    }

    static class ModelsHolder extends RecyclerView.ViewHolder {
        TextView modelName;

        public ModelsHolder(View itemView) {
            super(itemView);
            modelName = (TextView) itemView.findViewById(R.id.tv_city_name);
        }
    }
}


