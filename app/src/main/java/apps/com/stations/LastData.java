package apps.com.stations;

import java.io.Serializable;

public class LastData implements Serializable {


	private static final long serialVersionUID = 1L;
	String station = "";
	String start_date = "";

	public LastData(String station, String start_date, String end_date) {
		this.station = station;
		this.start_date = start_date;
		this.end_date = end_date;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	String end_date = "";














}
