package sg.edu.np.mad.evap2;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

    public class grpchat extends AppCompatActivity {
        TextView groupname;

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.groupchat);

            groupname = findViewById(R.id.tvStudygrpname);

            String grpname = getIntent().getStringExtra("groupname");

            groupname.setText(grpname);
        }
}
