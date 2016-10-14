package com.kk.zhbj.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 注释： 模仿 xutil 的 BitmapUtil <br>
 * 作者： kk <br>
 * QQ  : <br>
 * 创建时间：2016/10/14 - 13:29 <br>
 */

public class MyBitmapUtil {
    private static final String TAG = "main";
    Activity mActivity;
    LruCache<String, Bitmap> mLruCache;

    public MyBitmapUtil(Activity mActivity) {
        this.mActivity = mActivity;
        // 设置最大的 缓存大小为  虚拟运行时的最大内存
        mLruCache = new LruCache<>((int) (Runtime.getRuntime().totalMemory() / 8));


    }

    public void display(ImageView view, String imgUrl) {
        Log.e(TAG, "MyBitmapUtil: totalMemory " + Formatter.formatFileSize(mActivity, Runtime.getRuntime().totalMemory()));
        Log.e(TAG, "MyBitmapUtil: freeMemory " + Formatter.formatFileSize(mActivity, Runtime.getRuntime().freeMemory()));
        Log.e(TAG, "MyBitmapUtil: maxMemory " + Formatter.formatFileSize(mActivity, Runtime.getRuntime().maxMemory()));
        if (displayFromMemoryCache(view, imgUrl)) {
            Log.i(TAG, "MyBitmapUtil.display: 从Memory中读取图片");
            return;
        } else if (displayFromDiskCache(view, imgUrl)) {
            Log.i(TAG, "MyBitmapUtil.display: 从disk中读取图片");

            return;
        } else {
            disPlayFromInternet(view, imgUrl);
            Log.i(TAG, "MyBitmapUtil.display: 从网络读取图片");
        }


    }

    // 此方法在UI线程中运行
    private void disPlayFromInternet(final ImageView view, final String imgUrl) {
        AsyncTask task = new AsyncTask<Object, Integer, Bitmap>() {
            @Override
            // This method can call publishProgress to publish updates on the UI thread.
            //  The specified parameters are the parameters passed to execute by the caller of this task.
            protected Bitmap doInBackground(Object... params) {
                Log.i(TAG, "MyBitmapUtil.doInBackground: 再后台执行....");
                Bitmap bitmap = null;
                try {
                    bitmap = donwLoadBitmap((String) params[1]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 写入disk缓存 ， 内存缓存
                try {
                    boolean successful = writeDiskCache(bitmap, imgUrl);
                    boolean successfulm = writMemCache(bitmap, imgUrl);
                    if (successful) {
                        Log.i(TAG, "MyBitmapUtil.doInBackground: 成功写入disk缓存 ");
                    } else {
                        Log.i(TAG, "MyBitmapUtil.doInBackground: 失败写入disk缓存 ");
                    }
                    if (successfulm) {
                        Log.i(TAG, "MyBitmapUtil.doInBackground: 成功写入Memory缓存 ");
                    } else {
                        Log.i(TAG, "MyBitmapUtil.doInBackground: 失败写入Memory缓存 ");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return bitmap;
            }

            @Override
            //Runs on the UI thread before doInBackground.
            protected void onPreExecute() {
                Log.i(TAG, "MyBitmapUtil.onPreExecute:执行前  " + imgUrl);
                // 让 View 和 URl 绑定，因为ListView 可能重用对象
                view.setTag(imgUrl);
                super.onPreExecute();
            }

            @Override
            // Runs on the UI thread after publishProgress is invoked. The specified values are the values passed to publishProgress.
            protected void onProgressUpdate(Integer... values) {
                Log.i(TAG, "MyBitmapUtil.onProgressUpdate: 执行进度更新");
                super.onProgressUpdate(values);
            }

            @Override
            // Runs on the UI thread after doInBackground. The specified result is the value returned by doInBackground.
            protected void onPostExecute(Bitmap bitmap) {
                Log.i(TAG, "MyBitmapUtil.onPostExecute: 执行后");
                // 设置 bitmap 图片
                if (view.getTag().equals(imgUrl)) {
                    view.setImageBitmap(bitmap);
                }

                super.onPostExecute(bitmap);
            }
        };
        // 执行任务
        task.execute(view, imgUrl);
    }

    /**
     * 下载图片
     *
     * @param url 图片的URl
     * @return bitmap 图片
     * @throws Exception
     */
    private Bitmap donwLoadBitmap(String url) throws Exception {
        Bitmap bitmap = null;
        HttpURLConnection conn;
        InputStream inputStream = null;
        URL url1 = new URL(url);
        conn = (HttpURLConnection) url1.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);// 连接超时
        conn.setReadTimeout(5000);// 读取超时
        conn.connect();
        if (conn.getResponseCode() == 200) {
            inputStream = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        }
        if (inputStream != null) {
            inputStream.close();
        }
        conn.disconnect();
        return bitmap;
    }

    /**
     * @param bitmap 写入缓存的图片
     * @param url    图片的url
     * @return 是否写入缓存成功
     * @throws Exception
     */
    private boolean writeDiskCache(Bitmap bitmap, String url) throws Exception {
        File photoCacheDir = new File(mActivity.getExternalCacheDir(), "photoCache");
        if (!photoCacheDir.exists()) {
            photoCacheDir.mkdir();
        }
        String md5Url = MD5Util.encode(url);
        File outFile = new File(photoCacheDir, md5Url);
        return bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(outFile));

    }

    /**
     * @param view 图片ImageView
     * @param url  图片的Url
     * @return 是否设置图片成功
     */
    private boolean displayFromDiskCache(ImageView view, String url) {
        File photoCacheDir = new File(mActivity.getExternalCacheDir(), "photoCache");
        String md5Url = null;
        try {
            md5Url = MD5Util.encode(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        File cache = new File(photoCacheDir, md5Url);
        Bitmap bitmap = BitmapFactory.decodeFile(cache.getAbsolutePath());
        if (bitmap != null) {
            view.setImageBitmap(bitmap);
            return true;
        }
        return false;
    }

    /**
     * @param bitmap 要写入内存的图片
     * @param url    图片的内存
     * @return 是否成功写入缓存
     */
    private boolean writMemCache(Bitmap bitmap, String url) {
        String md5Url = null;
        try {
            md5Url = MD5Util.encode(url);
            mLruCache.put(md5Url, bitmap);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param view Imageview
     * @param url  图片的URL
     * @return 是否成功从内存设置图片
     */
    private boolean displayFromMemoryCache(ImageView view, String url) {
        String md5Url = null;
        try {
            md5Url = MD5Util.encode(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bitmap bitmap = mLruCache.get(md5Url);
        if (bitmap != null) {
            view.setImageBitmap(bitmap);
            return true;
        }
        return false;
    }
}
