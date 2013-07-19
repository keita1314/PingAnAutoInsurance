package com.keita.painganautoinsurance.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
public class TextImage {
	private String text;
	private Bitmap image;
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
	
}
