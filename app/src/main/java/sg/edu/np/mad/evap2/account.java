package sg.edu.np.mad.evap2;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.ContentResolver;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import java.util.List;

public class account extends Fragment {

    RecyclerView rvSchedule;
    RecyclerView.LayoutManager layoutManager;

    TextView tvUsername, tvEmail;

    CardView lessonmat;

    DatabaseReference scheduleref, uref;

    FirebaseAuth mauth;

    String currentEmail, currentUsername, currentUserID;
    Toolbar toolbar;
    //String scheduledate, schedulename, schedulelocation, idValue, title;


    /*cursor to get the content of the calendar
    Cursor cursor;
    end*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        container.removeAllViews();
        final View v = inflater.inflate(R.layout.account, container, false);

        rvSchedule = v.findViewById(R.id.rvSchedule);
        toolbar = v.findViewById(R.id.forumbar);
        tvUsername = v.findViewById(R.id.tvUsername);
        tvEmail = v.findViewById(R.id.tvEmail);

        lessonmat = v.findViewById(R.id.cvLessonMat);

        //code for getting child ref
        scheduleref = FirebaseDatabase.getInstance().getReference().child("Schedule");
        uref = FirebaseDatabase.getInstance().getReference().child("users");

        //init rv
        layoutManager = new LinearLayoutManager(v.getContext());
        rvSchedule.setLayoutManager(layoutManager);

        //firebase auth
        mauth = FirebaseAuth.getInstance();

        //get userid, email and username code
        currentEmail = mauth.getCurrentUser().getEmail();
        currentUserID = mauth.getCurrentUser().getUid();
        GetUsername(currentUserID);

        //set username textview and email textview with value
        tvEmail.setText(currentEmail);

        //code for card view lesson mat on click
        lessonmat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent to downloaded material history
                Intent intent = new Intent(getActivity(), dlmaterialhistory.class);
                startActivity(intent);
            }
        });

        /*final ContentResolver contentResolver = getContext().getContentResolver();

        if (ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return v;
        }
        cursor = contentResolver.query(CalendarContract.Events.CONTENT_URI, null, null, null, null);

        while(cursor.moveToNext()){
            if(cursor!=null){
                int coloindex = cursor.getColumnIndex(CalendarContract.Events._ID);
                int colotitle = cursor.getColumnIndex(CalendarContract.Events.TITLE);
                int colodesc = cursor.getColumnIndex(CalendarContract.Events.DESCRIPTION);
                int cololocation = cursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION);
                int coloday = cursor.getColumnIndex(CalendarContract.Events.DTSTART);

                idValue = cursor.getColumnName(coloindex);
                title = cursor.getColumnName(colotitle);
                scheduledate = cursor.getColumnName(coloday);
                schedulelocation = cursor.getColumnName(cololocation);
                schedulename = cursor.getColumnName(colodesc);

                slist.add(scheduledate);
                slist.add(schedulename);
                slist.add(schedulelocation);

                Toast.makeText(v.getContext(), idValue + ", " + title+ ", " +scheduledate+ ", " +schedulelocation+ ", " +schedulename, Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(v.getContext(),"There are no schedule in your calendar", Toast.LENGTH_SHORT).show();
            }
        }

        //init rv
        ScheduleAdapter adapter = new ScheduleAdapter(slist);

        LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());

        rvSchedule.setLayoutManager(layoutManager);
        rvSchedule.setItemAnimator(new DefaultItemAnimator());
        rvSchedule.setAdapter(adapter);*/

        return v;
    }

    //get current username
    private void GetUsername(String uid) {
        uref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    currentUsername = snapshot.child("username").getValue().toString();
                    tvUsername.setText(currentUsername);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();

        DatabaseReference sref = FirebaseDatabase.getInstance().getReference().child("Schedule");

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<accountClass>()
                        .setQuery(sref.orderByChild("Schedule"), accountClass.class)
                        .build();

        FirebaseRecyclerAdapter<accountClass, account.GroupsViewHolder> adapter
                = new FirebaseRecyclerAdapter<accountClass, account.GroupsViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final account.GroupsViewHolder groupsViewHolder, final int i, @NonNull accountClass accountClass) {
                String scheduleid = getRef(i).getKey();
                scheduleref.child(scheduleid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String day = snapshot.child("Day").getValue().toString();
                        String duration = snapshot.child("Duration").getValue().toString();
                        String location = snapshot.child("Location").getValue().toString();
                        String module = snapshot.child("Module").getValue().toString();

                        groupsViewHolder.sday.setText(day);
                        groupsViewHolder.module.setText(module);
                        groupsViewHolder.slocation.setText(location);
                        groupsViewHolder.sduration.setText(duration);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @NonNull
            @Override
            public account.GroupsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_schedule, parent,false);
                account.GroupsViewHolder viewholder = new account.GroupsViewHolder(view);
                return viewholder;
            }
        };

        rvSchedule.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();
    }

    public static class GroupsViewHolder extends RecyclerView.ViewHolder{

        public TextView sday, module, slocation, sduration;

        public GroupsViewHolder(@NonNull View itemView) {
            super(itemView);
            sday = itemView.findViewById(R.id.scheduleday);
            module = itemView.findViewById(R.id.module);
            slocation = itemView.findViewById(R.id.schedulelocation);
            sduration = itemView.findViewById(R.id.scheduleduration);
        }
    }
}
