package com.guoyukun.android.older.contact;

import java.net.URI;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
		//��ϵ�˻�����Ϣ����������ע
		contactBaseInfo(rawContactId);
		//���ɵ绰����ؼ�
		genPhoneNumberView( ll, rawContactId);
		
		StringBuffer sb = new StringBuffer();
		
       // this.addContentView(ll,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));

		//-------------------------------------------------
		// ����email��ַ������emailҲ�����ж�� 
		 
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

        // �����ϵ�˵ĵ�ַ 

        Cursor address = getContentResolver() 
                .query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI, 
                        null, 
                        ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID 
                                + " = " + rawContactId, null, null); 
        while (address.moveToNext()) { 
            // These are all private class variables, don��t forget to create 
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
	
	private void contactBaseInfo( long contactId){
		 Cursor contactCur = getContentResolver() 
	                .query(ContactsContract.Contacts.CONTENT_URI, 
	                        new String[]{ContactsContract.Contacts.DISPLAY_NAME}, 
	                        ContactsContract.Contacts._ID + " = " + contactId, null, null); 
		 if(contactCur.moveToNext()){
			 String displayName = contactCur.getString(contactCur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			 TextView view = (TextView)findViewById(R.id.contactDisplayName);
			 view.setText(displayName);
		 }
		
		
        final String noteWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?"; 
        final String[] noteWhereParams = new String[]{String.valueOf(contactId), 
        			ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE}; 
        Cursor noteCur = getContentResolver().query(ContactsContract.Data.CONTENT_URI, 
        			new String[]{ContactsContract.CommonDataKinds.Note.NOTE}, noteWhere, noteWhereParams, null); 
	 	if (noteCur.moveToFirst()) { 
	 	    String note = noteCur.getString(noteCur.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
	 	    TextView view = (TextView)findViewById(R.id.contactsRemark);
	 	    view.setText(note);
	 	}
	 	noteCur.close();
	}
	
	private void genPhoneNumberView(ViewGroup parent,long contactId){
		// ����е绰��������ϵ�˵�ID���ҵ���ϵ�˵ĵ绰���绰�����Ƕ��

				Cursor phones = getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
								+ contactId, null, null);
				while (phones.moveToNext()) {
					String phoneNumber = phones
							.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					//ȥ���ָ����������ܳ��� 1-234-123-1412 ��g���ĺ����ʽ
					phoneNumber = PhoneNumberUtils.stripSeparators(phoneNumber);
					//Log.v(TAG, PhoneNumberUtils.stripSeparators(phoneNumber));
					
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
			            TextView pnView = (TextView)view.findViewById(R.id.phoneNum);
			            
			            pnView.setText(phoneNumber);
			          
			            pnView.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								Intent intent= new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+((TextView)v).getText()));
								startActivity(intent);
							}
						});
			            
			            TextView ptView = (TextView)view.findViewById(R.id.phoneType);
			            ptView.setText(numberType);
			            
			            parent.addView(view);
					
				}
				phones.close();
	}

}