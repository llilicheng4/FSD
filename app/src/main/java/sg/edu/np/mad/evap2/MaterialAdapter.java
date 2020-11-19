package sg.edu.np.mad.evap2;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialViewHolder> {
    ArrayList<LMaterial> materialsArrayList;
    Activity activityMain;
    String materialKey;
    StorageReference mStorageRef;
    StorageReference ref;

    public MaterialAdapter(ArrayList<LMaterial> LMaterials, Activity parentActivity) {
        materialsArrayList = LMaterials;
        activityMain = parentActivity;
    }

    @NonNull
    @Override
    public MaterialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_learning_material, parent, false);
        MaterialViewHolder viewHolder = new MaterialViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialViewHolder holder, int position) {
        final LMaterial lm = materialsArrayList.get(position);

        holder.MaterialName.setText(lm.getMaterialName());
        holder.MaterialDesc.setText(lm.getMaterialDesc());
        if (lm.isDone()) {
            holder.MaterialDone.setText("Done");
        } else {
            holder.MaterialDone.setText("Not Done");
        }

    }

    @Override
    public int getItemCount() {
        return materialsArrayList.size();
    }

    public void downloadItems(Context context, String filename, String fileExtension, String destinationDirectory, String url) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, filename + fileExtension);

        downloadManager.enqueue(request);
    }

    public void startDownload(String Name, final Context context) {
        mStorageRef = FirebaseStorage.getInstance().getReference();
        ref = mStorageRef.child(Name);

        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                downloadItems(context, "Mobile", ".txt", DIRECTORY_DOWNLOADS, url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}
