package apps.com.stations;



import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


public class CustomTextView extends TextView {
 
 public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
 }
 
 public CustomTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
 }
 
 public CustomTextView(Context context) {
    super(context);
 }
 
 @Override
 public void setTypeface(Typeface tf) {
 
	super.setTypeface(CommonUtils.getFont(getContext() , CommonUtils.ASSET_PATH));
 }
 
}