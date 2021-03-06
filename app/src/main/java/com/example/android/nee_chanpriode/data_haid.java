 package com.example.android.nee_chanpriode;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.nee_chanpriode.Model.PeriodeHaid;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class data_haid extends AppCompatActivity {

    private EditText editSiklus,editJmlHaid;
    private CalendarView datePicker;
    private Button btnInput;

    FirebaseDatabase mDatabase;
    DatabaseReference myRef;
    FirebaseAuth mFirebaseAuth;

    GoogleSignInClient mGoogleSignInClient;

    String mTanggal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_haid);


        mFirebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mFirebaseAuth.getCurrentUser();


        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference("Periode");

        // inisialisasi komponen
        editSiklus = findViewById(R.id.ev_siklus);
        editJmlHaid = findViewById(R.id.ev_lamahaid);
        datePicker = findViewById(R.id.dp_tanggalhaid);
        btnInput = findViewById(R.id.btn_InputData);



        datePicker.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                String tanggal = String.valueOf(year)+"/"
                        + String.valueOf(month)+"/"
                        + String.valueOf(day);

                mTanggal = tanggal;
            }
        });
//        Date dt = getDateFromDatePicker(datePicker);



        // format tanggal
//
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(data_haid.this, mTanggal ,Toast.LENGTH_SHORT).show();
                newPeriod(user.getUid()
                        , Integer.parseInt(editSiklus.getText().toString())
                        , Integer.parseInt(editJmlHaid.getText().toString())
                        , mTanggal);
//
            }
        });


    }

    private void newPeriod(String uid, int siklus, int jumlah, String tanggal){
        PeriodeHaid haid = new PeriodeHaid(siklus, jumlah, tanggal);

        myRef.child(uid).setValue(haid);

        myRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(data_haid.this, "Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(data_haid.this, "Gagal Ditambahkan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static java.util.Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }
}
