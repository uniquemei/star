package com.fly.jiejing.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/10/2.
 */
@DatabaseTable(tableName = "address")
public class Address implements Serializable {
    @DatabaseField
    private String city;//城市的名字
    private String latitude;//纬度 例："30.665529"
    private String longitude;//经度 例：104.108532
    @DatabaseField
    private String street; //所属区，镇 例：成华区
    @DatabaseField
    private String detail;//"111"
    @DatabaseField
    private String address;//例：成华区111
    @DatabaseField
    private String address_id;//例：1694176
    private int selected;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Address() {
        super();
    }

    public Address(String city, int selected) {
        this.city = city;
        this.selected = selected;
    }

    public Address(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public boolean isSelected() {

        if (selected == 0) {
            return false;
        } else {
            return true;
        }

    }

    public void setSelected(int selected) {

        this.selected = selected;
    }


}
