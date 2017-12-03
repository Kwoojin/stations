
package apps.com.stations;

import java.io.Serializable;

public class  VisitData implements Serializable {
	/**
	 *
	 */




	private static final long serialVersionUID = 1L;
	String idx = "";
	String lat = "";
	String lng = "";
	String cate="";


	String name = "";
	String line = "";
	String station = "";
	String phone ="";
	String addr ="";
	String desc1 ="";
	String desc2 = "";
	String hit ="";
	String favor ="";
	String img ="";

	public VisitData(String idx, String lat, String lng, String cate, String name, String line, String station, String phone, String addr, String desc1, String desc2, String hit, String favor, String img) {
		this.idx = idx;
		this.lat = lat;
		this.lng = lng;
		this.cate = cate;
		this.name = name;
		this.line = line;
		this.station = station;
		this.phone = phone;
		this.addr = addr;
		this.desc1 = desc1;
		this.desc2 = desc2;
		this.hit = hit;
		this.favor = favor;
		this.img = img;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getCate() {
		return cate;
	}

	public void setCate(String cate) {
		this.cate = cate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getDesc1() {
		return desc1;
	}

	public void setDesc1(String desc1) {
		this.desc1 = desc1;
	}

	public String getDesc2() {
		return desc2;
	}

	public void setDesc2(String desc2) {
		this.desc2 = desc2;
	}

	public String getHit() {
		return hit;
	}

	public void setHit(String hit) {
		this.hit = hit;
	}

	public String getFavor() {
		return favor;
	}

	public void setFavor(String favor) {
		this.favor = favor;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

















}
