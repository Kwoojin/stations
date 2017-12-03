package apps.com.stations;

import java.io.Serializable;

public class ScheduleData implements Serializable {

	private static final long serialVersionUID = 1L;

	String idx = "";
	String s_idx = "";
	String title = "";

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	String img = "";
	String reg_date="";


	String start_time = "";
	String end_time = "";
	String lat = "";
	String lng ="";
	String station ="";
	String tag ="";

	String alarm = "";

	public ScheduleData(String idx,String s_idx, String title, String img, String reg_date, String start_time, String end_time, String lat, String lng, String station, String tag, String alarm) {
		this.idx = idx;
		this.s_idx = s_idx;
		this.title = title;
		this.img = img;
		this.reg_date = reg_date;
		this.start_time = start_time;
		this.end_time = end_time;
		this.lat = lat;
		this.lng = lng;
		this.station = station;
		this.tag = tag;
		this.alarm = alarm;
	}

	public String getS_idx() {
		return s_idx;
	}

	public void setS_idx(String s_idx) {
		this.s_idx = s_idx;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getReg_date() {
		return reg_date;
	}

	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
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

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getAlarm() {
		return alarm;
	}

	public void setAlarm(String alarm) {
		this.alarm = alarm;
	}














}
