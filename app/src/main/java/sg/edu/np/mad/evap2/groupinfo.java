package sg.edu.np.mad.evap2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class groupinfo extends AppCompatActivity {
    EditText email;
    ImageButton search;
    RecyclerView rvpeopleList;
    Toolbar toolbar;
    String grpname;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grpinfo);

        email = findViewById(R.id.etEmail);

        search = findViewById(R.id.btnSearch);

        rvpeopleList = findViewById(R.id.rvpeopleList);
        ref = FirebaseDatabase.getInstance().getReference();
        toolbar = findViewById(R.id.grpnamebar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Group info");

        grpname = getIntent().getStringExtra("groupname");

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailsearch = email.getText().toString();
                getUsername(grpname, emailsearch);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.grpinfomenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addmember:
                Intent i = new Intent(groupinfo.this, peoplelist.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getUsername(final String grpname, final String email){
        ref.child("Groups").child(grpname).child(email.replace(".","_")).setValue(email);
    }
}