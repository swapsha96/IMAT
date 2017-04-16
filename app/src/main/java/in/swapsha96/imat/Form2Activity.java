package in.swapsha96.imat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Form2Activity extends AppCompatActivity {

    String result = "";
    Button button;
    Spinner dest, purpose, spinnerIn, spinnerOut;
    DatabaseReference reference;
    ArrayList<String> list;
    Bundle bundle;
    ProgressDialog pd;
    String in, out;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form2);

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);


        list = new ArrayList<>();
        dest = (Spinner) findViewById(R.id.dest);
        List<String> list = new ArrayList<String>();
        list.add("Mandi Campus");
        list.add("Admin Block");
        list.add("Indira Market");
        list.add("Gandhi Chowk");
        list.add("Raman Bakery");
        list.add("Mandav Hospital");
        list.add("Zonal Hospital");
        list.add("Victoria Bridge");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dest.setAdapter(dataAdapter);

        purpose = (Spinner) findViewById(R.id.purpose);
        list = new ArrayList<String>();
        list.add("Shopping");
        list.add("Eating");
        list.add("Medical");
        list.add("Religious");
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        purpose.setAdapter(dataAdapter);

        spinnerIn = (Spinner) findViewById(R.id.spinner_in);
        list = new ArrayList<String>();
        list.add("12:00");
        list.add("02:00");
        list.add("04:00");
        list.add("05:30");
        list.add("06:30");
        list.add("07:30");
        list.add("08:30");
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIn.setAdapter(dataAdapter);

        spinnerOut = (Spinner) findViewById(R.id.spinner_out);
        list = new ArrayList<String>();
        list.add("12:00");
        list.add("02:00");
        list.add("04:00");
        list.add("05:30");
        list.add("06:30");
        list.add("07:30");
        list.add("08:30");
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOut.setAdapter(dataAdapter);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        bundle = getIntent().getExtras();

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(Form2Activity.this);
                pd.setMessage("loading");
                pd.show();
                final DatabaseReference ref = reference.child("Online").child("0").child(bundle.getString("uid")).getRef();
                ref.child("Purpose").setValue(purpose.getSelectedItem().toString());
                ref.child("Destination").setValue(dest.getSelectedItem().toString());
                in = spinnerIn.getSelectedItem().toString();
                out = spinnerOut.getSelectedItem().toString();
                ref.child("Time_In").setValue(in);
                ref.child("Time_Out").setValue(out);

                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c.getTime());
                ref.child("Date").setValue(formattedDate);

                reference.child("Users").child("0").child(bundle.getString("uid")).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ref.child("Branch").setValue(dataSnapshot.child("Branch").getValue(String.class));
                        ref.child("Degree").setValue(dataSnapshot.child("Degree").getValue(String.class));
                        ref.child("Gender").setValue(dataSnapshot.child("Gender").getValue(String.class));
                        ref.child("Hostel").setValue(dataSnapshot.child("Hostel").getValue(String.class));
                        ref.child("Name").setValue(dataSnapshot.child("Name").getValue(String.class));
                        ref.child("Phone Number").setValue(dataSnapshot.child("Phone Number").getValue(String.class));
                        ref.child("State").setValue(dataSnapshot.child("State").getValue(String.class));
                        ref.child("Year").setValue(Integer.parseInt(dataSnapshot.child("Year").getValue(String.class)));
                        pd.dismiss();
                        Toast.makeText(Form2Activity.this, "You have been added to the list!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Form2Activity.this, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
