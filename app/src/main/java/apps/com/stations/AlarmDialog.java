
package apps.com.stations;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class AlarmDialog extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON); // 화면이 꺼졌을때 화면 깨우기

        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_alarm);

        Intent intent = getIntent();


        String memo = intent.getStringExtra("memo");

        CustomTextView memos = (CustomTextView)findViewById(R.id.memo);
        memos.setText(memo);

        Button back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        final Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);


        vibrator.vibrate(1000);

    }






}
