package com.wkz.framework.utils;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import androidx.fragment.app.FragmentActivity;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wkz.framework.constants.FRFilesDirectory;
import com.wkz.utils.MD5Utils;
import com.wkz.utils.ToastUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.concurrent.ExecutionException;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Glide下载图片工具类
 * @author wkz
 */
public class GlideDownloadImageUtils {

    /**
     * 保存图片到本地相册
     */
    public static void savePictureToLocalAlbum(FragmentActivity context, final String url) {
        Observable
                .create((ObservableOnSubscribe<String>) emitter -> {
                    emitter.onNext(url);
                    emitter.onComplete();
                })
                .compose(new RxPermissions(context).ensure(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                .observeOn(Schedulers.io())
                .filter(aBoolean -> aBoolean)
                .flatMap((Function<Boolean, Observable<File>>) aBoolean -> {
                    File file = null;
                    try {
                        file = Glide.with(context)
                                .load(url)
                                .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .get();
                    } catch (InterruptedException | ExecutionException e) {
                        Logger.e(e.toString());
                        ToastUtils.showShort("图片下载失败！");
                    }
                    return Observable.just(file);
                })
                .observeOn(Schedulers.io())
                .flatMap((Function<File, Observable<Uri>>) file -> {
                    File mFile = null;
                    try {
                        File dir = FRFilesDirectory.DIR_DOWNLOAD_IMAGES;
                        if (!dir.exists()) {
                            //noinspection ResultOfMethodCallIgnored
                            dir.mkdirs();
                        }

                        //图片名称
                        String fileName;
                        //后缀
                        String postfix;
                        if (url.contains("/") && url.contains(".")) {
                            postfix = url.substring(url.lastIndexOf(".")).toUpperCase();
                            fileName = MD5Utils.getMD5(url.substring(url.lastIndexOf("/"), url.lastIndexOf("."))).toUpperCase() + postfix;
                        } else {
                            postfix = ".JPG";
                            fileName = MD5Utils.generateMD5Str().toUpperCase() + postfix;
                        }
                        mFile = new File(dir, fileName);
                        if (mFile.exists()) {
                            return Observable.just(Uri.fromFile(mFile));
                        }

                        FileInputStream fis = new FileInputStream(file.getAbsolutePath());

                        int byteRead;
                        byte[] buf = new byte[1024];

                        FileOutputStream fos = new FileOutputStream(mFile.getAbsolutePath());
                        while ((byteRead = fis.read(buf)) != -1) {
                            fos.write(buf, 0, byteRead);
                        }
                        fos.close();
                        fis.close();
                    } catch (Exception e) {
                        Logger.e(e.toString());
                        ToastUtils.showShort("图片保存失败！");
                    }
                    //更新本地图库
                    Uri uri = Uri.fromFile(mFile);
                    Intent mIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                    context.sendBroadcast(mIntent);

                    return Observable.just(uri);
                })
                .subscribeOn(Schedulers.io())
                .map(uri -> {
                    if (uri != null && !TextUtils.isEmpty(uri.getPath())) {
                        return String.format("图片已保存至 %s 文件夹！", FRFilesDirectory.DIR_DOWNLOAD_IMAGES.getAbsolutePath());
                    } else {
                        return "图片保存失败！";
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .retry()
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable s) {

                    }

                    @Override
                    public void onNext(String s) {
                        ToastUtils.showShort(s);
                    }

                    @Override
                    public void onError(Throwable t) {
                        ToastUtils.showShort("图片保存失败！");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
