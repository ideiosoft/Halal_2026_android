package com.dapl.halaladvisor.Model;

public class RestaurentMoreInfoObject {
    private String more_info;

    public String getMore_info ()
    {
        return more_info;
    }

    public void setMore_info (String more_info)
    {
        this.more_info = more_info;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [more_info = "+more_info+"]";
    }
}
