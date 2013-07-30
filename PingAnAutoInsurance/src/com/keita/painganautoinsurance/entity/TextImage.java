package com.keita.painganautoinsurance.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

public class TextImage implements Parcelable {


	//照片ID
	private String imageId;
	// 照片评论
	private String text;
	// 照片
	private Bitmap image;
	//照片路径
	private String imagePath;
	//构造函数
	public TextImage(){
		setText("尚未评论");
	}
	
	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public static final Creator<TextImage> CREATOR = new Creator<TextImage>() {

		@Override
		public TextImage createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			TextImage textImage = new TextImage();
			textImage.imageId = source.readString();
			textImage.image = Bitmap.CREATOR.createFromParcel(source);
			textImage.text = source.readString();
			textImage.imagePath = source.readString();
			return textImage;
		}

		@Override
		public TextImage[] newArray(int size) {
			// TODO Auto-generated method stub
			return new TextImage[size];
		}

	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel pracel, int flags) {
		// TODO Auto-generated method stub
		pracel.writeString(imageId);
		image.writeToParcel(pracel, 0);
		pracel.writeString(text);
		pracel.writeString(imagePath);

	}
	public void recycle(){
		if(image != null && !image.isRecycled())
			image.recycle();
	}

}
