package com.example.attendlog;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class take_attend_post extends AppCompatActivity {

    Button post,postonwhat;

    EditText txt;

    String Final_msg;

    ProgressDialog progressDialog;
    FirebaseFirestore firebaseFirestore;
    String s_classid,m_time,currentDate ,currentTime
                    ,selected_noon;

    static String resultFormatOne;
    static String resultFormatTwo;
    ArrayList<String> attendance_list;
    int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attend_post);

        post=findViewById(R.id.but_post);
        postonwhat=findViewById(R.id.but_postonwhat);


        txt=this.findViewById(R.id.attendance_text);




        Intent intent = getIntent();
       attendance_list = intent.getStringArrayListExtra("attendance_list");
         currentDate = intent.getStringExtra("current_date");
         currentTime = intent.getStringExtra("current_time");
         selected_noon = intent.getStringExtra("selected_noon");


//        Date: 01/01/2024
//        Time: 9.30-12.30
//        Department: CSE -B
//        Total strength :71
//        No of students present: 21
//        No of students absent: 50
//        Pinlist of Absentees
//        68,69,71,72,74,77,78,79,81,82,83,84,85,86,87,88,90,91,93,94,96,97,98,99,A0,A1,A2,A3,A4,A7,A8,B3,B4,B5,B8,B9,C0,C1,C4,C5,C7,C8,C9
//
//        Le : 7,8,10,11,14,16,17


        //22555A0527

        SharedPreferences sharedPreferences=getSharedPreferences(MainActivity.U_LOGIN,0);
        boolean haslogedin=sharedPreferences.getBoolean("hasClassLogIn",false);
        s_classid=sharedPreferences.getString("classid",null);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        progressDialog=new ProgressDialog(this);

        CollectionReference coll=firestore.collection("class");
        CollectionReference coll1 = coll.document(s_classid).collection("students");
        progressDialog.show();
        coll1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null) {
                        size = querySnapshot.size();
                        // Now 'size' contains the number of documents in the collection
                        Log.d("Firestore", "Number of documents: " + size);
                        progressDialog.cancel();
                        continueProcessing();
                    }
                } else {
                    Log.e("Firestore", "Error getting documents: ", task.getException());
                    progressDialog.cancel();
                }
            }
        });



        if (selected_noon.equals("MORNING")){
            m_time="9.30 - 12.30";
        }else{
            m_time="1.15 - 4.15";
        }


        Log.d(TAG, "arraylist before                     : "+attendance_list);

        sortAttendanceList(attendance_list);

        Log.d(TAG, "arraylist after                  : "+attendance_list);


        movePINOsToEnd(attendance_list);

        Log.d(TAG, "arraylist areplace               : "+attendance_list);



        processPINOs(attendance_list);

        SimpleDateFormat sdfInput = new SimpleDateFormat("d-M-yyyy");
        SimpleDateFormat sdfOutput = new SimpleDateFormat("d/M/yyyy");

        try {
            // Parse the current date using the input format
            String formattedDate = sdfOutput.format(sdfInput.parse(sdfInput.format(currentDate)));

            // Print the formatted date
            System.out.println("Formatted Date: " + formattedDate);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Final_msg="Date : "+currentDate+"\n"
                 +"Time : "+m_time+"\n"
                 +"Departement : "+s_classid+"\n"
                 +"Total Strength : "+size+"\n"
                 +"No of Students Present : "+(size-attendance_list.size())+"\n"
                 +"No of Students Absent : "+(attendance_list.size())+"\n"
                 +"Pin List Of Absenties : "+"\n"
                 +resultFormatOne+"\n"+"\n"
                 +"Le : "+resultFormatTwo;

        Log.d(TAG, "The Final Message is     "+Final_msg);

        txt.setText(Final_msg);


















        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                coll.document(s_classid).collection("attendance").document(currentDate+currentTime).set(new attendmodel(attendance_list,currentDate,currentTime,selected_noon,attendance_list.size(),size-attendance_list.size()));



                Intent in = new Intent(take_attend_post.this, home.class);
                startActivity(in);

                finish();
            }
        });

        postonwhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coll.document(s_classid).collection("attendance").document(currentDate + currentTime)
                        .set(new attendmodel(attendance_list, currentDate, currentTime, selected_noon, attendance_list.size(), size - attendance_list.size()));

                Intent intentShare = new Intent(Intent.ACTION_SEND);
                intentShare.setType("text/plain");
//                intentShare.putExtra(Intent.EXTRA_SUBJECT,"My Subject Here ... ");
//                intentShare.putExtra(Intent.EXTRA_TEXT,"My Text of the message goes here ... write anything what you want");


                intentShare.putExtra(Intent.EXTRA_TEXT, Final_msg);

