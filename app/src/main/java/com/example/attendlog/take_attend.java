package com.example.attendlog;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

public class take_attend extends AppCompatActivity {

    Button butdone;

    TextView prtdate,prttime;

    ArrayList<String> stupino,stusino,stuname,attendance_list;

    ProgressDialog progressDialog;
    FirebaseFirestore firebaseFirestore;
    ListView li;
    private RadioGroup radioGroup;

    String selected_noon,currentDate,currentTime;

    int size;
    String s_classid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attend);

        butdone=findViewById(R.id.but_done);
        prtdate=findViewById(R.id.present_date);
        prttime=findViewById(R.id.present_time);
        li=findViewById(R.id.student_list);


        stusino=new ArrayList<>();
        stuname=new ArrayList<>();
        stupino=new ArrayList<>();
        attendance_list=new ArrayList<>();

        currentDate = getCurrentDate();
        currentTime = getCurrentTime();

        // Set the text to TextViews
        prtdate.setText(currentDate);
        prttime.setText(currentTime);

        progressDialog=new ProgressDialog(this);


        radioGroup = (RadioGroup)findViewById(R.id.Radio_noon);

        // Uncheck or reset the radio buttons initially
        radioGroup.clearCheck();

        // Add the Listener to the RadioGroup
        radioGroup.setOnCheckedChangeListener(
                new RadioGroup
                        .OnCheckedChangeListener() {
                    @Override

                    // The flow will come here when
                    // any of the radio buttons in the radioGroup
                    // has been clicked

                    // Check which radio button has been clicked
                    public void onCheckedChanged(RadioGroup group,
                                                 int checkedId)
                    {

                        // Get the selected Radio Button
                        RadioButton
                                radioButton
                                = (RadioButton)group
                                .findViewById(checkedId);

                        Toast.makeText(take_attend.this,
                                        radioButton.getText(),
                                        Toast.LENGTH_SHORT)
                                .show();
                    }
                });


        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference coll=firestore.collection("class");

        SharedPreferences sharedPreferences=getSharedPreferences(MainActivity.U_LOGIN,0);
        boolean haslogedin=sharedPreferences.getBoolean("hasClassLogIn",false);
        s_classid=sharedPreferences.getString("classid",null);

        Query query = firestore.collection("class").document(s_classid).collection("students").orderBy("stusino_numeric", Query.Direction.ASCENDING);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    progressDialog.show();
                    QuerySnapshot document = task.getResult();
                    if (!document.isEmpty()) {
                        for(DocumentSnapshot doc:document){
                            stupino.add(doc.getString("stupino"));
                            stuname.add(doc.getString("stuname"));
                            stusino.add(doc.getString("stusino"));
                        }

                        progressDialog.cancel();

                        take_attend.CustomBaseAdapter cust=new take_attend.CustomBaseAdapter(getApplicationContext(),stusino,stupino,stuname,attendance_list);
                        li.setAdapter(cust);
                        progressDialog.cancel();






                    }
                    progressDialog.cancel();

                }
            }
        });

        progressDialog.cancel();












        butdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





                Log.d(TAG, "onComplete:                          "+attendance_list);
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    Toast.makeText(take_attend.this,
                                    "SELECT THE NOON",
                                    Toast.LENGTH_SHORT)
                            .show();
                }
                else {
                    progressDialog.show();

                    RadioButton radioButton
                            = (RadioButton) radioGroup
                            .findViewById(selectedId);
                    selected_noon = radioButton.getText().toString();

//                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//                    CollectionReference coll=firestore.collection("class");
//
//                    CollectionReference coll1 = coll.document(s_classid).collection("students");
//
//                    coll1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful()) {
//                                QuerySnapshot querySnapshot = task.getResult();
//                                if (querySnapshot != null) {
//                                    size = querySnapshot.size();
//                                    // Now 'size' contains the number of documents in the collection
//                                    Log.d("Firestore", "Number of documents: " + size);
//                                }
//                            } else {
//                                Log.e("Firestore", "Error getting documents: ", task.getException());
//                            }
//                        }
//                    });
//
//                    coll.document(s_classid).collection("attendance").document(currentDate+currentTime).set(new attendmodel(attendance_list,currentDate,currentTime,selected_noon,attendance_list.size(),size-attendance_list.size()));
//

                    // Now display the value of selected item
                    // by the Toast message




                    progressDialog.cancel();


                    Intent intent = new Intent(take_attend.this, take_attend_post.class);

                    intent.putStringArrayListExtra("attendance_list", attendance_list);
                    intent.putExtra("current_date", currentDate);
                    intent.putExtra("current_time", currentTime);
                    intent.putExtra("selected_noon", selected_noon);
                    startActivity(intent);
                }
            }
        });


    }
    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getCurrentTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Date date = new Date();
        return timeFormat.format(date);
    }


    public class CustomBaseAdapter extends BaseAdapter {

        Context context;
        ArrayList<String> l_stupino, l_stusino, l_stuname;
        ArrayList<String> attendance_present; // ArrayList to store checked items
        LayoutInflater inflater;

        // Create a SparseBooleanArray to store the checked state of each item
        private SparseBooleanArray checkedStateArray;

        public CustomBaseAdapter(Context applicationContext, ArrayList<String> stusino, ArrayList<String> stupino, ArrayList<String> stuname, ArrayList<String> attendance_absent) {
            this.context = applicationContext;
            this.l_stusino = stusino;
            this.l_stupino = stupino;
            this.l_stuname = stuname;
            this.attendance_present = attendance_absent;
            inflater = LayoutInflater.from(applicationContext);
            checkedStateArray = new SparseBooleanArray();
        }

        @Override
        public int getCount() {
            return l_stuname.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.attend_pin_list, null);

            TextView stusino = view.findViewById(R.id.stu_sino);
            TextView stupino = view.findViewById(R.id.stu_pino);
            TextView stuname = view.findViewById(R.id.stu_name);
            CheckBox checkBox = view.findViewById(R.id.checkBox);

            stusino.setText(l_stusino.get(i));
            stupino.setText(l_stupino.get(i));
            stuname.setText(l_stuname.get(i));

            // Set a tag to identify the position of the item
            checkBox.setTag(i);

            // Set the checkbox state based on the checkedStateArray
            checkBox.setChecked(checkedStateArray.get(i, false));

            // Set an OnCheckedChangeListener to handle checkbox state changes
            checkBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                // Update the checked state in the array
                checkedStateArray.put(i, isChecked);

                int position = (int) compoundButton.getTag();
                if (isChecked) {
                    // Checkbox is checked, add to attendance_present list
                    attendance_present.add(l_stupino.get(position));
                } else {
                    // Checkbox is unchecked, remove from attendance_present list
                    attendance_present.remove(l_stupino.get(position));
                }

                // Log the attendance_present list and its size
                for (String name : attendance_present) {
                    // Log the names in the attendance_present list
                    System.out.println("Attendance Present: " + name);
                }
                System.out.println("Total Checked: " + attendance_present.size());

                // Notify the adapter that the data set has changed
                notifyDataSetChanged();
            });

            return view;
        }

        // Method to get the list of students present
        public ArrayList<String> getAttendancePresentList() {
            return attendance_present;
        }
    }

}