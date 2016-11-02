package com.mdxx.qmmz.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.util.Log;

public class BitMapUtils {
	/**
	 * ͼƬ���ѹ��?
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap comp(Bitmap image, float width, float height) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {// �ж����ͼƬ����?M,����ѹ�����������ͼƬ��BitmapFactory.decodeStream��ʱ���?
			baos.reset();// ����baos�����baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// ����ѹ��50%����ѹ�������ݴ�ŵ�baos��
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// ��ʼ����ͼƬ����ʱ��options.inJustDecodeBounds ���true��
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// ���������ֻ�Ƚ϶���?00*480�ֱ��ʣ����ԸߺͿ���������Ϊ
		float hh = height;// �������ø߶�Ϊ800f
		float ww = width;// �������ÿ���?80f
		// ���űȡ������ǹ̶��������ţ�ֻ�ø߻��߿�����һ����ݽ��м��㼴��?
		int be = 1;// be=1��ʾ������
		if (w > h && w > ww) {// ����ȴ�Ļ���ݿ�ȹ̶���С����
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// ���߶ȸߵĻ���ݿ�ȹ̶���С����
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// �������ű���
		// ���¶���ͼƬ��ע���ʱ�Ѿ���options.inJustDecodeBounds ���false��
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);// ѹ���ñ����С���ٽ�������ѹ��?
	}

	/**
	 * ͼƬ����ѹ��
	 * 
	 * @param image
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// ����ѹ������������100��ʾ��ѹ������ѹ�������ݴ�ŵ�baos��
		int options = 100;
		while (baos.toByteArray().length / 1024 > 1024) { // ѭ���ж����ѹ����ͼƬ�Ƿ����100kb,���ڼ���ѹ��
			baos.reset();// ����baos�����baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// ����ѹ��options%����ѹ�������ݴ�ŵ�baos��
			options -= 4;// ÿ�ζ�����10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// ��ѹ��������baos��ŵ�ByteArrayInputStream��
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// ��ByteArrayInputStream������ͼƬ
		return bitmap;
	}

	public static void compressBmpToFile(Bitmap bmp, String filepath) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 100;// ����ϲ����80��ʼ,

		bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
		Log.d("pic_length", baos.toByteArray().length / 1024 + "");
		while (baos.toByteArray().length / 1024 > 1024) {
			baos.reset();
			options -= 4;
			bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
		}
		Log.d("pic_length", baos.toByteArray().length / 1024 + "");
		try {
			File file = new File(filepath);
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baos.toByteArray());
			fos.flush();
			fos.close();
			if (!bmp.isRecycled()) {
				bmp.recycle(); // ����ͼƬ��ռ���ڴ�
				System.gc(); // ����ϵͳ��ʱ����
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Bitmapת��File����
	 * 
	 * @param bitmap
	 * @param filepath
	 */
	public static void saveBitmapFile(Bitmap bitmap, String filepath) {
		File file = new File(filepath);// ��Ҫ����ͼƬ��·��
		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ͼƬ�������Сѹ������?
	 * 
	 * @param srcPath
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap getimage(String srcPath, float width, float height) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// ��ʼ����ͼƬ����ʱ��options.inJustDecodeBounds ���true��
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// ��ʱ����bmΪ��
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = height;// �������ø߶�Ϊ800f
		float ww = width;// �������ÿ���?80f
		// ���űȡ������ǹ̶��������ţ�ֻ�ø߻��߿�����һ����ݽ��м��㼴��?
		int be = 1;// be=1��ʾ������
		if (w > h && w > ww) {// ����ȴ�Ļ���ݿ�ȹ̶���С����
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// ���߶ȸߵĻ���ݿ�ȹ̶���С����
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// �������ű���
		// ���¶���ͼƬ��ע���ʱ�Ѿ���options.inJustDecodeBounds ���false��
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap);// ѹ���ñ����С���ٽ�������ѹ��?
	}
	
	/**
	 * ͼƬ�������Сѹ������?
	 * 
	 * @param srcPath
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap getBitmapFromSources( Context context,int resid, float width, float height) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// ��ʼ����ͼƬ����ʱ��options.inJustDecodeBounds ���true��
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resid);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = height;// �������ø߶�Ϊ800f
		float ww = width;// �������ÿ���?80f
		// ���űȡ������ǹ̶��������ţ�ֻ�ø߻��߿�����һ����ݽ��м��㼴��?
		int be = 1;// be=1��ʾ������
		if (w > h && w > ww) {// ����ȴ�Ļ���ݿ�ȹ̶���С����
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// ���߶ȸߵĻ���ݿ�ȹ̶���С����
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// �������ű���
		// ���¶���ͼƬ��ע���ʱ�Ѿ���options.inJustDecodeBounds ���false��
		bitmap = BitmapFactory.decodeResource(context.getResources(), resid);
		return compressImage(bitmap);// ѹ���ñ����С���ٽ�������ѹ��?
	}

	/**
	 * ɾ���ļ������ļ���
	 * 
	 * @param file
	 */
	public void deleteFile(File file) {
		if (file.exists()) { // �ж��ļ��Ƿ����?
			if (file.isFile()) { // �ж��Ƿ����ļ�
				file.delete(); // delete()���� ��Ӧ��֪�� ��ɾ������?
			} else if (file.isDirectory()) { // �����������һ��Ŀ�?
				File files[] = file.listFiles(); // ����Ŀ¼�����е��ļ� files[];
				for (int i = 0; i < files.length; i++) { // ����Ŀ¼�����е��ļ�
					this.deleteFile(files[i]); // ��ÿ���ļ� ������������е��
				}
			}
			file.delete();
		} else {
			Log.e("error", "�ļ�������");
		}
	}

	public static int getExifOrientation(String filepath) {
		int degree = 0;
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(filepath);
		} catch (IOException ex) {
			Log.e("test", "cannot read exif", ex);
		}
		if (exif != null) {
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION, -1);
			if (orientation != -1) {
				switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
				}
			}
		}
		return degree;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * ͼƬ���?
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap grey(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Bitmap faceIconGreyBitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(faceIconGreyBitmap);
		Paint paint = new Paint();
		ColorMatrix colorMatrix = new ColorMatrix();
		colorMatrix.setSaturation(0);
		ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(
				colorMatrix);
		paint.setColorFilter(colorMatrixFilter);
		canvas.drawBitmap(bitmap, 0, 0, paint);
		if (bitmap != null && !bitmap.isRecycled() && faceIconGreyBitmap != bitmap) {  
            bitmap.recycle();  
        }  
		return faceIconGreyBitmap;
	}
	
	
	/**
	 * resIdתBitmap
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inSampleSize = 1;
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// ��ȡ��ԴͼƬ
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

}
