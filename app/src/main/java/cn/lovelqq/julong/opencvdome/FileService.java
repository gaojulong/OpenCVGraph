package cn.lovelqq.julong.opencvdome;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class FileService {
	private File files;
	protected File file = new File(Environment.getExternalStorageDirectory()
			+ "/print");
	private String name = Environment.getExternalStorageDirectory() + "/print/";

	// 保存文字
	public void saveToSDCard(String fileName, String content)
			throws IOException {
		// 考虑不同版本的sdCard目录不同，采用系统提供的API获取SD卡的目录
		// 一、当两个并发线程访问同一个对象object中的这个synchronized(this)同步代码块时，
		//一个时间内只能有一个线程得到执行。另一个线程必须等待当前线程执行完这个代码块以后才能执行该代码块。
		//二、然而，当一个线程访问object的一个synchronized(this)同步代码块时，
		//另一个线程仍然可以访问该object中的非synchronized(this)同步代码块。
		synchronized (this) {
			if (!file.isDirectory()) {
				file.mkdir();
			}
			files = new File(name, fileName);
		}

		if (!files.isDirectory()) {
			files.createNewFile();
		}
		FileOutputStream fileOutputStream = new FileOutputStream(files);
		fileOutputStream.write(content.getBytes());
		fileOutputStream.close();
	}

	// 读取文字
	public String read(String fileName) throws IOException {
		synchronized (this) {
			files = new File(name, fileName);
		}

		if (files.exists()) {
			FileInputStream fileInputStream = new FileInputStream(files);
			// 把每次读取的内容写入到内存中，然后从内存中获取
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			// 只要没读完，不断的读取
			while ((len = fileInputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, len);
			}
			// 得到内存中写入的所有数据
			byte[] data = outputStream.toByteArray();
			fileInputStream.close();
			return new String(data);
		} else
			return "";
	}

	// 保存图片
	public void savePhoto(Bitmap bitmap, String PhotoName) {
		synchronized (this)
		{
			if (!file.isDirectory()) {
				file.mkdir();
			}
			files = new File(name, PhotoName);
		}
		
		try {
			if (!files.isDirectory())
				files.createNewFile();

			FileOutputStream fos = new FileOutputStream(files);
			if (fos != null) {
				bitmap.compress(Bitmap.CompressFormat.PNG, 80, fos);
				fos.flush();
				fos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	// 读取图片
	public Bitmap readPhoto(String PhotoName) {
		Bitmap bitmap = BitmapFactory.decodeFile(name + PhotoName);
		return bitmap;
	}

	// 删除文件夹下所有文件
	public void deleteFile(File fileName) {
		File file;
		if (fileName.isDirectory()) {
			if (fileName.list().length > 0) {
				for (String name : fileName.list()) {
					file = new File(fileName, name);
					file.delete();
				}
			}
		}
	}

	// 保存多颜色分割图片
	public void ManysavePhoto(Bitmap bitmap, File fileName, String PhotoName) {
		synchronized (this) {
			if (!fileName.isDirectory()) {
				fileName.mkdir();
			}
			files = new File(fileName, PhotoName);
		}
		try {
			if (!files.isDirectory())
				files.createNewFile();

			FileOutputStream fos = new FileOutputStream(files);
			if (fos != null) {
				bitmap.compress(Bitmap.CompressFormat.PNG, 80, fos);
				fos.flush();
				fos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
