package apps.com.stations;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;


public class CommonUtils {
	private static CommonUtils current = null;
	AlertDialog.Builder alert = null;

	public static CommonUtils getInstance() {
		if (current == null) {
			current = new CommonUtils();
		}
		return current;
	}



	private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();
	  
	public static final String ASSET_PATH="assetPath";
	 
	public static Typeface getFont(Context c, String assetPath) {
	    synchronized (cache) {
	 
	        if (!cache.containsKey(assetPath)) {
	 
	            try {
	                Typeface t =(Typeface.createFromAsset(c.getAssets(),
	                        "aa.ttf"));
	 
	                cache.put(assetPath, t);
	 
	            } catch (Exception e) {
	                return null;
	            }
	        }
	        return cache.get(assetPath);
	    }
	}





}
