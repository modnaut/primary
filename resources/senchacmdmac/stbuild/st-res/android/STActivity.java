package %s;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.content.Intent;
import com.sencha.nimblekit.*;

public class STActivity extends Activity
{
    private static NimbleKit nimblekit = null;
	private static String SERVER_URL = "%s";
	public static String getServerUrl() {
		return SERVER_URL;
	}
	private static String SENDER_ID = "%s";
	public static String getSenderId() {
		return SENDER_ID;
	}
	private static String base64EncodedPublicKey = "%s";
	public static String getBase64EncodedPublicKey() {
		return base64EncodedPublicKey;
	}
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		nimblekit = new NimbleKit(this);
	}

	@Override
	public void onBackPressed() 
	{
    	if (!nimblekit.onBackPressed()) 
    		{
    			super.onBackPressed();
    		}
	}
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent){
        nimblekit.onActivityResult(requestCode, resultCode, imageReturnedIntent);
    }
}
