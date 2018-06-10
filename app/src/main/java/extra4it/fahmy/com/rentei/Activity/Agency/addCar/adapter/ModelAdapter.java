package extra4it.fahmy.com.rentei.Activity.Agency.addCar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import extra4it.fahmy.com.rentei.Model.Model;
import extra4it.fahmy.com.rentei.R;

public class ModelAdapter extends RecyclerView.Adapter<ModelAdapter.ModelsHolder> {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Model.ModelsEntity> modelsList = new ArrayList<>();
    private ModelAdapter.OnItemClick mOnItemClickListener;

    public interface OnItemClick {
        public void onModelClick(View view, int position);
    }

    public ModelAdapter(Context context, ArrayList<Model.ModelsEntity> modelsArray ,
                         ModelAdapter.OnItemClick mOnItemClickListener) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.modelsList = modelsArray;
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public ModelsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.row_city, parent, false);

        ModelsHolder viewHolder = new ModelsHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ModelsHolder holder, final int position) {

        final Model.ModelsEntity model = modelsList.get(position);
        holder.modelName.setText(model.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onModelClick(holder.itemView , position);
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


