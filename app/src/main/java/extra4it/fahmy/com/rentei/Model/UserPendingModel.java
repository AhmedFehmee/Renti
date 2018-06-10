package extra4it.fahmy.com.rentei.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserPendingModel {

    @Expose
    @SerializedName("data")
    private List<DataEntity> data;

    public List<DataEntity> getData() {
        return data;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public static class CarEntity {
        @Expose
        @SerializedName("updated_at")
        private String updated_at;
        @Expose
        @SerializedName("created_at")
        private String created_at;
        @Expose
        @SerializedName("first_photo")
        private String first_photo;
        @Expose
        @SerializedName("second_photo")
        private String second_photo;
        @Expose
        @SerializedName("main_photo")
        private String main_photo;
        @Expose
        @SerializedName("features")
        private String features;
        @Expose
        @SerializedName("details")
        private String details;
        @Expose
        @SerializedName("driver_price")
        private String driver_price;
        @Expose
        @SerializedName("home_price")
        private String home_price;
        @Expose
        @SerializedName("office_price")
        private String office_price;
        @Expose
        @SerializedName("color")
        private String color;
        @Expose
        @SerializedName("year")
        private String year;
        @Expose
        @SerializedName("model_id")
        private int model_id;
        @Expose
        @SerializedName("brand_id")
        private int brand_id;
        @Expose
        @SerializedName("user_id")
        private int user_id;
        @Expose
        @SerializedName("name")
        private String name;
        @Expose
        @SerializedName("id")
        private int id;

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getFirst_photo() {
            return first_photo;
        }

        public void setFirst_photo(String first_photo) {
            this.first_photo = first_photo;
        }

        public String getSecond_photo() {
            return second_photo;
        }

        public void setSecond_photo(String second_photo) {
            this.second_photo = second_photo;
        }

        public String getMain_photo() {
            return main_photo;
        }

        public void setMain_photo(String main_photo) {
            this.main_photo = main_photo;
        }

        public String getFeatures() {
            return features;
        }

        public void setFeatures(String features) {
            this.features = features;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String getDriver_price() {
            return driver_price;
        }

        public void setDriver_price(String driver_price) {
            this.driver_price = driver_price;
        }

        public String getHome_price() {
            return home_price;
        }

        public void setHome_price(String home_price) {
            this.home_price = home_price;
        }

        public String getOffice_price() {
            return office_price;
        }

        public void setOffice_price(String office_price) {
            this.office_price = office_price;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public int getModel_id() {
            return model_id;
        }

        public void setModel_id(int model_id) {
            this.model_id = model_id;
        }

        public int getBrand_id() {
            return brand_id;
        }

        public void setBrand_id(int brand_id) {
            this.brand_id = brand_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class DataEntity {
        @Expose
        @SerializedName("car")
        private CarEntity car;
        @Expose
        @SerializedName("updated_at")
        private String updated_at;
        @Expose
        @SerializedName("created_at")
        private String created_at;
        @Expose
        @SerializedName("status")
        private String status;
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
        @SerializedName("drop_off_time")
        private String drop_off_time;
        @Expose
        @SerializedName("bick_up_time")
        private String bick_up_time;
        @Expose
        @SerializedName("drop_off_date")
        private String drop_off_date;
        @Expose
        @SerializedName("bick_up_date")
        private String bick_up_date;
        @Expose
        @SerializedName("car_id")
        private int car_id;
        @Expose
        @SerializedName("office_id")
        private int office_id;
        @Expose
        @SerializedName("user_id")
        private int user_id;
        @Expose
        @SerializedName("id")
        private int id;

        public CarEntity getCar() {
            return car;
        }

        public void setCar(CarEntity car) {
            this.car = car;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        public String getDrop_off_time() {
            return drop_off_time;
        }

        public void setDrop_off_time(String drop_off_time) {
            this.drop_off_time = drop_off_time;
        }

        public String getBick_up_time() {
            return bick_up_time;
        }

        public void setBick_up_time(String bick_up_time) {
            this.bick_up_time = bick_up_time;
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

        public int getCar_id() {
            return car_id;
        }

        public void setCar_id(int car_id) {
            this.car_id = car_id;
        }

        public int getOffice_id() {
            return office_id;
        }

        public void setOffice_id(int office_id) {
            this.office_id = office_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
