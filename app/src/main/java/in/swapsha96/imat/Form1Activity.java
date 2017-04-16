package in.swapsha96.imat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Form1Activity extends AppCompatActivity {

    String result = "";
    Button button;
    Spinner dest, purpose;
    DatabaseReference reference;
    ArrayList<String> list;
    Bundle bundle;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form1);

        list = new ArrayList<>();
        dest = (Spinner) findViewById(R.id.purpose);
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
        purpose = (Spinner) findViewById(R.id.spinner_in);

        list = new ArrayList<String>();
        list.add("Shopping");
        list.add("Eating");
        list.add("Medical");
        list.add("Religious");
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        purpose.setAdapter(dataAdapter);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        bundle = getIntent().getExtras();

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(Form1Activity.this);
                pd.setMessage("loading");
                pd.show();
                reference.child("Online").child("0").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final JSONObject object = new JSONObject();
                        JSONArray array = new JSONArray();
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            JSONObject person = new JSONObject();
                            try {
                                person.put("Branch", snapshot.child("Branch").getValue(String.class));
                                person.put("Date", snapshot.child("Date").getValue(String.class));
                                person.put("Degree", snapshot.child("Degree").getValue(String.class));
                                person.put("Destination", snapshot.child("Destination").getValue(String.class));
                                person.put("Gender", snapshot.child("Gender").getValue(String.class));
                                person.put("Hostel", snapshot.child("Hostel").getValue(String.class));
                                person.put("Name", snapshot.child("Name").getValue(String.class));
                                person.put("Year", snapshot.child("Year").getValue(Long.class));
                                person.put("Purpose", snapshot.child("Purpose").getValue(String.class));
                                person.put("State", snapshot.child("State").getValue(String.class));
                                person.put("Time_In", snapshot.child("Time_In").getValue(String.class));
                                person.put("Time_Out", snapshot.child("Time_Out").getValue(String.class));
                                person.put("uid", snapshot.getKey());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            array.put(person);
                        }
                        try {
                            object.put("data", array);
                            reference.child("Users").child("0").child(bundle.getString("uid")).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    try {
                                        object.put("User", "56");
                                        object.put("Name", dataSnapshot.child("Name").getValue(String.class));
                                        object.put("Branch", dataSnapshot.child("Branch").getValue(String.class));
                                        object.put("Degree", dataSnapshot.child("Degree").getValue(String.class));
                                        object.put("Gender", dataSnapshot.child("Gender").getValue(String.class));
                                        object.put("Hostel", dataSnapshot.child("Hostel").getValue(String.class));
                                        object.put("State", dataSnapshot.child("State").getValue(String.class));
                                        object.put("Year", dataSnapshot.child("Year").getValue(String.class));
                                        object.put("Purpose", purpose.getSelectedItem().toString());
                                        object.put("Destination", dest.getSelectedItem().toString());
                                        new JSONTask().execute(object.toString());

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(Form1Activity.this, e.toString(), Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Form1Activity.this, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    public class JSONTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            String param = "?data=";
            try {
                param += URLEncoder.encode(params[0], "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                URL url = new URL("http://192.168.43.136:5000/list"+ param);
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();

                int respCode = connection.getResponseCode();
                if(respCode == HttpURLConnection.HTTP_OK) {
                    InputStream stream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    stream.close();
                    result = sb.toString();
                }
                else {
                    return  "Error code: " + respCode;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
//            button.setText(s);
            HashMap<Integer,String> map = new HashMap<Integer, String>();
            try {
                JSONObject jsonObj = new JSONObject(s);
                JSONArray array = jsonObj.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject person = array.getJSONObject(i);
                    map.put(Integer.parseInt(person.getString("uid")),person.getString("score"));
//                    list.add(person.getString("uid"));
                }
//                Toast.makeText(Form1Activity.this, jsonObj.toString(), Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
//                Toast.makeText(Form1Activity.this, e.toString(), Toast.LENGTH_LONG).show();
            }

            Intent intent = new Intent(Form1Activity.this, ShowAllActivity.class);
            pd.dismiss();
            LinkedHashMap<Integer, String> linkedHashMap = sortHashMapByValues(map);
            for(Map.Entry<Integer, String> user : linkedHashMap.entrySet()) {
                list.add(0, user.getKey().toString());
            }
            intent.putExtra("list", list);
            intent.putExtra("uid", bundle.get("uid").toString());
            startActivity(intent);
//            reference.child("Online").child("0").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
//                        String key = postSnapshot.getKey().toString();
//                        list.add(key);
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
        }
    }

    public LinkedHashMap<Integer, String> sortHashMapByValues(HashMap<Integer, String> passedMap) {
        List<Integer> mapKeys = new ArrayList<>(passedMap.keySet());
        List<String> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap<Integer, String> sortedMap =
                new LinkedHashMap<>();

        Iterator<String> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            String val = valueIt.next();
            Iterator<Integer> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                Integer key = keyIt.next();
                String comp1 = passedMap.get(key);
                String comp2 = val;

                if (comp1.equals(comp2)) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }
}
