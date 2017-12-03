package apps.com.stations;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmRecever extends BroadcastReceiver {
    String aIdx = "";
    Context con;

    @Override
    public void onReceive(Context context, Intent intent) {  //알람이 울렸을때 이벤트를 받는 BroadcastReceiver
        con =context;

        int idx = intent.getIntExtra("idx", -1);
        aIdx = Integer.toString(idx);
    //    getAlarmData(context);

        Intent gt = new Intent(con, AlarmDialog.class);//AlarmDialog 를 뛰운다.
        gt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        gt.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);


        gt.putExtra("memo", "한시간 뒤에 일정이 있습니다. 일정을 확인하세요.");


        con.startActivity(gt);

    }






}

