package yu.cleaner.util;


import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import yu.cleaner.R;

//可以存储网络或者是本地图片
public class ImageLoaderUtil {

	//展示图片
	private static DisplayImageOptions options = new DisplayImageOptions.Builder()
			// 下载过程中显示的图片
			.showImageOnLoading(R.mipmap.xsearch_loading)
					// 下载失败时显示的图片
			.showImageOnFail(R.mipmap.head)
					// uri为空的时候显示的图片
			.showImageForEmptyUri(R.mipmap.head)//前三个必写
					// 是否进行内存缓存
			.cacheInMemory(true)
					// 磁盘缓存
			.cacheOnDisk(true)
					// 显示清晰度方式
			.bitmapConfig(Bitmap.Config.RGB_565)
					//
			.resetViewBeforeLoading(true)
			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 淡入淡出效果
			.displayer(new FadeInBitmapDisplayer(200))
					// RoundedBitmapDisplayer
			.build();//必须写

	// file://
	public static void display(String url, ImageView imageView) {
		ImageLoader.getInstance().displayImage(url, imageView, options);
	}


}
