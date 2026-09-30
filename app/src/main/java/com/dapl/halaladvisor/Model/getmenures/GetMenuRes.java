
package com.dapl.halaladvisor.Model.getmenures;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

 public class GetMenuRes {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("menus")
    @Expose
    private ArrayList<Menu> menus = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Menu> getMenus() {
        return menus;
    }

    public void setMenus(ArrayList<Menu> menus) {
        this.menus = menus;
    }

}
