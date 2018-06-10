package extra4it.fahmy.com.rentei.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateModel {

    @Expose
    @SerializedName("user")
    private UserEntity user;
    @Expose
    @SerializedName("success")
    private int success;

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
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
}
