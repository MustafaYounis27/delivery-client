package com.client.metyassara.ui.login.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.client.metyassara.R;
import com.client.metyassara.model.UserModel;
import com.client.metyassara.ui.home_page.HomePageActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpFragment extends Fragment {
    // 1- define signupFragment View
    private View signupFragment;
    private Context context;
    private EditText username, email, password, confirmPassword, phone;
    private CircleImageView profilee_img;
    private ProgressDialog dialog;
    private Button finsh;
    private Uri image_uri;
    private FirebaseDatabase firebaseDatabase;
    private Task<Void> databaseReference;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //define context
        context = getContext();
    }

    //2- override On Create view to inflate signupFragment View
    @Nullable
    @Override
    public View onCreateView
    (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //3- assign signupFragment view with view that return From inflate object that need (View Layout , Root)
        // to attach with layout
        signupFragment = inflater.inflate(R.layout.signup_fragment, null);
        return signupFragment;
    }
    //4- override On activity Created to inflate view in LoginActivity

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        intialviews();
        onslectImage();
        intialfirebase();
        onfinshclick();
    }

    private void intialviews() {
        profilee_img = signupFragment.findViewById(R.id.profile_image);
        username = signupFragment.findViewById(R.id.username_field_singup);
        email = signupFragment.findViewById(R.id.email_field_singup);
        password = signupFragment.findViewById(R.id.password_field_signup);
        confirmPassword = signupFragment.findViewById(R.id.confirm_password_field_signup);
        phone = signupFragment.findViewById(R.id.phone_field);

        finsh = signupFragment.findViewById(R.id.finish);
        //intial dialog
        dialog = new ProgressDialog(context);
        dialog.setTitle("Sign up");
        dialog.setMessage("plese wait...");
        dialog.setCancelable(false);
    }

    private void intialfirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
    }

    private void onslectImage() {
        profilee_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 505);
            }
        });
    }

    private void onfinshclick() {
        finsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    public void register() {
        //get all text from edite text and save in string to check it empty or no
        String usernamee = username.getText().toString();
        String emaill = email.getText().toString();
        String paswword = password.getText().toString();
        String confirmpaswword = confirmPassword.getText().toString();
        String phonee = phone.getText().toString();


        if (image_uri == null) {
            Toast.makeText(context, "please select photo", Toast.LENGTH_SHORT).show();
            return;
        }
        if (usernamee.isEmpty()) {
            Toast.makeText(context, "please enter username", Toast.LENGTH_SHORT).show();
            //بخلي يوديني لل edite text ده
            username.requestFocus();
            return;
        }
        if (emaill.isEmpty()) {
            Toast.makeText(context, "please enter email", Toast.LENGTH_SHORT).show();
            email.requestFocus();
            return;
        }
        if (paswword.isEmpty()) {
            Toast.makeText(context, "please enter password", Toast.LENGTH_SHORT).show();
            password.requestFocus();
            return;
        }
        if (!confirmpaswword.equals(paswword)) {
            Toast.makeText(context, "password does not match", Toast.LENGTH_SHORT).show();
            confirmPassword.requestFocus();
            return;
        }

        if (phonee.isEmpty()) {
            Toast.makeText(context, "please enter phone", Toast.LENGTH_SHORT).show();
            phone.requestFocus();
            return;
        }
        createmail(image_uri, usernamee, emaill, paswword, confirmpaswword, phonee);
    }

    private void savedata(Uri image_uri, String usernamee, String emaill, String paswword, String confirmpaswword, String phonee) {
        dialog.show();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        uploadphototostorage(image_uri, usernamee, uid, emaill, paswword, confirmpaswword, phonee);
    }

    private void uploadphototostorage(Uri image_uri, final String usernamee, final String uid, final String emaill, final String paswword, final String confirmpaswword, final String phonee) {
        storageReference = firebaseStorage.getReference().child("users_image/" + image_uri.getLastPathSegment());
        UploadTask uploadTask = storageReference.putFile(image_uri);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                String image_urL = task.getResult().toString();
                uploadtodatebase(image_urL, usernamee, uid, phonee);
            }
        });

    }

    private void createmail(final Uri image_uri, final String usernamee, final String emaill, final String paswword, final String confirmpaswword, final String phonee) {
        //sing up with email and password before uploade date in datatbase
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emaill, paswword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    savedata(image_uri, usernamee, emaill, paswword, confirmpaswword, phonee);

                } else {
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadtodatebase(String image_urL, String usernamee, String uid, String phonee) {
        UserModel userModel = new UserModel(image_urL, usernamee, uid, phonee);
        databaseReference = firebaseDatabase.getReference().child("users").child(uid).setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    dialog.dismiss();
                    Intent intent = new Intent(getActivity(), HomePageActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    dialog.dismiss();
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 505 && resultCode == getActivity().RESULT_OK && data != null) {
            image_uri = data.getData();
            Picasso.get().load(image_uri).into(profilee_img);
        }
    }
}
