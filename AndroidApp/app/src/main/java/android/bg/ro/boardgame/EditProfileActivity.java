package android.bg.ro.boardgame;

import android.bg.ro.boardgame.models.User;
import android.bg.ro.boardgame.services.CustomParser;
import android.bg.ro.boardgame.services.GenericHttpService;
import android.bg.ro.boardgame.services.TaskDelegate;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity implements TaskDelegate {

    private static int RESULT_LOAD_IMAGE = 1;
    private ImageView profilePicture;
    private EditText name;
    private EditText email;
    private EditText town;
    private boolean isUpdate;
    private boolean isUpdatePicture;

    private TaskDelegate taskDelegate;
    private GenericHttpService genericHttpService;
    private int userId;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        Bundle bundle = getIntent().getExtras();
        userId = bundle.getInt("userId");
        String nameString = bundle.getString("name");
        String emailString = bundle.getString("email");
        String townString = bundle.getString("town");
        isUpdate = false;
        isUpdatePicture = false;

        URL url = null;
        try {
            url = new URL("http://" + getResources().getString(R.string.localhost) + "/searchUser");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        taskDelegate = this;
        List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
        parameters.add(new Pair<>("userId",String.valueOf(userId)));
        parameters.add(new Pair<>("searchUserId",String.valueOf(userId)));

        genericHttpService = (GenericHttpService) new GenericHttpService(this,"searchUser", parameters,taskDelegate).execute(url);

        profilePicture = (ImageView) findViewById(R.id.imgView);
        profilePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);

            }
        });

        TextView title = findViewById(R.id.textSignUp_title);
        title.setText("Edit profile");
        TextView textPassword = findViewById(R.id.textPassword);
        textPassword.setVisibility(View.GONE);
        EditText editPassword = findViewById(R.id.editPassword);
        editPassword.setVisibility(View.GONE);
        Button signupButton = (Button) findViewById(R.id.buttonSignUp);
        signupButton.setText("Update");
        email = (EditText) findViewById(R.id.editEmail);
        email.setText(emailString);
        email.setEnabled(false);
        name = (EditText) findViewById(R.id.editName);
        name.setText(nameString);
        town = (EditText) findViewById(R.id.editTown);
        town.setText(townString);
        taskDelegate = this;


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BitmapDrawable drawable = (BitmapDrawable) profilePicture.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                bitmap = scaleImage(bitmap);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                bitmap.recycle();
                String profilePhoto = Base64.encodeToString(byteArray, Base64.DEFAULT);

                URL url = null;
                try {
                    url = new URL("http://" + getResources().getString(R.string.localhost) + "/updateBgUser");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                List<Pair<String, String>> params = new ArrayList<Pair<String, String>>();
                params.add(new Pair<>("id",String.valueOf(userId)));
                params.add(new Pair<>("name",name.getText().toString()));
                params.add(new Pair<>("email",email.getText().toString()));
                params.add(new Pair<>("town",town.getText().toString()));
                params.add(new Pair<>("photo", profilePhoto));

                genericHttpService = (GenericHttpService) new GenericHttpService(EditProfileActivity.this.getApplicationContext(),"updateBgUser", params,taskDelegate).execute(url);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.imgView);

            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(selectedImage);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            imageView.setImageBitmap(bmp);
            isUpdatePicture = true;

        }
    }


    private String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        BitmapFactory.Options options=new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
        options.inPurgeable = true; // inPurgeable is used to free up memory while required
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    private static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    private Bitmap scaleImage(Bitmap bitmap){
        final int maxSize = 960;
        int outWidth;
        int outHeight;
        int inWidth = bitmap.getWidth();
        int inHeight = bitmap.getHeight();
        if(inWidth > inHeight){
            outWidth = maxSize;
            outHeight = (inHeight * maxSize) / inWidth;
        } else {
            outHeight = maxSize;
            outWidth = (inWidth * maxSize) / inHeight;
        }

        return Bitmap.createScaledBitmap(bitmap, outWidth, outHeight, false);
    }

    @Override
    public void TaskCompletionResult(String result) {
        switch (genericHttpService.getResponseCode()) {
            case 200:
                if(genericHttpService.getMapping().equals("searchUser")) {
                    CustomParser customParser = new CustomParser();
                    user = customParser.getSearchUser(genericHttpService.getResponse());
                    EditText name = findViewById(R.id.editName);
                    name.setText(user.getName());
                    EditText town = findViewById(R.id.editTown);
                    town.setText(user.getTown());
                    EditText email = findViewById(R.id.editEmail);
                    email.setText(user.getEmail());

                    ImageView profilePic = (ImageView) findViewById(R.id.imgView);

                    BitmapFactory.Options options = new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
                    options.inPurgeable = true; // inPurgeable is used to free up memory while required
                    Bitmap songImage = BitmapFactory.decodeByteArray(user.getProfilePicture(), 0, user.getProfilePicture().length, options);//Decode image, "thumbnail" is the object of image file
                    profilePic.setImageBitmap(songImage);
                }
                break;
            case 202:
                isUpdate = true;
                Toast.makeText(EditProfileActivity.this, "The user was successfully updated.",
                        Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        if(isUpdate){
            if(isUpdatePicture) {
                BitmapDrawable drawable = (BitmapDrawable) profilePicture.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                bitmap = scaleImage(bitmap);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                bundle.putByteArray("photo",byteArray);

            }
            else {
                bundle.putByteArray("photo",null);
            }
            bundle.putString("town",town.getText().toString());
            bundle.putString("name",name.getText().toString());
        }
        else {
            bundle.putByteArray("photo",null);
            bundle.putString("town",null);
            bundle.putString("name",null);
        }
        intent.putExtras(bundle);
        setResult(20, intent);
        super.onBackPressed();
    }
}
