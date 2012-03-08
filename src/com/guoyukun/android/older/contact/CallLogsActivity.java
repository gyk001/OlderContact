package com.guoyukun.android.older.contact;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CallLogsActivity extends ListActivity {
	private static final String TAG = "CallLogsActivity";

	//private static final String[] PROJECTION = new String[] {CallLog.Calls.NUMBER,CallLog.Calls.CACHED_NAME,CallLog.Calls.TYPE, CallLog.Calls.DATE,}; 
	
	private Cursor cursor = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI,
				null, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
		startManagingCursor(cursor);

		CallLogCursorAdapter adapter = new CallLogCursorAdapter(this,cursor);
		setListAdapter(adapter);
	}
	
	
    public class CallLogCursorAdapter extends CursorAdapter{
		private LayoutInflater mInflater;
    	private final SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    	
		
        public CallLogCursorAdapter(Context context, Cursor c) {
			super(context, c);
			mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		}
        

 
		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			setChildView(view, cursor);  
			
		}
		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			View view = mInflater.inflate(R.layout.calllog_item, null);  
	        setChildView(view, cursor);  
	        return view; 
		}
		
		
		public void setChildView(View view, Cursor cursor) { 
			
			 ImageView callTypeImg = (ImageView)view.findViewById(R.id.calltype);
             TextView nameTxt = (TextView)view.findViewById(R.id.name);
             TextView numberTxt = (TextView)view.findViewById(R.id.number);
             TextView epochTxt = (TextView)view.findViewById(R.id.epoch);
             TextView durationTxt = (TextView)view.findViewById(R.id.duration);
             
             String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
             String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
             Integer epoch = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DATE));
             Integer duration = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION));
             Integer callType = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
             
             
             Date date = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE))));  
             String time = sfd.format(date);
             
             
             nameTxt.setText( name );
             numberTxt.setText(number+":"+time);
             durationTxt.setText(""+duration);
             epochTxt.setText(""+epoch);
             int color = 0;
             
             switch( callType ){
	             case CallLog.Calls.INCOMING_TYPE:{
	            	 color = Color.BLUE ;
	            	 break;
	             	}
	             case CallLog.Calls.OUTGOING_TYPE:{
	            	 color = Color.GREEN;
	            	 break;
	             	}
	             case CallLog.Calls.MISSED_TYPE:{
	            	 color = Color.RED;
	            	 break;
	             }
             }
             
             callTypeImg.setBackgroundColor(color);
	    }         
         
    }
	
}