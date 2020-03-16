package com.example.qd.douyinwu.view.cropimageview;

import android.os.Build;
import android.support.annotation.NonNull;

public class CropImageFactory {

  public CropImage getCropImage(@NonNull CropImageView imageView) {
    final boolean preApi18 = Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2;
    return preApi18 ? new PreApi18CropImage(imageView) : new API18Image(imageView);
  }
}
