package in.swapsha96.imat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    ArrayList arrayList;
    DatabaseReference reference;
    Context context;

    public UserListAdapter(Context context, ArrayList arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_card,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        reference.child("Online").child("0").child(arrayList.get(position).toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                holder.name.setText(dataSnapshot.child("Name").getValue(String.class));
                holder.start.setText(dataSnapshot.child("Time_In").getValue(String.class));
                holder.end.setText(dataSnapshot.child("Time_Out").getValue(String.class));
                holder.location.setText(dataSnapshot.child("Destination").getValue(String.class));
                holder.purpose.setText(dataSnapshot.child("Purpose").getValue(String.class));

                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ProfileActivity.class);
                        intent.putExtra("uid", arrayList.get(position).toString());
                        context.startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView name, start, end, location, purpose;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            name = (TextView) itemView.findViewById(R.id.name);
            start = (TextView) itemView.findViewById(R.id.start);
            end = (TextView) itemView.findViewById(R.id.end);
            location = (TextView) itemView.findViewById(R.id.textView10);
            purpose = (TextView) itemView.findViewById(R.id.spinner_in);
        }
    }
}
