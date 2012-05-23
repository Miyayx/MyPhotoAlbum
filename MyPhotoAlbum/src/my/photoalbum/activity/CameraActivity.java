package my.photoalbum.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

public class CameraActivity extends Activity{
	private static final int PHOTO_PICKED_WITH_DATA = 1;
    private static final int CAMERA_WITH_DATA = 2;
    private ImageView lblImage = null;
    
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_WITH_DATA);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if(resultCode!=RESULT_OK)
            return;
        switch(requestCode){
        case CAMERA_WITH_DATA:
            final Bitmap photo = data.getParcelableExtra("data");
            FileOutputStream fos = null;
            File file = new File("D:/mysdcard/myImage/");
            file.mkdir();
            String fileName = "D:/mysdcard/myImage/11.jpg";
            try{
            	fos = new FileOutputStream(fileName);
            	photo.compress(Bitmap.CompressFormat.JPEG, 100, fos);  
            }catch(FileNotFoundException e){
            	e.printStackTrace();
            }finally{
            	try{
            		fos.flush();
            		fos.close();
            	}catch(IOException e){
            		e.printStackTrace();
            	}
            }
            if(photo!=null){
                doCropPhoto(photo);
            }
        case PHOTO_PICKED_WITH_DATA:
            Bitmap photo1 = data.getParcelableExtra("data");
            if(photo1!=null){
                lblImage.setImageBitmap(photo1);
            }
           
        }
    }
   
    protected void doCropPhoto(Bitmap data){
        Intent intent = getCropImageIntent(data);
        startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
    }
   
    public static Intent getCropImageIntent(Bitmap data) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        intent.putExtra("data", data);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 128);
        intent.putExtra("outputY", 128);
        intent.putExtra("return-data", true);
        return intent;
    }
}

