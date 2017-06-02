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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
   private final int REQ_PERMISSION = 100;      // 갤러리가 호출했음을 알려주는 키값

    Button btnGallery, btnCamera;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 위젯과 연결
        btnGallery = (Button) findViewById(R.id.btnGallery);
        btnCamera = (Button) findViewById(R.id.btnCamera);
       imageView = (ImageView) findViewById(R.id.imageView);

        // 리스너 연결
        btnCamera.setOnClickListener(this);
        btnGallery.setOnClickListener(this);

        // 버튼 잠금
        btnCamera.setEnabled(false);    // 초기에 버튼 사용을 막고 권한이 생기면 Enable 시켜준다.
        btnCamera.setEnabled(false);    // 초기에 버튼 사용을 막고 권한이 생기면 Enable 시켜준다.


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

    private void checkPermission(){             // 권한 체크
        // 1. 권한체크 - 특정 권한이 있는지 시스템에 물어본다.              WRITE_EXTERNAL_STORAGE : 외부저장소에 쓰기
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED &&
          //시스템이 스스로 (Mani...)에 권한이 있는지 검사              // 패키지 매니저가 승인이 되었는지 확인한다.
                checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            init();
        }
        else {
            // 2. 권한이 없으면 사용자에 권한을 달라고 요청
            String pemission[] = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
                                  // 사용할 권한을 미리 배열로 넣어둠
            requestPermissions(pemission, REQ_PERMISSION); // -> 권한을 요구하는 팝업이 사용자 화면에 노출된다.
        }
    }

    // 3. 사용자가 권한체크 팝업에서 권한을 승인 또는 거절하면 아래 메서드가 호출된다.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQ_PERMISSION){      // 갤러리에서 승인 요청 했음
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
//        else if(requestCode == REQ1_PERMISSION){      // 카메라에서 승인 요청 했음
//            // 3.1 사용자가 승인을 했음
//            if(grantResults[0] == PackageManager.PERMISSION_GRANTED &&
//                    grantResults[1] == PackageManager.PERMISSION_GRANTED){
//                init();
//            }
//            // 3.2 사용자가 거절했음
//            else{
//                Toast.makeText(this,"권한요청을 승인하셔야 앱을 사용할 수 있습니다.",Toast.LENGTH_SHORT).show();
//            }
//        }

    }

    public void init(){      // 권한 승인 후 실행
        btnCamera.setEnabled(true);
        btnGallery.setEnabled(true);
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.btnGallery :
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                 startActivityForResult(Intent.createChooser(intent,"사용할 앱을 선택하세요"),REQ_PERMISSION);
                                                        // 사용할 갤러리를 선택하는 창의 제목 // 호출한 것을 알려주는 플래그 값
                break;
            case R.id.btnCamera :
                intent = new Intent(getBaseContext(),CameraActivity.class);
                startActivity(intent);
                break;
        }
    }


    // 결과가 넘어온 후 닫는 함수
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri imageUri = null;
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case REQ_PERMISSION :
                    imageUri = data.getData();      // 갤러리에서 넘겨준 이미지를 받아서 이미지뷰에 넣어줌 - 이미지뷰를 세팅해줌
                    Log.i("Galery","imageUri=============================="+imageUri.getPath());
                    imageView.setImageURI(imageUri);
                    break;
            }
        }
    }
}
