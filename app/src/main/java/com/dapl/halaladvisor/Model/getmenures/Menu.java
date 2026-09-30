
package com.dapl.halaladvisor.Model.getmenures;

 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

 public class Menu {

    @SerializedName("menu_id")
    @Expose
    private String menuId;
    @SerializedName("r_id")
    @Expose
    private String rId;
    @SerializedName("menu_link")
    @Expose
    private String menuLink;
    @SerializedName("menu_name")
    @Expose
    private String menuName;

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getrId() {
        return rId;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }

    public String getMenuLink() {
        return menuLink;
    }

    public void setMenuLink(String menuLink) {
        this.menuLink = menuLink;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

}
