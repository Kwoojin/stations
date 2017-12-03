package apps.com.stations;

import java.io.Serializable;

public class BoardData implements Serializable {

	String idx = "";
	String title = "";
	String contents = "";
	String id="";

	public BoardData(String idx, String title, String contents, String id, String reg_date) {
		this.idx = idx;
		this.title = title;
		this.contents = contents;
		this.id = id;
		this.reg_date = reg_date;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getReg_date() {
		return reg_date;
	}

	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}

	String reg_date = "";


}
