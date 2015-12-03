package com.xbb.la.modellibrary.net.data;

import java.io.Serializable;

public class UserLocation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName;
	private double lat;
	private double lng;
	private int level;
	private int usertype;
	private boolean isUnion = false;
	private String orderId;
	private String uid;

	public UserLocation(String userName, double lat, double lng) {
		this.userName = userName;
		this.lat = lat;
		this.lng = lng;
	}

	public UserLocation(String userName, double lat, double lng, int level,
			int usertype) {
		super();
		this.userName = userName;
		this.lat = lat;
		this.lng = lng;
		this.level = level;
		this.usertype = usertype;
	}

	public UserLocation(double lat, double lng, String orderId, String uid) {
		this.lat = lat;
		this.lng = lng;
		this.orderId = orderId;
		this.uid = uid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getUsertype() {
		return usertype;
	}

	public void setUsertype(int usertype) {
		this.usertype = usertype;
	}

	public boolean isUnion() {
		return isUnion;
	}

	public void setUnion(boolean isUnion) {
		this.isUnion = isUnion;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
}
