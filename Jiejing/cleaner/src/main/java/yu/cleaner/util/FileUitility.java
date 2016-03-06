package yu.cleaner.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class FileUitility {

	private static String ROOT_CACHE;
	// 在磁盘下创建图片缓存路径
	private static String ROOT_DIR = "fly";
	public final static String USER_HAED = "item";
	private static FileUitility instance = null;

	private FileUitility() {
	}

	// Environment.getExternalStorageDirectory()系统默认的存储根目录，sd卡或者手机内存
	public static FileUitility getInstance(Context context) {
		if (instance == null) {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				ROOT_CACHE = (Environment.getExternalStorageDirectory() + "/"
						+ ROOT_DIR + "/");
			} else {
				ROOT_CACHE = (context.getFilesDir().getAbsolutePath() + "/"
						+ ROOT_DIR + "/");
			}
			File dir = new File(ROOT_CACHE);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			instance = new FileUitility();
		}
		return instance;
	}
	//创建一个文件夹
	public File makeDir(String dir) {
		File fileDir = new File(ROOT_CACHE + dir);
		if (fileDir.exists()) {
			return fileDir;
		} else {
			fileDir.mkdirs();
			return fileDir;
		}
	}

}
