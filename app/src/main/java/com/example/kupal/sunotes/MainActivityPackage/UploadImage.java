package com.example.kupal.sunotes.MainActivityPackage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kupal.sunotes.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UploadImage extends AppCompatActivity {

    private Button button;
    private String userid = "";
    private StorageReference ref;
    private static final int GALLERY_INTENT = 2;
    private ProgressDialog progressDialog;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        progressDialog = new ProgressDialog(this);
        ref = FirebaseStorage.getInstance().getReference();
        button = (Button) findViewById(R.id.selectimage);
        imageView = (ImageView) findViewById(R.id.upload_image);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            progressDialog.setMessage("Uploading...");
            progressDialog.show();
            Uri uri = data.getData();
            Log.d("image ", uri.toString());
            StorageReference filepath = ref.child("Photos").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") Uri uri = taskSnapshot.getDownloadUrl();
                    Picasso.with(getApplicationContext()).load(uri).centerCrop()
                            .resize(imageView.getMeasuredWidth(), imageView.getMeasuredHeight())
                            .into(imageView);
                    Intent intent = new Intent();
                    intent.putExtra("url", uri.toString());
                    setResult(RESULT_OK, intent);
                    finish();
                    //Toast.makeText(getApplicationContext(), "Upload Success " + uri.toString(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Upload Not Success", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
