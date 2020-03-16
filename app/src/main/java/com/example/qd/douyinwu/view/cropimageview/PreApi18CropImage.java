package com.example.qd.douyinwu.view.cropimageview;

import android.graphics.Matrix;

public class PreApi18CropImage extends CropImage {

  private Matrix matrix;

  PreApi18CropImage(CropImageView imageView) {
    super(imageView);

    init(imageView);
  }

  private void init(CropImageView imageView) {
    if (imageView.getCropType() != CropType.NONE) {
      matrix = new Matrix();
    }
  }

  @Override
  public Matrix getMatrix() {
    return matrix == null ? cropImageView.getImageMatrix() : matrix;
  }
}
