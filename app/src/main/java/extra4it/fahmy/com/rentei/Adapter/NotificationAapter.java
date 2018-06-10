package extra4it.fahmy.com.rentei.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import extra4it.fahmy.com.rentei.Model.CityModel;
import extra4it.fahmy.com.rentei.Model.NotificationModel;
import extra4it.fahmy.com.rentei.R;

public class NotificationAapter extends RecyclerView.Adapter<NotificationAapter.AgentsHolder> {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<NotificationModel.NotificationsEntity> notificationList = new ArrayList<>();

    public NotificationAapter(Context context, ArrayList<NotificationModel.NotificationsEntity> notifivationArray ) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        notificationList = notifivationArray;
    }

    @Override
    public NotificationAapter.AgentsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.row_notification, parent, false);

        NotificationAapter.AgentsHolder viewHolder = new NotificationAapter.AgentsHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final NotificationAapter.AgentsHolder holder, final int position) {
        final NotificationModel.NotificationsEntity notification = notificationList.get(position);
        holder.notificationName.setText(notification.getTitle());

        holder.itemView.setTag(holder);
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    static class AgentsHolder extends RecyclerView.ViewHolder {
        TextView notificationName;

        public AgentsHolder(View itemView) {
            super(itemView);
            notificationName = (TextView) itemView.findViewById(R.id.notification_text);
        }
    }
}

