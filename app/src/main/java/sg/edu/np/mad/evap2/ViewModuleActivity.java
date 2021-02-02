package sg.edu.np.mad.evap2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ViewModuleActivity extends AppCompatActivity {
    final String TAG = "viewModuleActivity";
    ImageView arrowBack, image;
    TextView title, name, nameOfMod, description1, description2;
    Button enroll;
    DatabaseReference dbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_module);
        arrowBack = findViewById(R.id.imageView3);
        title = findViewById(R.id.textView8);
        name = findViewById(R.id.textView13);
        nameOfMod = findViewById(R.id.textView16);
        description1 = findViewById(R.id.textView15);
        description2 = findViewById(R.id.textView19);
        enroll = findViewById(R.id.btnEnroll);
        image = findViewById(R.id.imageView2);

        Intent data = getIntent();
        Module viewedModule = (Module)data.getSerializableExtra("Module");
        final String moduleName = viewedModule.getModName();
        String modDesc = viewedModule.getModDesc();
        String modDesc2 = viewedModule.getModDesc2();
        final String moduleID = viewedModule.getModID();
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title.setText(moduleID.toUpperCase());
        name.setText(moduleName.toUpperCase());
        nameOfMod.setText(moduleID);
        description1.setText(modDesc);
        description2.setText(modDesc2);
        dbRef = FirebaseDatabase.getInstance().getReference();
        final String uid = FirebaseAuth.getInstance().getUid();
        StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("modules/"+moduleID+"/ppic.png");
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(image);
            }
        });

        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query search = dbRef.child("enrollment").child(uid).child(moduleID);
                search.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d(TAG, String.valueOf(snapshot.getValue()));
                        if(!String.valueOf(snapshot.getValue()).equals("null")){
                            Toast.makeText(ViewModuleActivity.this, "You are already enrolled in this module", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            dbRef.child("enrollment").child(uid).child(moduleID).setValue(moduleID);
                            Toast.makeText(ViewModuleActivity.this, "You are now enrolled in this module", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


    }
}