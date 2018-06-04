package com.example.joochahyana.orderup;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import static java.lang.Thread.sleep;

public class foodstaffActivity extends AppCompatActivity {



     // Camera Option
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_LOAD = 2;

    String mCurrentPhotoPath;
    Uri photoURI, albumURI = null;
    Boolean album = false;
    ImageView caputreImage;

    // text
    String foodName;
    String foodDescrption;
    String foodPrice;
    int foodTypeName;

    // input text
    EditText editTextFoodName;
    EditText editTextFoodDes;
    EditText editTextPrice;
    RadioGroup foodType;

    //button
    Button enrollButton, takingPicture, loadingPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodstaff);
        caputreImage  =(ImageView) findViewById(R.id.imagePicutreTest);

        takingPicture = (Button)findViewById(R.id.buttonTakingPicutre);
        loadingPicture =(Button)findViewById(R.id.buttonLoadingPicutre);
        enrollButton = (Button)findViewById(R.id.buttonEnroll);
        //taking button
        takingPicture.setOnClickListener(new Button.OnClickListener(){
            @Override //
            public  void onClick(View view) {
                int permissionChedk = ContextCompat.checkSelfPermission(foodstaffActivity.this,Manifest.permission.CAMERA);
                if (permissionChedk==PackageManager.PERMISSION_DENIED)
                {
                    ActivityCompat.requestPermissions(foodstaffActivity.this,new String[]{
                            Manifest.permission.CAMERA},0);
                }
                else {
                    try {

                        dispatchTakePictureIntent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //loading button
        loadingPicture.setOnClickListener(new Button.OnClickListener(){
            @Override
            public  void onClick(View view) {
                doTakeAlbumAction();
            }
        });

        //enroll button
        enrollButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public  void onClick(View view){

                editTextFoodName = (EditText)findViewById(R.id.editTextFoodName);
                editTextFoodDes = (EditText)findViewById(R.id.editTextFoodDes);
                editTextPrice = (EditText)findViewById(R.id.editTextFoodPrice);

              foodName = editTextFoodName.getText().toString();
              foodDescrption = editTextFoodDes.getText().toString();
              foodPrice = editTextPrice.getText().toString();
              foodType = (RadioGroup)findViewById(R.id.radioGroup);
              foodTypeName = foodType.getCheckedRadioButtonId();


                if(foodName.length() != 0 && foodDescrption.length() != 0 && foodPrice.length() != 0 && foodTypeName != -1){
                    RadioButton radioButton = (RadioButton)findViewById(foodTypeName);
                    Toast.makeText(getApplicationContext(), radioButton.getText(), Toast.LENGTH_LONG).show();

                }
                else{
                    // Nothing.
                }

            }
        });




    }
    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permision,@NonNull int[] grantresult){
        if (requestCode==0)
            if (grantresult[0]==0){
                Toast.makeText(this,"camara permision ",Toast.LENGTH_LONG).show();

            }
            else {
                Toast.makeText(this,"camara permision denyed ",Toast.LENGTH_LONG).show();
            }
    }

    private void dispatchTakePictureIntent() throws IOException {


        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }



    }

    private  File createImageFile() throws  IOException{
        String imageFileName = "temp_" + String.valueOf(System.currentTimeMillis()+".jpg");
        File storageDir = new File(Environment.getExternalStorageDirectory(),imageFileName);
        mCurrentPhotoPath = storageDir.getAbsolutePath();
        return storageDir;
    }

    private void doTakeAlbumAction(){

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_IMAGE_LOAD);

    }



    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){



        if(resultCode != RESULT_OK){
            Toast.makeText(getApplicationContext(), "NOT OK RESULT", Toast.LENGTH_SHORT).show();
        }
        else{
            switch (requestCode){
                case REQUEST_IMAGE_LOAD:
                    album =true;
                        photoURI = data.getData();
                    if (photoURI==null)
                        Toast.makeText(getApplicationContext(),"null", Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(getApplicationContext(), photoURI.toString(), Toast.LENGTH_SHORT).show();


                        Bitmap bitmap = null;

                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoURI);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        caputreImage.setImageBitmap(bitmap);
                    }
                        break;
                case REQUEST_IMAGE_CAPTURE:
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    caputreImage.setImageBitmap(imageBitmap);
                    break;

            }

        }
    }



}
