package rcdiploma.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class ContactListActivity extends AppCompatActivity {

    int CONTACT_CODE = 1000;

    RecyclerView recyclerView;
    ArrayList<ContactList> arrayList;

    SearchView searchView;
    ContactListAdapter adapter;

    String[] appPermission = {Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        searchView = findViewById(R.id.contact_search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.trim().equalsIgnoreCase("")){
                    adapter.filter("");
                }
                else{
                    adapter.filter(newText);
                }
                return false;
            }
        });

        recyclerView = findViewById(R.id.contact_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(ContactListActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //requestStoragePermission();

        if (checkAndRequestPermission()) {
            getAllContacts();
        }

    }

    public boolean checkAndRequestPermission() {
        List<String> listPermission = new ArrayList<>();
        for (String perm : appPermission) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                listPermission.add(perm);
            }
        }
        if (!listPermission.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermission.toArray(new String[listPermission.size()]), CONTACT_CODE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CONTACT_CODE) {
            HashMap<String, Integer> permissionResult = new HashMap<>();
            int deniedCount = 0;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionResult.put(permissions[i], grantResults[i]);
                    deniedCount++;
                }
            }
            if (deniedCount == 0) {
                getAllContacts();
            } else {
                for (Map.Entry<String, Integer> entry : permissionResult.entrySet()) {
                    String permName = entry.getKey();
                    int permResult = entry.getValue();
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permName)) {
                        /*showDialogPermission("", "This App needs Read External Storage And Location permissions to work whithout and problems.",*/
                        showDialogPermission("", "This App needs Read Contact And Send SMS permissions to work whithout and problems.",
                                "Yes, Grant permissions", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        checkAndRequestPermission();
                                    }
                                },
                                "No, Exit app", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        finishAffinity();
                                    }
                                }, false);
                    } else {
                        showDialogPermission("", "You have denied some permissions. Allow all permissions at [Setting] > [Permissions]",
                                "Go to Settings", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                Uri.fromParts("package", getPackageName(), null));
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }, "No, Exit app", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        finish();
                                    }
                                }, false);
                        break;
                    }
                }
            }
        }
    }

    public AlertDialog showDialogPermission(String title, String msg, String positiveLable, DialogInterface.OnClickListener positiveOnClickListener,
                                            String negativeLable, DialogInterface.OnClickListener negativeOnClickListener, boolean isCancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setCancelable(isCancelable);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveLable, positiveOnClickListener);
        builder.setNegativeButton(negativeLable, negativeOnClickListener);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }


    /*private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            getAllContacts();
            return;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {

        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, CONTACT_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CONTACT_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new CommonMethodClass(ContactListActivity.this, "Permission Granted");
                getAllContacts();
            } else {
                new CommonMethodClass(ContactListActivity.this, "You Just Denied Permission");
            }
        }
    }*/

    private void getAllContacts() {
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");
        ArrayList<String> contactNameList = new ArrayList<>();
        ArrayList<String> contactNumberList = new ArrayList<>();
        while(cursor.moveToNext()){
            contactNumberList.add(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            contactNameList.add(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
        }

        if(cursor.getCount()>0){
            arrayList = new ArrayList<>();
            for(int i=0;i<contactNameList.size();i++){
                ContactList list = new ContactList();
                list.setName(contactNameList.get(i));
                list.setContactNo(contactNumberList.get(i));
                arrayList.add(list);
            }
            removeDuplicates(arrayList);
        }
        cursor.close();
        return;
    }

    private void removeDuplicates(ArrayList<ContactList> arrayList) {
        Set<ContactList> set = new TreeSet<>(new Comparator<ContactList>() {
            @Override
            public int compare(ContactList old, ContactList newName) {
                if(old.getName().equalsIgnoreCase(newName.getName())) {
                    return 0;
                }
                return 1;
            }
        });
        set.addAll(arrayList);
        ArrayList newList = new ArrayList(set);
        adapter = new ContactListAdapter(ContactListActivity.this,newList);
        recyclerView.setAdapter(adapter);
        Collections.sort(newList, new Comparator<ContactList>() {
            @Override
            public int compare(ContactList lhs, ContactList rhs) {
                return lhs.getName().trim().compareTo(rhs.getName().trim());
            }
        });
        adapter.notifyDataSetChanged();
    }

    private class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.MyHolder> {

        Context context;
        ArrayList<ContactList> arrayList;
        ArrayList<ContactList> searchList;

        public ContactListAdapter(ContactListActivity contactListActivity, ArrayList<ContactList> newList) {
            this.context = contactListActivity;
            this.arrayList = newList;
            searchList = new ArrayList<>();
            searchList.addAll(arrayList);
        }

        public void filter(String name){
            arrayList.clear();
            name = name.toLowerCase(Locale.getDefault());
            if(name.length()==0){
                arrayList.addAll(searchList);
            }
            else{
                for(ContactList s : searchList){
                    if(s.getName().toLowerCase(Locale.getDefault()).contains(name) || s.getContactNo().toLowerCase(Locale.getDefault()).contains(name)){
                        arrayList.add(s);
                    }
                }
            }
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_contact,parent,false);
            return new MyHolder(view);
        }

        private class MyHolder extends RecyclerView.ViewHolder {

            TextView name,contactNo;
            Button invite,call;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.custom_contact_name);
                contactNo = itemView.findViewById(R.id.custom_contact_number);
                invite = itemView.findViewById(R.id.custom_contact_invite);
                call = itemView.findViewById(R.id.custom_contact_call);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            holder.name.setText(arrayList.get(position).getName());
            holder.contactNo.setText(arrayList.get(position).getContactNo());

            holder.invite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String sendMessage = context.getResources().getString(R.string.app_name)+"\n\nWatch Entertaining, Fun, News Videos.\n\nDownload App By Tapping Link : \n\nmarket://details?id="+context.getApplicationContext().getPackageName();
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT,context.getResources().getString(R.string.app_name));
                    intent.putExtra(Intent.EXTRA_TEXT,sendMessage);
                    startActivity(Intent.createChooser(intent,"Select Application"));
                    /*SmsManager sms = SmsManager.getDefault();
                    ArrayList<String> parts = sms.divideMessage(sendMessage);
                    sms.sendMultipartTextMessage(arrayList.get(position).getContactNo(),null,parts,null,null);
                    new CommonMethodClass(context,"Invitation Sent Successfully");*/
                }
            });

            holder.call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:"+arrayList.get(position).getContactNo()));
                    context.startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }
}