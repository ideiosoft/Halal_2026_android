
package com.dapl.halaladvisor.Model.resdetailsres;

 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

 public class CuisinesName {

    @SerializedName("cuisines_name")
    @Expose
    private String cuisinesName;

    public String getCuisinesName() {
        return cuisinesName;
    }

    public void setCuisinesName(String cuisinesName) {
        this.cuisinesName = cuisinesName;
    }

}
