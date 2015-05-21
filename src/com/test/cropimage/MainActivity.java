package com.test.cropimage;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final int PHOTO_HRAPH = 1; // ����
	public static final int PHOTO_ZOOM = 2; // ����
	public static final int PHOTO_RESOULT = 3; // ���

	public static final String IMAGE_UNSPECIFIED = "image/*"; // ͼƬ��MIME����

	private String filePath = "";
	private String fileName = "/temp.jpg";
	ImageView imageView = null;
	Button button0 = null;
	Button button1 = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		imageView = (ImageView) findViewById(R.id.imageID);
		button0 = (Button) findViewById(R.id.btn_01);
		button1 = (Button) findViewById(R.id.btn_02);

		button0.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// ��ͼƬ��
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(Media.EXTERNAL_CONTENT_URI,
						IMAGE_UNSPECIFIED);
				startActivityForResult(intent, PHOTO_ZOOM);
			}
		});

		button1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					filePath = Environment.getExternalStorageDirectory()
							.getPath() + fileName;
					// �������
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					// ������Ƭ����·��
					intent.putExtra(MediaStore.EXTRA_OUTPUT,
							Uri.fromFile(new File(filePath)));
					startActivityForResult(intent, PHOTO_HRAPH);
				} else {
					Toast.makeText(MainActivity.this, "�洢�������ã�������ѡȡ", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PHOTO_HRAPH) { // ����
			startPhotoZoom(Uri.fromFile(new File(filePath)));
		} else if (data != null) {
			if (requestCode == PHOTO_ZOOM) { // ��ȡ�������ͼƬ
				startPhotoZoom(data.getData());
			} else if (requestCode == PHOTO_RESOULT) { // �������Ž��
				Bundle extras = data.getExtras();
				if (extras != null) {
					Bitmap photo = extras.getParcelable("data");
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					// ѹ���ļ�
					photo.compress(CompressFormat.JPEG, 75, stream);
					imageView.setImageBitmap(photo);
					// ɾ����ʱ�ļ�
					new File(filePath).delete();
				}

			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * ����ϵͳ���г������ͼƬ
	 * 
	 * @param uri
	 *            ͼƬ��Uri
	 */
	private void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		// aspectX aspectY �ǿ�ߵı���
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY �ǲü�ͼƬ���
		intent.putExtra("outputX", 340);
		intent.putExtra("outputY", 340);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PHOTO_RESOULT);
	}

}