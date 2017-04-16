package in.swapsha96.imat;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    Button button;
    EditText name, email, password;
    Spinner degree, year, branch, dest, gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText) findViewById(R.id.reg_name);
        email = (EditText) findViewById(R.id.reg_email);
        password = (EditText) findViewById(R.id.reg_password);
        degree = (Spinner) findViewById(R.id.degree);
        year = (Spinner) findViewById(R.id.year);
        branch = (Spinner) findViewById(R.id.branch);
        dest = (Spinner) findViewById(R.id.destination);
        gender = (Spinner) findViewById(R.id.gender);

        String[] stringsDegree = new String[] {"B.Tech", "M.Tech", "MS", "MSc", "PhD"};
        ArrayAdapter adapterDegree = new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_spinner_item, stringsDegree);
        adapterDegree.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        degree.setAdapter(adapterDegree);

        String[] stringsYear = new String[] {"2012", "2013", "2014", "2015", "2016"};
        ArrayAdapter adapterYear = new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_spinner_item, stringsYear);
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(adapterYear);

        String[] stringsBranch = new String[] {"CSE", "EE", "ME", "CE"};
        ArrayAdapter adapterBranch = new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_spinner_item, stringsBranch);
        adapterBranch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branch.setAdapter(adapterBranch);

        String[] stringsDest = new String[] {"Rajasthan", "Himachal Pradesh", "Punjab", "Chhattisgarh", "Uttar Pradesh"};
        ArrayAdapter adapterDest = new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_spinner_item, stringsDest);
        adapterDest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dest.setAdapter(adapterDest);

        String[] stringsGender = new String[] {"Female", "Male"};
        ArrayAdapter adapterGender = new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_spinner_item, stringsGender);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapterGender);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
//                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
//                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        mAuth.addAuthStateListener(mAuthListener);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                                }
                                else {

                                }
                            }
                        });
            }
        });
    }
}
