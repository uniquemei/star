package yu.cleaner.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import yu.cleaner.entity.Cleaner;

/**
 * Created by Administrator on 2015/10/5.
 */
public class MainApplication extends Application {
    private RequestQueue queue;    //定义一个静态常量，可以CCAplication.getInstance().getApplication()直接访问
    private static MainApplication application;

    private ImageLoaderConfiguration imgConfig;

    @Override
    public void onCreate() {
        super.onCreate();
        this.queue = Volley.newRequestQueue(this);
        application = this;
        //缓存图片
        imgConfig = new ImageLoaderConfiguration.Builder(this)
                //不要缓存不同尺寸
                .denyCacheImageMultipleSizesInMemory()
                        //下载图片线程的优先级
                .threadPriority(Thread.NORM_PRIORITY - 2)
                        //设置下载线程的执行器（线程池）
                .taskExecutor(ExecutorManager.getInstance().getExecutors())
                        //设置内存缓存的大小
                .memoryCacheSize((int) Runtime.getRuntime().maxMemory() / 8)
                        //设置磁盘缓存大小
                .diskCacheSize(50 * 1024 * 1024)
                        //设置磁盘缓存文件的命名生成器（文件命名的规则）
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                        //设置磁盘缓存的路径
                .discCache(new UnlimitedDiskCache(FileUitility.getInstance(this).makeDir("imgCache")))
                        //
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                        //设置具体的图片加载器：
                .imageDownloader(new BaseImageDownloader(this, 60 * 1000, 60 * 1000))
                        //生成配置信息
                .build();
        ImageLoader.getInstance().init(imgConfig);
    }

    public RequestQueue getQueue() {
        return queue;
    }

    public static MainApplication getApplication() {
        return application;
    }

}