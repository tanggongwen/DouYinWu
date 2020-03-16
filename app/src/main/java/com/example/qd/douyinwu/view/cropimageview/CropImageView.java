/*
 * Copyright 2015 Cesar Diez Sanchez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.qd.douyinwu.view.cropimageview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.qd.douyinwu.R;


public class CropImageView extends ImageView {

  private CropImage cropImage;
  private int cropType = CropType.LEFT_CENTER;
  private String url;

  public CropImageView(Context context) {
    super(context);

    initImageView();
  }

  public CropImageView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CropImageView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

    parseAttributes(attrs);
    initImageView();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public CropImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);

    parseAttributes(attrs);
    initImageView();
  }

  public void setCropType(@CropType int cropType) {
    this.cropType = cropType;
    setWillNotCacheDrawing(false);

    requestLayout();
    invalidate();
  }

  public
  @CropType
  int getCropType() {
    return cropType;
  }

  @Override
  protected boolean setFrame(int l, int t, int r, int b) {
    final boolean changed = super.setFrame(l, t, r, b);
    if (!isInEditMode() && cropImage != null && getDrawable() != null) {
      cropImage.computeImageTransformation();
    }
    return changed;
  }

  @Override
  public void setImageBitmap(Bitmap bm) {
    super.setImageBitmap(bm);
    initImageView();
  }

  private void parseAttributes(AttributeSet attrs) {
    final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.civ_CropImageView);
    cropType = a.getInt(R.styleable.civ_CropImageView_civ_crop, CropType.NONE);
    a.recycle();
  }

  @Override
  public void setImageDrawable(Drawable drawable) {
    super.setImageDrawable(drawable);
    initImageView();
  }

  @Override
  public void setImageResource(int resId) {
    super.setImageResource(resId);
    initImageView();
  }

  private void initImageView() {
    setScaleType(ScaleType.MATRIX);

    if (getDrawable() != null) {
      cropImage = new CropImageFactory().getCropImage(this);
    }
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }

}
