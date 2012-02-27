package com.guoyukun.android.older.contact;

import java.net.URI;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ViewContactActivity extends Activity {
	private static final String TAG = "ViewContactActivity";
	URI mLookupUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.skeleton_activity);
		LinearLayout ll = (LinearLayout)findViewById(R.id.contactInfoLinearLayout);
		ll.setOrientation(LinearLayout.VERTICAL);
		//ListView lv = (ListView) findViewById(R.id.infoList);
		
		final Intent intent = getIntent();
		Uri data = intent.getData();

		final long rawContactId = ContentUris.parseId(data);

		StringBuffer sb = new StringBuffer();
		// 如果有电话，根据联系人的ID查找到联系人的电话，电话可以是多个

		Cursor phones = getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				null,
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
						+ rawContactId, null, null);
		while (phones.moveToNext()) {
			String phoneNumber = phones
					.getString(phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			
			sb.append(phoneNumber + " ");
			
		     int type = phones.getInt( phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
	           
	            String label = null;
	            //Custom type? Then get the custom label
	            if (type == ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM) {
	                label = phones.getString( phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL));
	            }
	            //Get the readable string
	            String numberType = (String) ContactsContract.CommonDataKinds.Phone.getTypeLabel(getResources(), type, label);

	            
	            LayoutInflater flater = LayoutInflater.from(this); 
	            View view = flater.inflate(R.layout.phone_item, null);
	            TextView textView = (TextView)view.findViewById(R.id.phoneNum);
	            textView.setText(phoneNumber);

	            
	            ll.addView(view);
			sb.append( numberType);
		}
		sb.append("\r\n");
		phones.close();
       // this.addContentView(ll,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));

		//-------------------------------------------------
		// 查找email地址，这里email也可以有多个 
		 
        Cursor emails = getContentResolver().query( 
                ContactsContract.CommonDataKinds.Email.CONTENT_URI, 
                null, 
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " 
                        + rawContactId, null, null); 
        sb.append("email:"); 
        while (emails.moveToNext()) { 
            String emailAddress = emails 
                    .getString(emails 
                            .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)); 
            sb.append(emailAddress+" "); 
        } 
        sb.append("\r\n"); 
        emails.close(); 

        // 获得联系人的地址 

        Cursor address = getContentResolver() 
                .query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI, 
                        null, 
                        ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID 
                                + " = " + rawContactId, null, null); 
        while (address.moveToNext()) { 
            // These are all private class variables, don’t forget to create 
            // them. 
            String poBox = address 
                    .getString(address 
                            .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX)); 
            String street = address 
                    .getString(address 
                            .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET)); 
            String city = address 
                    .getString(address 
                            .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY)); 
            String state = address 
                    .getString(address 
                            .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION)); 
            String postalCode = address 
                    .getString(address 
                            .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE)); 
            String country = address 
                    .getString(address 
                            .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY)); 
            String type = address 
                    .getString(address 
                            .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE)); 
            sb.append(type +poBox + street + city + state + postalCode+ country);
        }

    
		Log.v(TAG, sb.toString());

	}
	
	

}