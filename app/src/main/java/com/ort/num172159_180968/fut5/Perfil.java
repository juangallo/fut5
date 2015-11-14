package com.ort.num172159_180968.fut5;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

<<<<<<< HEAD
import com.facebook.AccessToken;
import com.facebook.FacebookActivity;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
=======
>>>>>>> a9b0492b3eab49e3b251a0de34168ea7d601d3bf
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Perfil extends AppCompatActivity {

    private JSONObject objectListFriends;
    private ProfilePictureView pictureGallo;
    private EditText txtGallo;

    ImageView viewImage;
    Button btnCamera;
    Button btnSave;

    private String user_name;
    private String last_name;
    private String id_facebook;
    private ProfilePictureView mProfilePicture;

    private EditText txtName;
    private EditText txtLastName;

    private boolean rotation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        btnSave = (Button)findViewById(R.id.btnSave);
        btnCamera = (Button)findViewById(R.id.btnSelectPhoto);

        user_name = getIntent().getStringExtra("user_name");
        last_name = getIntent().getStringExtra("last_name");
        id_facebook = getIntent().getStringExtra("id_fb");
        System.out.println("id en el perfil: " + id_facebook);

        txtName = (EditText) findViewById(R.id.txtName);
        txtLastName = (EditText) findViewById(R.id.txtLastName);

        mProfilePicture = (ProfilePictureView) findViewById(R.id.profilePicturePerfil);
        mProfilePicture.setProfileId(id_facebook);

        if(id_facebook != null){
            btnSave.setVisibility(View.INVISIBLE);
            btnCamera.setVisibility(View.INVISIBLE);
            txtName.setEnabled(false);
            txtName.setText(user_name);
            txtLastName.setEnabled(false);
            txtLastName.setText(last_name);

        } else {

        }

        pictureGallo = (ProfilePictureView) findViewById(R.id.profileGallo);
        txtGallo = (EditText) findViewById(R.id.txtGallo);

        //loadFriends();

        btnCamera=(Button)findViewById(R.id.btnSelectPhoto);
        viewImage=(ImageView)findViewById(R.id.viewImage);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
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

        AlertDialog.Builder builder = new AlertDialog.Builder(Perfil.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                }
                else if (options[item].equals("Cancel")) {
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


                /*Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
                    viewImage.setRotation(270);
                } else {
                    viewImage.setRotation(90);
                }*/



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

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);

                    viewImage.setImageBitmap(bitmap);

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
                viewImage.setImageBitmap(thumbnail);

            }
        }
        mProfilePicture.setVisibility(View.INVISIBLE);

    }

    private void loadFriends(){

        GraphRequest.Callback graphCallback = new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                JSONObject o = graphResponse.getJSONObject();
                objectListFriends = o;
                System.out.println("jsonobjeto: " + o);
            }
        };
        GraphRequest graph = new GraphRequest(AccessToken.getCurrentAccessToken(),"/me/friends",null, HttpMethod.GET,graphCallback);
        graph.executeAsync();

        String id = null;
        String name = null;

        //String json = "{'data':'x','paging':'x'}";
        try {
            Object mainObject  = objectListFriends.get("data");
            System.out.println(mainObject);
            /*JSONObject contentObject = mainObject.getJSONObject("content");
            System.out.println(contentObject);

            name = contentObject.getString("name");
            id = contentObject.getString("id");*/
        }catch (JSONException e) {
            e.printStackTrace();
        }

        pictureGallo.setProfileId(id);
        txtGallo.setText(name);

        /*new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        System.out.println("guardo el json: " + response.getJSONObject());

                        try {
                            Object o = response.getJSONObject().get("data");
                            System.out.println("objeto: " + o);
                            objectListFriends = o;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();*/
    }



}


