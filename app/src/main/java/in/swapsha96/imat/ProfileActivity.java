package in.swapsha96.imat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    Button call;
    TextView name, branch, degree, destination, gender, hostel, purpose, state, year, timeIn, timeOut;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        call = (Button) findViewById(R.id.call);
        name = (TextView) findViewById(R.id.name);
        branch = (TextView) findViewById(R.id.branch);
        degree = (TextView) findViewById(R.id.degree);
        destination = (TextView) findViewById(R.id.destination);
        gender = (TextView) findViewById(R.id.gender);
        hostel = (TextView) findViewById(R.id.hostel);
        purpose = (TextView) findViewById(R.id.spinner_in);
        state = (TextView) findViewById(R.id.state);
        year = (TextView) findViewById(R.id.year);
        timeIn = (TextView) findViewById(R.id.time_in);
        timeOut = (TextView) findViewById(R.id.time_out);
        reference = FirebaseDatabase.getInstance().getReference();

        String uid = getIntent().getExtras().getString("uid");
        reference.child("Online").child("0").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("Name").getValue(String.class));
                branch.setText(dataSnapshot.child("Branch").getValue(String.class));
                degree.setText(dataSnapshot.child("Degree").getValue(String.class));
                destination.setText(dataSnapshot.child("Destination").getValue(String.class));
                gender.setText(dataSnapshot.child("Gender").getValue(String.class));
                hostel.setText(dataSnapshot.child("Hostel").getValue(String.class));
                purpose.setText(dataSnapshot.child("Purpose").getValue(String.class));
                state.setText(dataSnapshot.child("State").getValue(String.class));
                year.setText(dataSnapshot.child("Year").getValue(Integer.class).toString());
                timeIn.setText(dataSnapshot.child("Time_In").getValue(String.class));
                timeOut.setText(dataSnapshot.child("Time_Out").getValue(String.class));

                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:"+dataSnapshot.child("Phone Number").getValue(String.class)));
                        if (ActivityCompat.checkSelfPermission(ProfileActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(callIntent);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
