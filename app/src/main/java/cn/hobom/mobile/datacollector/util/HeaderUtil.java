package cn.hobom.mobile.datacollector.util;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import cn.hobom.mobile.datacollector.R;


public class HeaderUtil {

	public static void show(final Activity context, boolean showBackBtn,
			boolean showLeftTitle, boolean showCenterTitle, boolean showRightBtn) {
		context.findViewById(R.id.back).setVisibility(showBackBtn ? View.VISIBLE : View.GONE);
		context.findViewById(R.id.lefttitle).setVisibility(showLeftTitle ? View.VISIBLE : View.GONE);
		context.findViewById(R.id.centertitle).setVisibility(showCenterTitle ? View.VISIBLE : View.GONE);
		context.findViewById(R.id.rightbtn).setVisibility(showRightBtn ? View.VISIBLE : View.GONE);
		if(showBackBtn){
			initLeftBtn(context);
		}
	}

	public static void show(final View context, boolean showBackBtn,
			boolean showLeftTitle, boolean showCenterTitle, boolean showRightBtn) {
		context.findViewById(R.id.back).setVisibility(
				showBackBtn ? View.VISIBLE : View.GONE);
		context.findViewById(R.id.lefttitle).setVisibility(
				showLeftTitle ? View.VISIBLE : View.GONE);
		context.findViewById(R.id.centertitle).setVisibility(
				showCenterTitle ? View.VISIBLE : View.GONE);
		context.findViewById(R.id.rightbtn).setVisibility(
				showRightBtn ? View.VISIBLE : View.GONE);
	}



	public static void initLeftBtn(final Activity context, int drawableid,
			OnClickListener listener) {
		Button back = (Button) context.findViewById(R.id.back);
		if(drawableid!=-1)
		back.setBackgroundResource(drawableid);
		back.setOnClickListener(listener);

	}

	public static void initLeftBtn(final View context, int drawableid,
			OnClickListener listener) {
		Button back = (Button) context.findViewById(R.id.back);
		back.setBackgroundResource(drawableid);
		back.setOnClickListener(listener);
	}

	public static void initLeftBtn(final Activity context) {
		Button back = (Button) context.findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				context.finish();
			}

		});
	}

	
	public static void initLeftTitle(final View context, String message) {
		TextView leftTitle = (TextView) context.findViewById(R.id.lefttitle);
		leftTitle.setText(message);
	}

	public static void initLeftTitle(final Activity context, String message) {
		TextView leftTitle = (TextView) context.findViewById(R.id.lefttitle);
		leftTitle.setText(message);
	}

	public static void initCenterTitle(final Activity context, String message) {
		TextView centerTitle = (TextView) context
				.findViewById(R.id.centertitle);
		centerTitle.setText(message);
	}

	public static void initCenterTitle(final View context, String message) {
		TextView centerTitle = (TextView) context
				.findViewById(R.id.centertitle);
		centerTitle.setText(message);
	}

	public static void initRightBtn(final Activity context, int resid,
			String message, OnClickListener listener) {
		Button rightBtn = (Button) context.findViewById(R.id.rightbtn);
		rightBtn.setText(message);
		if (resid != -1)
			rightBtn.setBackgroundResource(resid);
		rightBtn.setOnClickListener(listener);

	}
	public static void initRightBtn(final View context,int resid,
			String message, OnClickListener listener){
		Button rightBtn = (Button) context.findViewById(R.id.rightbtn);
		rightBtn.setText(message);
		if (resid != -1)
			rightBtn.setBackgroundResource(resid);
		rightBtn.setOnClickListener(listener);
	}
}
