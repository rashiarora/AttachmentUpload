package com.rashi.attachmentsupload;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txtTitle;
    EditText txtName;
    ImageView iv;
    Button btnUpload;
    ProgressDialog dialog;
    String encodedImage;
    byte[] byteArray;
    Bean bean;
    RequestQueue requestQueue;

    void initViews(){
        txtTitle = (TextView)findViewById(R.id.textViewHeading);
        txtName = (EditText) findViewById(R.id.textFileName);
        iv = (ImageView)findViewById(R.id.imageAttachment);
        btnUpload = (Button)findViewById(R.id.buttonUpload);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading Image...");
        dialog.setCancelable(false);

        bean = new Bean();
        requestQueue = Volley.newRequestQueue(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        btnUpload.setOnClickListener(this);
        iv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v.getId()== R.id.buttonUpload){
            Bitmap image = ((BitmapDrawable)iv.getDrawable()).getBitmap();
            dialog.show();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArray = byteArrayOutputStream.toByteArray();
            image.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
            bean.setFilePath(txtName.getText().toString().trim());
            Log.i("send data","working");
            sendAttachment();

        }else if(v.getId()== R.id.imageAttachment){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,101);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==101 && resultCode== RESULT_OK && data!= null){
            Uri selectedImageUri = data.getData();
            iv.setImageURI(selectedImageUri);
        }
    }

    void sendAttachment(){
StringRequest stringRequest = new StringRequest(Request.Method.POST, Util.urlUpload, new Response.Listener<String>() {
    @Override
    public void onResponse(String response) {
        Log.i("Message from server", response);
        dialog.dismiss();
        Toast.makeText(getApplication(), "Attachment Uploaded Successfully", Toast.LENGTH_SHORT).show();
    }
}, new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError error) {
        Log.i("err", error.toString());
        dialog.dismiss();
        Toast.makeText(getApplication(), "Attachment Upload Unsuccessful", Toast.LENGTH_SHORT).show();
    }
})
{
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        HashMap<String,String> map = new HashMap<>();
        map.put("imageName",bean.getFilePath());
        map.put("imageData",encodedImage);
        return map;
    }
};
stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
requestQueue.add(stringRequest);
    }
}
