package com.example.friendfinder;

import java.util.Timer;
import java.util.TimerTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class OnBootReceiver extends BroadcastReceiver {

	public OnBootReceiverClass obrc = null;
	
	public class OnBootReceiverClass extends Activity {
		
		//public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
//			Log.d("positionService", "here1");
//			Intent intentLaunchService = new Intent(context,
//					PositionService.class);
//			Log.d("positionService", "here2");
//			
//			context.startService(intentLaunchService);

		//}
	}

	@Override
	public void onReceive(final Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask(){
		

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.d("positionService", "here1");
				Intent intentLaunchService = new Intent(context,
						PositionService.class);
				Log.d("positionService", "here2");
				context.startService(intentLaunchService);
				Log.d("positionService", "here3");
			} 
			
		}, 60000);
		
//		
//		obrc = new OnBootReceiverClass();
//		
//		if (context.getApplicationContext() == null)
//		{
//			Log.d("positionService", "context.getApplicationContext");
//		}
//		
//		obrc.onReceive(context.getApplicationContext(), intent);
	}

}
