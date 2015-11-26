package com.ort.num172159_180968.fut5;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;


import com.facebook.login.widget.ProfilePictureView;
import com.magnet.android.mms.MagnetMobileClient;
import com.magnet.android.mms.async.Call;
import com.ort.num172159_180968.fut5.controller.api.User;
import com.ort.num172159_180968.fut5.controller.api.UserFactory;
import com.ort.num172159_180968.fut5.model.beans.AddUserImageRequest;
import com.ort.num172159_180968.fut5.model.beans.UpdateUserRequest;
import com.ort.num172159_180968.fut5.model.persistance.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class Profile extends AppCompatActivity {

    ImageView viewImage;
    ImageButton btnCamera;
    ImageButton btnSave;
    private User user;
    private SessionManager session;
    private String user_name;
    private int userId;
    private String id_facebook;
    private ProfilePictureView mProfilePicture;

    private EditText txtName;
    private EditText txtLastName;
    private DatabaseHelper db;
    private Bitmap image;


    private boolean rotation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(getApplicationContext());
        setContentView(R.layout.activity_profile);
        try {
            setUpUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session = new SessionManager(getApplicationContext());

        HashMap<String, String> user = session.getUserDetails();
        user_name = user.get(SessionManager.KEY_USERNAME);

        btnSave = (ImageButton)findViewById(R.id.btnSave);
        btnCamera = (ImageButton)findViewById(R.id.btnSelectPhoto);

        id_facebook = getIntent().getStringExtra("id_fb");
        System.out.println("id en el perfil: " + id_facebook);

        txtName = (EditText) findViewById(R.id.txtName);
        txtLastName = (EditText) findViewById(R.id.txtLastName);

        mProfilePicture = (ProfilePictureView) findViewById(R.id.profilePicturePerfil);
        mProfilePicture.setProfileId(id_facebook);

        Button btnTest = (Button)findViewById(R.id.button);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test();
            }
        });
        if(id_facebook != null){
            btnSave.setVisibility(View.INVISIBLE);
            btnCamera.setVisibility(View.INVISIBLE);
            txtName.setEnabled(false);
            txtName.setText(user_name);
            txtLastName.setEnabled(false);
            //txtLastName.setText(last_name);

        } else {
            com.ort.num172159_180968.fut5.model.persistance.User dbUser;
            try {
                System.out.println(user_name);
                dbUser = db.getUser(user_name);
            } catch (RuntimeException e) {
                AppHelper helper = new AppHelper(getApplicationContext());
                helper.reloadUsers();
                dbUser = db.getUser(user_name);
            }
            userId = dbUser.getUserId();
            ((EditText) findViewById(R.id.txtName)).setText(dbUser.getFirstName());
            ((EditText) findViewById(R.id.txtUsername)).setText(dbUser.getUsername());
            ((EditText) findViewById(R.id.txtEmail)).setText(dbUser.getEmail());
            ((EditText) findViewById(R.id.txtLastName)).setText(dbUser.getLastName());
            if (!dbUser.getPhoto().isEmpty()) {
                byte[] decodedString = Base64.decode(dbUser.getPhoto(), Base64.DEFAULT);
                Bitmap userImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                ((ImageView)findViewById(R.id.viewImage)).setImageBitmap(userImage);

            }

            /*Uri uri = Uri.parse("https://graph.facebook.com/10156116930130472/picture?height=512&width=512&migration_overrides=%7Boctober_2012%3Atrue%7D");
            ((ImageView)findViewById(R.id.viewImage)).setImageURI(null);
            ((ImageView)findViewById(R.id.viewImage)).setImageURI(uri);*/
        }

        btnCamera = (ImageButton)findViewById(R.id.btnSelectPhoto);
        viewImage = (ImageView)findViewById(R.id.viewImage);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        btnSave = (ImageButton)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser();
            }
        });
    }

    private void test() {
        AppHelper helper = new AppHelper(getApplicationContext());
        ImageView profileImageView = ((ImageView)mProfilePicture.getChildAt(0));
        Bitmap bitmap  = ((BitmapDrawable)profileImageView.getDrawable()).getBitmap();
        System.out.println("bitmaptoString: " + helper.BitMapToString(bitmap));
    }

    private void saveUser() {
        com.ort.num172159_180968.fut5.model.persistance.User dbUser;
        UpdateUserRequest body;
        dbUser = db.getUser(userId);
        dbUser.setUsername(((EditText)findViewById(R.id.txtUsername)).getText().toString());
        dbUser.setFirstName(((EditText) findViewById(R.id.txtName)).getText().toString());
        dbUser.setLastName(((EditText) findViewById(R.id.txtLastName)).getText().toString());
        dbUser.setEmail(((EditText)findViewById(R.id.txtEmail)).getText().toString());

        if(image != null) {
            image = (new AppHelper(getApplicationContext())).getResizedBitmap(image, 512, 512);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            try {
            } catch (Exception e){
                e.printStackTrace();
            }
            UpdateUserRequest.UpdateUserRequestBuilder builder = new UpdateUserRequest.UpdateUserRequestBuilder();
            builder.image(encoded);
            body = builder.build();
            //user.addUserImage(user_name, body, null);
            dbUser.setPhoto(encoded);
        } else {
            UpdateUserRequest.UpdateUserRequestBuilder builder = new UpdateUserRequest.UpdateUserRequestBuilder();
            builder.image(dbUser.getPhoto());
            body = builder.build();
        }

        Call<String> callObject = user.updateUser(userId + "", dbUser.getUsername(), ((EditText) findViewById(R.id.txtPassword)).getText().toString(),
                dbUser.getFirstName(), dbUser.getLastName(), dbUser.getEmail(), body, null);
        try {
            if (callObject.get().equals("true")) {
                db.updateUser(dbUser);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_perfil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void selectImage() {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    //bitmapOptions

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);

                    Matrix matrix = check_orientation(f.getAbsolutePath());

                    Bitmap adjustedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    image = adjustedBitmap;
                    //viewImage.setImageBitmap(adjustedBitmap);

                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                //Log.w("path of image from gallery......******************.........", picturePath + "");
                image = thumbnail;
                //viewImage.setImageBitmap(thumbnail);

            }
            AppHelper helper = new AppHelper(getApplicationContext());
            viewImage.setImageBitmap(helper.getResizedBitmap(image, 250, 250));
        }
        mProfilePicture.setVisibility(View.INVISIBLE);

    }

    private Matrix check_orientation(String path) throws IOException{

        ExifInterface exif = new ExifInterface(path);

        int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int rotationInDegrees = exifToDegrees(rotation);

        System.out.println("Rotation: " + rotation);
        System.out.println("RotationInDegrees: " + rotationInDegrees);

        Matrix matrix = new Matrix();
        if (rotation != 0f) {
            matrix.preRotate(rotationInDegrees);
        }

        return matrix;

    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }

    protected void setUpUser() throws Exception {
        // Instantiate a controller
        MagnetMobileClient magnetClient = MagnetMobileClient.getInstance(this.getApplicationContext());
        UserFactory controllerFactory = new UserFactory(magnetClient);
        user = controllerFactory.obtainInstance();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.closeDB();
    }

}


