package com.keita.pinganautoinsurance.mywidget;

/*
 * 可编辑可选择的输入框
 */
import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class ComboEditText extends LinearLayout {
	private AutoCompleteTextView autoTextView;
	private ImageButton button;

	public ComboEditText(Context context) {
		super(context);
		this.createChildControls(context);
	}

	public ComboEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.createChildControls(context);
	}

	private void createChildControls(Context context) {
		this.setOrientation(HORIZONTAL);
		this.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		autoTextView = new AutoCompleteTextView(context);
		autoTextView.setSingleLine();
		autoTextView.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_NORMAL
				| InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
				| InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE
				| InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
		autoTextView.setRawInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		this.addView(autoTextView, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, 1));
		button = new ImageButton(context);
		button.setImageResource(android.R.drawable.arrow_down_float);
		button.setOnClickListener(new OnClickListener() {
		

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				autoTextView.showDropDown();
			}
		});
		this.addView(button, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
	}
	
	/* 设置数据源
	 */
	public void setAdapter(String[] column) {
		ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(
				this.getContext(), android.R.layout.simple_dropdown_item_1line,
				column);
		autoTextView.setAdapter(mAdapter);
	}

	/**
	 *
	 */
	public String getText() {
		return autoTextView.getText().toString();
	}

	/** * Sets the text in combo box. */
	public void setText(String text) {
		autoTextView.setText(text);
	}
}
