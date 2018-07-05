package project02.csc214.mysns.Fragments;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import project02.csc214.mysns.AppDatabase;
import project02.csc214.mysns.MainActivity;
import project02.csc214.mysns.R;
import project02.csc214.mysns.RegisterActivity;
import project02.csc214.mysns.model.Profile;

public class EditProfileFragment extends Fragment {
    private static final String USER = "CurrentUser";
    private static final String VIEWUSER = "ViewingProfile";
    private static final String DBNAME = "MainDB";

    EditText mFirstName;
    EditText mLastName;
    EditText mBirth;
    EditText mHometown;
    EditText mBio;
    Button mSubmit;
    Button mUpload;
    String mPhotoPath;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        mFirstName = (EditText) view.findViewById(R.id.edit_profile_firstname);
        mLastName = (EditText) view.findViewById(R.id.edit_profile_lastname);
        mBirth = (EditText) view.findViewById(R.id.edit_profile_birth);
        mHometown = (EditText) view.findViewById(R.id.edit_profile_hometown);
        mBio = (EditText) view.findViewById(R.id.edit_profile_bio);
        mUpload = (Button) view.findViewById(R.id.image_upload);
        mSubmit = (Button) view.findViewById(R.id.edit_profile_submit);

        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    File mPhotoFile = null;
                    try {
                        mPhotoFile = createImageFile();
                    } catch (IOException e) {
                        Toast.makeText(getActivity(), "Error creating file for picture",
                                Toast.LENGTH_LONG).show();
                    }
                    if (mPhotoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(getActivity(),
                                "project02.csc214.mysns.fileprovider", mPhotoFile);
                        mPhotoPath = mPhotoFile.getPath();
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(intent, 0);
                    }
                } else {
                    Toast.makeText(getActivity(), "No camera app available to take photo!",
                            Toast.LENGTH_LONG).show();
                }

            }
        });


        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname = mFirstName.getText().toString();
                String lastname = mLastName.getText().toString();
                String birth = mBirth.getText().toString();
                String homwtown = mHometown.getText().toString();
                String bio = mBio.getText().toString();
                String path = mPhotoPath;


                ListFragment.OnListFragmentInteractionListener olfil = (ListFragment.OnListFragmentInteractionListener) getActivity();
                ProfileFragment.OnProfileFragmentInteractionListener opfil = (ProfileFragment.OnProfileFragmentInteractionListener) getActivity();

                if (firstname!=null && lastname!=null && birth!=null && homwtown!=null && bio!=null){
                    if (bio.length()<150) {
                        AppDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(),
                                AppDatabase.class, DBNAME).allowMainThreadQueries().build();
                        Profile tobeEdited = db.profileDao().getById(olfil.getUid());
                        if (firstname.length()>0) {
                            tobeEdited.setFirstname(firstname);
                        }
                        if (lastname.length()>0) {
                            tobeEdited.setLastname(lastname);
                        }
                        if (birth.length()>0) {
                            tobeEdited.setBirth_date(birth);
                        }
                        if (homwtown.length()>0) {
                            tobeEdited.setHometown(homwtown);
                        }
                        if (bio.length()>0) {
                            tobeEdited.setBio(bio);
                        }
                        if (path!=null) {
                            tobeEdited.setPhoto_url(path);
                        }
                        db.profileDao().update(tobeEdited);
                        db.close();
                        ProfileFragment profile = new ProfileFragment();
                        opfil.setTriggered(false);
                        olfil.removeFunction();
                        olfil.bootFunction(profile);
                        db.close();
                    }else{
                        Toast toast = Toast.makeText(getActivity(),"Bio too long",Toast.LENGTH_LONG);
                        toast.show();
                    }

                }else{
                    Toast toast = Toast.makeText(getActivity(),"Please fill in all fields",Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        });



        return view;
    }

    private File createImageFile() throws IOException {
        String imageFileName = "jpg_" + UUID.randomUUID();
        File image = File.createTempFile(imageFileName, ".jpg",getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        return image;
    }

}