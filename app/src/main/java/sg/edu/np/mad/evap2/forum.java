package sg.edu.np.mad.evap2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class forum extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView rvForum;
    RecyclerView.LayoutManager layoutManager;

    DatabaseReference ref, eref;
    FirebaseAuth mauth;

    String currentuserid, currentemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum);

        rvForum = findViewById(R.id.rvforum);

        toolbar = findViewById(R.id.forumbar);

        mauth = FirebaseAuth.getInstance();

        //toolbar code
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Forum");

        //rv code here
        layoutManager = new LinearLayoutManager(this);
        rvForum.setLayoutManager(layoutManager);

        ref = FirebaseDatabase.getInstance().getReference().child("Forums");
        eref = FirebaseDatabase.getInstance().getReference().child("Forums");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.forummenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addforum:
                Intent i = new Intent(forum.this, addforum.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart(){
        super.onStart();

        //String fbemail = currentemail.replace(".","_");

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<forumclass>()
                        .setQuery(eref, forumclass.class)
                        .build();

        FirebaseRecyclerAdapter<forumclass, forum.GroupsViewHolder> adapter
                = new FirebaseRecyclerAdapter<forumclass, forum.GroupsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final forum.GroupsViewHolder groupsViewHolder, int i, @NonNull forumclass forum) {
                String grpid = getRef(i).getKey();
                ref.child(grpid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String title = snapshot.child("title").getValue().toString();
                        String content = snapshot.child("content").getValue().toString();
                        String category = snapshot.child("category").getValue().toString();
                        String status = snapshot.child("status").getValue().toString();
                        String answer = snapshot.child("noofanswers").getValue().toString();

                        groupsViewHolder.title.setText(title);
                        groupsViewHolder.content.setText(content);
                        groupsViewHolder.status.setText(status);
                        groupsViewHolder.category.setText(category);
                        groupsViewHolder.noofanswers.setText(answer);

                        final String titleText = groupsViewHolder.title.getText().toString();
                        final String contentText = groupsViewHolder.content.getText().toString();
                        final String categoryText = groupsViewHolder.category.getText().toString();

                        groupsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(forum.this, forumchat.class);

                                intent.putExtra("title", titleText);
                                intent.putExtra("content", contentText);
                                intent.putExtra("category", categoryText);

                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @NonNull
            @Override
            public forum.GroupsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvforum, parent,false);
                forum.GroupsViewHolder viewholder = new forum.GroupsViewHolder(view);
                return viewholder;
            }
        };

        rvForum.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();
    }

    public static class GroupsViewHolder extends RecyclerView.ViewHolder{

        TextView title, content, status, noofanswers, category;

        public GroupsViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tvTitle);
            content = itemView.findViewById(R.id.tvQuestion);
            status = itemView.findViewById(R.id.tvAnswered);
            noofanswers = itemView.findViewById(R.id.tvVoteCount);
            category = itemView.findViewById(R.id.tvModule);
        }
    }

}