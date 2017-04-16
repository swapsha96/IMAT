package in.swapsha96.imat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class ShowAllActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ShowAllActivity.this));

        ArrayList arrayList = getIntent().getExtras().getStringArrayList("list");

        UserListAdapter recyclerViewAdapter = new UserListAdapter(ShowAllActivity.this, arrayList);
        recyclerView.setAdapter(recyclerViewAdapter);
        registerForContextMenu(recyclerView);
    }
}
