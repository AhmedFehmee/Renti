package extra4it.fahmy.com.rentei.Activity.Agency.addCar.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import extra4it.fahmy.com.rentei.R;

public class AddCarPicsAdapter extends RecyclerView.Adapter<AddCarPicsAdapter.AgentsHolder>
        implements RecyclerView.OnItemTouchListener {

    private Context context;
    private LayoutInflater inflater;
    ArrayList<Bitmap> bitmaps = new ArrayList<>();


    public AddCarPicsAdapter(Context context, ArrayList<Bitmap> picsArray ) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        bitmaps = picsArray;
    }

    @Override
    public AddCarPicsAdapter.AgentsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.row_add_car_pic, parent, false);

        AddCarPicsAdapter.AgentsHolder viewHolder = new AddCarPicsAdapter.AgentsHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AddCarPicsAdapter.AgentsHolder holder, final int position) {

        final Bitmap pic = bitmaps.get(position);

        holder.car_pic.setImageBitmap(pic);
        holder.delete_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bitmaps.remove(position);
                notifyItemChanged(position);
            }
        });
        holder.itemView.setTag(holder);
    }

    @Override
    public int getItemCount() {
        return bitmaps.size();
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
        ImageView car_pic;
        ImageView delete_car;

        public AgentsHolder(View itemView) {
            super(itemView);
            car_pic = (ImageView) itemView.findViewById(R.id.car_img);
            delete_car = (ImageView) itemView.findViewById(R.id.delete_pic);
        }
    }
}