package com.example.qd.douyinwu.view.cropimageview;

import android.graphics.Matrix;

public class API18Image extends CropImage {

  API18Image(CropImageView imageView) {
    super(imageView);
  }

  @Override
  public Matrix getMatrix() {
    return cropImageView.getImageMatrix();
  }
}