package extra4it.fahmy.com.rentei.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddCarModel {

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
        @SerializedName("rate")
        private int rate;
        @Expose
        @SerializedName("id")
        private int id;
        @Expose
        @SerializedName("second_photo")
        private String second_photo;
        @Expose
        @SerializedName("first_photo")
        private String first_photo;
        @Expose
        @SerializedName("main_photo")
        private String main_photo;
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
        @SerializedName("model_id")
        private String model_id;
        @Expose
        @SerializedName("brand_id")
        private String brand_id;
        @Expose
        @SerializedName("user_id")
        private String user_id;

        public int getRate() {
            return rate;
        }

        public void setRate(int rate) {
            this.rate = rate;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSecond_photo() {
            return second_photo;
        }

        public void setSecond_photo(String second_photo) {
            this.second_photo = second_photo;
        }

        public String getFirst_photo() {
            return first_photo;
        }

        public void setFirst_photo(String first_photo) {
            this.first_photo = first_photo;
        }

        public String getMain_photo() {
            return main_photo;
        }

        public void setMain_photo(String main_photo) {
            this.main_photo = main_photo;
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

        public String getModel_id() {
            return model_id;
        }

        public void setModel_id(String model_id) {
            this.model_id = model_id;
        }

        public String getBrand_id() {
            return brand_id;
        }

        public void setBrand_id(String brand_id) {
            this.brand_id = brand_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
    }
}
