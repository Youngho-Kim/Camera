# Camera

###갤러리에서 사진 가져오기  
[전체코드보기](https://github.com/Youngho-Kim/Camera/blob/master/app/src/main/java/com/android/kwave/camera/MainActivity.java)
1. 권한체크 - 특정 권한이 있는지 시스템에 물어본다.   
 ```java
   if(checkSelfPermission(Manifest.permission.승인받을 권한 ) == PackageManager.PERMISSION_GRANTED}
   ````
2. 권한이 없으면 사용자에 권한을 달라고 요청
```java
requestPermissions(승인받을 권한, 호출한 것을 알려주는 플래그 값);
```
3. 사용자가 권한체크 팝업에서 권한을 승인 또는 거절하면 아래 메서드가 호출된다.
```java
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {   }
    // 권한 승인 요청에 관한 결과로 인해 실행됨

if(requestCode == 권한 요청한 플래그 값){  
//3.1 사용자가 승인을 했음   - 다음 작업 진행
//3.2 사용자가 거절했음 - 거절했으므로 작업 실행X
```
4. 권한 승인 후 실행
```java
    @Override
        public void onClick(View v) {
            Intent intent = null;
                    intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                   // 인텐트가 고르는 행위를 함     // 갤러리의 uri는 위와 같음
                     startActivityForResult(Intent.createChooser(intent,"사용할 앱을 선택하세요"),REQ_PERMISSION);
                     break;
```                     
5. 결과가 넘어온 후 닫는 함수
```java
@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri imageUri = null;                 
        if(resultCode == RESULT_OK){         // 결과가 OK라면 
            switch (requestCode){           // 권한을 요청한 리퀘스트 코드
                case REQ_PERMISSION :       // 호출한 것을 알려주는 플래그 값     
                    imageUri = data.getData();      // 갤러리에서 넘겨준 이미지를 받아서 이미지뷰에 넣어줌 - 이미지뷰를 세팅해줌
                    imageView.setImageURI(imageUri);
                    break;
                }
            }
        }
    }
```    

  
### 카메라 어플 만들기  
[전체코드보기](https://github.com/Youngho-Kim/Camera/blob/master/app/src/main/java/com/android/kwave/camera/CameraActivity.java)
사진을 저장하기 위한 파일에 대한 권한을 획득하기 위한 설정
Manifest에 아래 코드 넣기

        <provider       
                     android:name="android.support.v4.content.FileProvider"
                     android:authorities="${applicationId}.provider"
                     android:exported="false"
                     android:grantUriPermissions="true">
                     <meta-data
                         android:name="android.support.FILE_PROVIDER_PATHS"
                         android:resource="@xml/file_path"/>
         </provider>