// Explicitly set the package to WhatsApp
                intentShare.setPackage("com.whatsapp");

                startActivity(Intent.createChooser(intentShare, "Share using"));

            }
        });




    }




    private static void processPINOs(ArrayList<String> pinos) {
        StringBuilder formatOneResult = new StringBuilder();
        StringBuilder formatTwoResult = new StringBuilder();

        for (String pino : pinos) {
            // Extract last two digits
            String lastTwoDigits = pino.substring(pino.length() - 2);

            if (pino.startsWith("21551A05")) {
                // Format One
                formatOneResult.append(lastTwoDigits).append(",");
            } else if (pino.startsWith("22555A05")) {
                // Format Two
                formatTwoResult.append(lastTwoDigits).append(",");
            }
        }

        // Remove trailing commas
        resultFormatOne = formatOneResult.toString().replaceAll(",$", "");
        resultFormatTwo = formatTwoResult.toString().replaceAll(",$", "");

        // Print the results
        Log.d(TAG, "Format One: " + resultFormatOne);
        Log.d(TAG, "Format Two: Le : " + resultFormatTwo);
    }


//    private static void sortAttendanceList(ArrayList<String> attendanceList) {
//        Collections.sort(attendanceList, new Comparator<String>() {
//            @Override
//            public int compare(String pino1, String pino2) {
//                // Extract numeric and alphanumeric parts
//                String numericPart1 = pino1.replaceAll("[^0-9]", "");
//                String numericPart2 = pino2.replaceAll("[^0-9]", "");
//                String alphaNumericPart1 = pino1.replaceAll("[^a-zA-Z]", "");
//                String alphaNumericPart2 = pino2.replaceAll("[^a-zA-Z]", "");
//
//                // Compare numeric parts first
//                int numericComparison = Integer.compare(Integer.parseInt(numericPart1), Integer.parseInt(numericPart2));
//
//                if (numericComparison != 0) {
//                    return numericComparison;
//                } else {
//                    // If numeric parts are equal, compare alphanumeric parts
//                    if (isNumeric(alphaNumericPart1) && isNumeric(alphaNumericPart2)) {
//                        return alphaNumericPart1.compareTo(alphaNumericPart2);
//                    } else {
//                        // Handle cases where one of the alphanumeric parts is not numeric
//                        return alphaNumericPart1.compareToIgnoreCase(alphaNumericPart2);
//                    }
//                }
//            }
//
//            // Check if a string is numeric
//            private boolean isNumeric(String str) {
//                return str.matches("\\d+");
//            }
//        });
//    }

    private static void sortAttendanceList(ArrayList<String> attendanceList) {
        Collections.sort(attendanceList, new Comparator<String>() {
            @Override
            public int compare(String pino1, String pino2) {
                // Extract alphanumeric parts
                String alphaNumericPart1 = pino1.replaceAll("[0-9]", "");
                String alphaNumericPart2 = pino2.replaceAll("[0-9]", "");

                // Compare alphanumeric parts first
                int alphaNumericComparison = alphaNumericPart1.compareTo(alphaNumericPart2);

                if (alphaNumericComparison != 0) {
                    return alphaNumericComparison;
                } else {
                    // Extract numeric parts
                    String numericPart1 = pino1.replaceAll("[^0-9]", "");
                    String numericPart2 = pino2.replaceAll("[^0-9]", "");

                    // Compare numeric parts
                    return Integer.compare(Integer.parseInt(numericPart1), Integer.parseInt(numericPart2));
                }
            }
        });
    }



    private static void movePINOsToEnd(ArrayList<String> attendanceList) {
        Iterator<String> iterator = attendanceList.iterator();
        ArrayList<String> toMove = new ArrayList<>();

        while (iterator.hasNext()) {
            String pino = iterator.next();

            // Check if last two digits contain an alphabet
            if (containsAlphabet(pino.substring(pino.length() - 2))) {
                toMove.add(pino);
                iterator.remove();
            }
        }

        // Add the items back to the end of the list
        attendanceList.addAll(toMove);
    }

    // Check if a string contains an alphabet
    private static boolean containsAlphabet(String str) {
        return str.matches(".*[a-zA-Z]+.*");
    }

    private void continueProcessing() {
        if (selected_noon.equals("MORNING")) {
            m_time = "9.30 - 12.30";
        } else {
            m_time = "1.15 - 4.15";
        }

        Log.d(TAG, "arraylist before: " + attendance_list);

        sortAttendanceList(attendance_list);

        Log.d(TAG, "arraylist after: " + attendance_list);

        movePINOsToEnd(attendance_list);

        Log.d(TAG, "arraylist replace: " + attendance_list);

        processPINOs(attendance_list);

        Final_msg = "Date : " + currentDate + "\n"
                + "Time : " + m_time + "\n"
                + "Departement : " + s_classid + "\n"
                + "Total Strength : " + size + "\n"
                + "No of Students Present : " + (size - attendance_list.size()) + "\n"
                + "No of Students Absent : " + attendance_list.size() + "\n"
                + "Pin List Of Absentees : " + "\n"
                + resultFormatOne + "\n" + "\n"
                + "Le : " + resultFormatTwo;

        Log.d(TAG, "The Final Message is " + Final_msg);

        txt.setText(Final_msg);
    }
    @Override

    protected void onActivityResult(int requestCode,int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123) {
            // The request code 123 is the requestCode you specified when starting the activity for result
            // Handle any additional logic here
            // For example, navigate to the home activity
            Intent in = new Intent(take_attend_post.this, home.class);
            startActivity(in);

            // Finish the current activity
            finish();
        }
    }
}