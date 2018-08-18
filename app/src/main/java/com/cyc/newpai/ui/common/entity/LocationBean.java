package com.cyc.newpai.ui.common.entity;

// 位置信息bean

import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import java.io.Serializable;

public class LocationBean implements Serializable{
	// {
	// "ExMsgType" : "Location",
	// "kAddress" = "详细地址";
	// "longitude" : "115.8606824751916",
	// "latitude" : "28.69923557623561",
	// "site" : "江西省南昌市青山湖区金融大街"
	// }

	String ExMsgType = "Location";		// 消息类型，值为Location
	String site = null;			    // 地址
	String kAddress = null;				// 详细地址
	double longitude = 0;			    // 经度
	double latitude = 0;			    // 纬度
	String name;
	ReverseGeoCodeResult.AddressComponent addressComponent;

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getkAddress() {
		return kAddress;
	}

	public void setkAddress(String kAddress) {
		this.kAddress = kAddress;
	}

	public String getExMsgType() {
		return ExMsgType;
	}

	public void setExMsgType(String exMsgType) {
		this.ExMsgType = exMsgType;
	}

    public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ReverseGeoCodeResult.AddressComponent getAddressComponent() {
		return addressComponent;
	}

	public void setAddressComponent(ReverseGeoCodeResult.AddressComponent addressComponent) {
		this.addressComponent = addressComponent;
	}

	@Override
	public String toString() {
		return "LocationBean{" +
				"ExMsgType='" + ExMsgType + '\'' +
				", site='" + site + '\'' +
				", kAddress='" + kAddress + '\'' +
				", longitude=" + longitude +
				", latitude=" + latitude +
				'}';
	}
}
