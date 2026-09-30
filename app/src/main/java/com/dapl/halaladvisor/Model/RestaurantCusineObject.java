package com.dapl.halaladvisor.Model;

public class RestaurantCusineObject {
    private String cuisines_name;

    public String getCuisines_name ()
    {
        return cuisines_name;
    }

    public void setCuisines_name (String cuisines_name)
    {
        this.cuisines_name = cuisines_name;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [cuisines_name = "+cuisines_name+"]";
    }
}
