package sg.edu.np.mad.evap2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

public class peoplelist extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView rvpeopleList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people_list);
        toolbar = findViewById(R.id.peoplelistbar);
        rvpeopleList = findViewById(R.id.rvpeopleList);

        rvpeopleList.setLayoutManager(new LinearLayoutManager(this));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add member");
    }

    @Override
    protected void onStart(){
        super.onStart();

        //FirebaseRecyclerAdapter<>
    }
}