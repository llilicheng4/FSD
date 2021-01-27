package sg.edu.np.mad.evap2;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class dlmaterialhistory extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlmaterialhistory);

        toolbar = findViewById(R.id.forumbar);

        //toolbar code
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Downloaded material history");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle button click here
        if (item.getItemId() == android.R.id.home) {
            setResult(Activity.RESULT_CANCELED);
            finish(); // close this activity and return to preview activity
        }
        return super.onOptionsItemSelected(item);
    }
}
