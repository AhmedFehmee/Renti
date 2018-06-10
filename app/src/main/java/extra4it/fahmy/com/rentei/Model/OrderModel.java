package extra4it.fahmy.com.rentei.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderModel {

    @Expose
    @SerializedName("data")
    private DataEntity data;
    @Expose
    @SerializedName("success")
    private int success;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public static class DataEntity {
        @Expose
        @SerializedName("id")
        private int id;
        @Expose
        @SerializedName("created_at")
        private String created_at;
        @Expose
        @SerializedName("updated_at")
        private String updated_at;
        @Expose
        @SerializedName("status")
        private String status;
        @Expose
        @SerializedName("office_id")
        private int office_id;
        @Expose
        @SerializedName("type")
        private String type;
        @Expose
        @SerializedName("drop_off_place")
        private String drop_off_place;
        @Expose
        @SerializedName("bick_up_place")
        private String bick_up_place;
        @Expose
        @SerializedName("drop_off_date")
        private String drop_off_date;
        @Expose
        @SerializedName("bick_up_date")
        private String bick_up_date;
        @Expose
        @SerializedName("car_id")
        private String car_id;
        @Expose
        @SerializedName("user_id")
        private String user_id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getOffice_id() {
            return office_id;
        }

        public void setOffice_id(int office_id) {
            this.office_id = office_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDrop_off_place() {
            return drop_off_place;
        }

        public void setDrop_off_place(String drop_off_place) {
            this.drop_off_place = drop_off_place;
        }

        public String getBick_up_place() {
            return bick_up_place;
        }

        public void setBick_up_place(String bick_up_place) {
            this.bick_up_place = bick_up_place;
        }

        public String getDrop_off_date() {
            return drop_off_date;
        }

        public void setDrop_off_date(String drop_off_date) {
            this.drop_off_date = drop_off_date;
        }

        public String getBick_up_date() {
            return bick_up_date;
        }

        public void setBick_up_date(String bick_up_date) {
            this.bick_up_date = bick_up_date;
        }

        public String getCar_id() {
            return car_id;
        }

        public void setCar_id(String car_id) {
            this.car_id = car_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
    }
}
