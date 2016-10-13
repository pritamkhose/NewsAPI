package com.pritam.newsapi.utility;

import android.app.Application;

import java.io.File;

/**
 * Created by Pritam on 10/13/2016.
 */
public class AppConstant extends Application {

    private boolean compressImage;

    public boolean isCompressImage() {
        return compressImage;
    }

    public void setCompressImage(boolean compressImage) {
        this.compressImage = compressImage;
    }



}
