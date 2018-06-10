package extra4it.fahmy.com.rentei.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CityModel {

    @Expose
    @SerializedName("cities")
    private List<CitiesEntity> cities;

    public List<CitiesEntity> getCities() {
        return cities;
    }

    public void setCities(List<CitiesEntity> cities) {
        this.cities = cities;
    }

    public static class CitiesEntity {
        @Expose
        @SerializedName("name")
        private String name;
        @Expose
        @SerializedName("id")
        private int id;

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
