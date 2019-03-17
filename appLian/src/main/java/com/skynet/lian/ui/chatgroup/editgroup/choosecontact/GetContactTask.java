package com.skynet.lian.ui.chatgroup.editgroup.choosecontact;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.gson.Gson;
import com.skynet.lian.application.AppController;
import com.skynet.lian.models.Profile;

import java.util.ArrayList;
import java.util.List;

public class GetContactTask extends AsyncTask<ContentResolver,Void,List<Profile>> {
    CallBack callBack;
    public GetContactTask(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected List<Profile> doInBackground(ContentResolver... contentResolvers) {
        ContentResolver cr = contentResolvers[0];

        List<Profile> listContact = new ArrayList<>();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.i("", "Name: " + name);
                        Log.i("", "Phone Number: " + phoneNo);
                        Profile profile =new Profile();
                        profile.setName(name);
                        profile.setPhone(phoneNo);
                        listContact.add(profile);
                        AppController.getInstance().getmSetting().put("contact",new Gson().toJson(listContact));
                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }

        return listContact;
    }

    @Override
    protected void onPostExecute(List<Profile> profiles) {
        super.onPostExecute(profiles);
       callBack.onCallBack(profiles);
    }

    public interface CallBack{
        void onCallBack(List<Profile> list);
    }
}
