package apps.com.stations;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBHelper extends SQLiteOpenHelper {

	DBHelper(Context context) {
		super(context, "VISIT.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {


		String sql2 = "create table member("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "mem_id TEXT NOT NULL,  mem_pass TEXT NOT NULL,  mem_name TEXT NOT NULL"
				+ ",  email TEXT NOT NULL)"; //멤버

		String sql3 = "create table visit("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "lat TEXT NOT NULL, lng TEXT NOT NULL,  cate TEXT NOT NULL, name TEXT NOT NULL,  line TEXT NOT NULL , station TEXT NOT NULL, phone TEXT NOT NULL, addr TEXT,  desc1 TEXT NOT NULL" +
				",  desc2 TEXT NOT NULL,  hit TEXT NOT NULL,  favor TEXT NOT NULL,  img TEXT NOT NULL )"; //여행지



		String sql5 = "create table schedule("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "s_idx TEXT NOT NULL, title TEXT NOT NULL, img TEXT NOT NULL,  reg_date DATETIME NOT NULL,  start_time TEXT NOT NULL,  end_time TEXT NOT NULL, lat TEXT NOT NULL, lng TEXT NOT NULL, station TEXT NOT NULL,tag TEXT NOT NULL," +
				" alarm TEXT NOT NULL" +
				",mem_id TEXT NOT NULL, start_date TEXT NOT NULL,end_date TEXT NOT NULL )";//스케쥴


		db.execSQL(sql2);
		db.execSQL(sql3);

		db.execSQL(sql5);


	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXIST member");
		onCreate(db);

	}
}
