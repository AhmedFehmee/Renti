package extra4it.fahmy.com.rentei.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Fehoo on 1/31/2018.
 */

public class CarModel {

    @Expose
    @SerializedName("data")
    private List<DataEntity> data;

    public List<DataEntity> getData() {
        return data;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public static class UserEntity {
        @Expose
        @SerializedName("updated_at")
        private String updated_at;
        @Expose
        @SerializedName("created_at")
        private String created_at;
        @Expose
        @SerializedName("country_id")
        private int country_id;
        @Expose
        @SerializedName("end")
        private String end;
        @Expose
        @SerializedName("start")
        private String start;
        @Expose
        @SerializedName("approved")
        private String approved;
        @Expose
        @SerializedName("driving_license")
        private String driving_license;
        @Expose
        @SerializedName("national_identity")
        private String national_identity;
        @Expose
        @SerializedName("photo")
        private String photo;
        @Expose
        @SerializedName("type")
        private String type;
        @Expose
        @SerializedName("lang")
        private double lang;
        @Expose
        @SerializedName("lat")
        private double lat;
        @Expose
        @SerializedName("address")
        private String address;
        @Expose
        @SerializedName("phone")
        private String phone;
        @Expose
        @SerializedName("email")
        private String email;
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

        public int getCountry_id() {
            return country_id;
        }

        public void setCountry_id(int country_id) {
            this.country_id = country_id;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getApproved() {
            return approved;
        }

        public void setApproved(String approved) {
            this.approved = approved;
        }

        public String getDriving_license() {
            return driving_license;
        }

        public void setDriving_license(String driving_license) {
            this.driving_license = driving_license;
        }

        public String getNational_identity() {
            return national_identity;
        }

        public void setNational_identity(String national_identity) {
            this.national_identity = national_identity;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public double getLang() {
            return lang;
        }

        public void setLang(double lang) {
            this.lang = lang;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
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
        @SerializedName("user")
        private UserEntity user;
        @Expose
        @SerializedName("model_name")
        private String model_name;
        @Expose
        @SerializedName("brand_name")
        private String brand_name;
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
        @Expose
        @SerializedName("rate")
        private int rate;

        public int getRate() {
            return rate;
        }

        public void setRate(int rate) {
            this.rate = rate;
        }


        public UserEntity getUser() {
            return user;
        }

        public void setUser(UserEntity user) {
            this.user = user;
        }

        public String getModel_name() {
            return model_name;
        }

        public void setModel_name(String model_name) {
            this.model_name = model_name;
        }

        public String getBrand_name() {
            return brand_name;
        }

        public void setBrand_name(String brand_name) {
            this.brand_name = brand_name;
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
}
