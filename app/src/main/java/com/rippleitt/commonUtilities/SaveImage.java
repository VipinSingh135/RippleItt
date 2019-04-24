package com.rippleitt.commonUtilities;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SaveImage {
	
	public static final int MEDIA_TYPE_IMAGE = 1;
	private static final String IMAGE_DIRECTORY_NAME = "Rippleit App";
	File f=null;
	Context context;
	Uri fileUri;
	//int REQUEST_CAMERA=12132;
	
	public SaveImage(Context context) {
		// TODO Auto-generated constructor stub
		this.context=context;
	}

	public Bitmap scaleImage(Context context, Uri photoUri, int maxH, int maxW) throws IOException {
		
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();

        int rotatedWidth, rotatedHeight;
        int orientation = getOrientation(context, photoUri);

        if (orientation == 90 || orientation == 270) {
            rotatedWidth = dbo.outHeight;
            rotatedHeight = dbo.outWidth;
        } else {
            rotatedWidth = dbo.outWidth;
            rotatedHeight = dbo.outHeight;
        }

        Bitmap srcBitmap;
        is = context.getContentResolver().openInputStream(photoUri);
        
        if (rotatedWidth > maxW || rotatedHeight > maxH) {
            float widthRatio = ((float) rotatedWidth) / ((float) maxW);
            float heightRatio = ((float) rotatedHeight) / ((float) maxH);
            float maxRatio = Math.max(widthRatio, heightRatio);

            // Create the bitmap from file
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) maxRatio;
            srcBitmap = BitmapFactory.decodeStream(is, null, options);
            
            
        } else {
            srcBitmap = BitmapFactory.decodeStream(is);
        }
        is.close();

        /*
         * if the orientation is not 0 (or -address, which means we don't know), we
         * have to do a rotation.
         */
        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
                    srcBitmap.getHeight(), matrix, true);
        }

        String type = context.getContentResolver().getType(photoUri);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (type.equals("image/png")) {
            srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        } else if (type.equals("image/jpg") || type.equals("image/jpeg")) {
            srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        }
        byte[] bMapArray = baos.toByteArray();
        baos.close();
        return BitmapFactory.decodeByteArray(bMapArray, 0, bMapArray.length);
    }

    public  int getOrientation(Context context, Uri photoUri) {
        /* it's on the external media. */
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }
    
    public File storeImage(Bitmap image) {
	      File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
	      
	     Log.e("NEW FILE", pictureFile.toString());
	      
	      f=new File(pictureFile.toString());
	      
	      try {
	          FileOutputStream fos = new FileOutputStream(pictureFile);
	          image.compress(Bitmap.CompressFormat.PNG, 100, fos);
	          fos.close();
	      } catch (FileNotFoundException e) {
	          Log.d("Error", "File not found: " + e.getMessage());
	      } catch (IOException e) {
	          Log.d("Error", "Error accessing file: " + e.getMessage());
	      }
		return pictureFile;  
	  }
    
    private File getOutputMediaFile(int type) {

		// External sdcard location
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),IMAGE_DIRECTORY_NAME);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
						+ IMAGE_DIRECTORY_NAME + " directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
		File mediaFile;
		
		if (type == MEDIA_TYPE_IMAGE) 
		{
			mediaFile = new File(mediaStorageDir.getPath() + File.separator+ "IMG_" + timeStamp + ".jpg");
		}
		else 
		{
			return null;
		}

		return mediaFile;
	}
	public Bitmap previewCapturedImagereg(Uri fileUri)
	{
		Bitmap bitmap=null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;

			f = new File(fileUri.getPath());
			Log.d("file", f.toString());
			final Bitmap bitmap1 = BitmapFactory.decodeFile(fileUri.getPath(),options);
			Log.e("BITMAP SIZE1", bitmap1.getHeight()+""+bitmap1.getWidth());
			int rotation = getCameraPhotoOrientation(context, fileUri,fileUri.getPath());

			Matrix matrix = new Matrix();
			matrix.postRotate(rotation);
			bitmap = Bitmap.createBitmap(bitmap1, 0, 0,bitmap1.getWidth(), bitmap1.getHeight(), matrix, true);
			bitmap=Bitmap.createScaledBitmap(bitmap,200,200,true);
			Log.e("BITMAP SIZE1", bitmap.getHeight()+""+bitmap.getWidth());

			/*Bitmap b=Bitmap.createScaledBitmap(bitmap, 250, 250, true);
			Bitmap bCircular=CommonUtils.getCircularBitmap(b);*/

			//Log.e("BITMAP SIZE1", b.getHeight()+""+b.getWidth());

			//storeImage(bitmap);

			//profile.setImageBitmap(bCircular);


		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
    
    public Bitmap previewCapturedImage(Uri fileUri)
	{
    	Bitmap bitmap=null;
		try {
			Log.e("file_NEW*********8", fileUri+"");
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;
			
			f = new File(fileUri.getPath());

			final Bitmap bitmap1 = BitmapFactory.decodeFile(fileUri.getPath(),options);
			Log.e("BITMAP SIZE1", bitmap1.getHeight()+""+bitmap1.getWidth());
			int rotation = getCameraPhotoOrientation(context, fileUri,fileUri.getPath());

			Matrix matrix = new Matrix();
			matrix.postRotate(rotation);
			bitmap = Bitmap.createBitmap(bitmap1, 0, 0,bitmap1.getWidth(), bitmap1.getHeight(), matrix, true);
			 bitmap=Bitmap.createScaledBitmap(bitmap,450,450,true);
			Log.e("BITMAP SIZE1", bitmap.getHeight()+""+bitmap.getWidth());
			
			/*Bitmap b=Bitmap.createScaledBitmap(bitmap, 250, 250, true);
			Bitmap bCircular=CommonUtils.getCircularBitmap(b);*/
			
			//Log.e("BITMAP SIZE1", b.getHeight()+""+b.getWidth());
			
			//storeImage(bitmap);
			
			//profile.setImageBitmap(bCircular);


		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	
	public int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath) {
		int rotate = 0;
		try {
			context.getContentResolver().notifyChange(imageUri, null);
			File imageFile = new File(imagePath);

			ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			}

			Log.i("RotateImage", "Exif orientation: " + orientation);
			Log.i("RotateImage", "Rotate value: " + rotate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rotate;
	}
	
	
}
