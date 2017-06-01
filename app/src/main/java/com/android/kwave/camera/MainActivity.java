package com.android.kwave.camera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final int REQ_PERMISSION = 100;
    Button gallery, camera;
    ImageView luncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gallery = (Button) findViewById(R.id.gallery);
        camera = (Button) findViewById(R.id.camera);
       luncher = (ImageView) findViewById(R.id.image);

        // 버튼 잠금
        camera.setEnabled(false);
        gallery.setEnabled(false);
        // api level이 23이상일 경우만 실행
        // 마시멜로 이상인 경우만 런타임 체크
        // Build.VERSION.SDK_INT = 설치 안드로이드폰의 api level 가져오기
        // VERSION_CODES 아래에 상수로 각 버전별 api level이 작성되어 있다.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkPermission();
        }
        else{
            // 아니면 init
            init();
        }

    }

    private void checkPermission(){
        // 1. 권한체크 - 특정 권한이 있는지 시스템에 물어본다.
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            init();
        }
        else {
            // 2. 권한이 없으면 사용자에 권한을 달라고 요청
            String pemission[] = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
            requestPermissions(pemission, REQ_PERMISSION); // -> 권한을 요구하는 팝업이 사용자 화면에 노출된다.
        }
    }

    // 3. 사용자가 권한체크 팝업에서 권한을 승인 또는 거절하면 아래 메서드가 호출된다.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQ_PERMISSION){
            // 3.1 사용자가 승인을 했음
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED){
                init();
            }
            // 3.2 사용자가 거절했음
            else{
                Toast.makeText(this,"권한요청을 승인하셔야 앱을 사용할 수 있습니다.",Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void init(){      // 권한 승인 후 실행
        camera.setEnabled(true);
        gallery.setEnabled(true);
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.gallery :
                 intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                 startActivityForResult(Intent.createChooser(intent,"Select Photo"),REQ_PERMISSION);
                break;
            case R.id.camera :
                intent = new Intent(Intent.ACTION_CAMERA_BUTTON, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent,"Camera"),REQ_PERMISSION);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case 100 :
                    Uri imageUri = data.getData();
                    luncher.setImageURI(imageUri);
            }
        }
    }
}
