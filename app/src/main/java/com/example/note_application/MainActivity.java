package com.example.note_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback{
    Bitmap bitmap;
    Button b;
    ImageView picture_img = (ImageView) findViewById(R.id.picture_img);
    ImageView show_image = (ImageView) findViewById(R.id.show_image);
    LinearLayout l1, l2, l3;
    private static final int PERMISSIOIN_REQUEST_CAMERA = 0;
    private static final int REQUEST_IMAGE_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        l1 = (LinearLayout) findViewById(R.id.lin1);
        l2 = (LinearLayout) findViewById(R.id.lin2);
        l2.setVisibility(View.INVISIBLE);
        l3 = (LinearLayout) findViewById(R.id.lin3);
        l3.setVisibility(View.INVISIBLE);
    }

    public void picture_button_click(View v){
        showCameraPerview();
    }

    public void draw_button_click(View v){
    }

    public void picture_click(View v, Bitmap bitmap){
        l1.setVisibility(View.INVISIBLE);
        l2.setVisibility(View.VISIBLE);
    }

    // 카메라 권한 요청 응답 처리
    public void onReqeustPermissioinsResult(int requestCode,
                                            @NonNull String[] permissioins, @NonNull int[] grantResults){
        if(requestCode == PERMISSIOIN_REQUEST_CAMERA){
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(), "권한 획득", Toast.LENGTH_SHORT).show();
                startCamera();
            }else{
                Toast.makeText(getApplicationContext(), "권한 획득 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 카메라 권한 확인
    private void showCameraPerview(){
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getApplicationContext(), "권한 획득", Toast.LENGTH_SHORT).show();
            startCamera();
        }else{
            requestCameraPermission();
        }
    }

    // 카메라 권한 요청
    private void requestCameraPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)){
            Toast.makeText(getApplicationContext(), "이 앱은 카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, PERMISSIOIN_REQUEST_CAMERA);
        }else{
            Toast.makeText(getApplicationContext(), "권한 획득 실패", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, PERMISSIOIN_REQUEST_CAMERA);
        }
    }

    // 카메라 실행
    private void startCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takePictureIntent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CODE && requestCode == RESULT_OK){
            Bundle extras = data.getExtras();
            // 이 비트맵 저장
            Bitmap imagebitmap = (Bitmap) extras.get("data");
            picture_img.setImageBitmap(imagebitmap);
            show_image.setImageBitmap(imagebitmap);
        }
    }

    private void saveBitmapToJpeg(Bitmap bitmap, String name) {

        //내부저장소 캐시 경로를 받아옵니다.
        File storage = getCacheDir();
        //저장할 파일 이름
        String fileName = name + ".jpg";
        //storage 에 파일 인스턴스를 생성합니다.
        File tempFile = new File(storage, fileName);
        try {
            // 자동으로 빈 파일을 생성합니다.
            tempFile.createNewFile();
            // 파일을 쓸 수 있는 스트림을 준비합니다.
            FileOutputStream out = new FileOutputStream(tempFile);
            // compress 함수를 사용해 스트림에 비트맵을 저장합니다.
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            // 스트림 사용후 닫아줍니다.
            out.close();
        } catch (FileNotFoundException e) {
            Log.e("MyTag","FileNotFoundException : " + e.getMessage());
        } catch (IOException e) {
            Log.e("MyTag","IOException : " + e.getMessage());
        }
    }
}