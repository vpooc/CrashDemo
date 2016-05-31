package com.vpooc.crashdemo;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;



import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.widget.Toast;

public class CrashHandler implements UncaughtExceptionHandler {

	private TApplication app;

	public CrashHandler(Application app) {

		this.app = (TApplication) app;
	}

	//
	@SuppressLint("ServiceCast") @Override
	public void uncaughtException(Thread thread, Throwable ex) {
		String str = null;
		str = ex.getMessage();
		// 输出一条错误信息
		System.out.println("CrashHandler " + str);
		// e.printWriter()
		// 输出多条错误信息

		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		ex.printStackTrace(printWriter);
		str = stringWriter.toString();
		System.out.println();
		// 用Volley等发送错误信息到网上（此处没有实现）


		// toast提示信息
		// 主线程已经崩溃，需要重新开新线程Toast
		new Thread(new Runnable() {
			@Override
			public void run() {

				Looper.prepare();
				Toast.makeText(app, "重新启动", Toast.LENGTH_LONG).show();
				Looper.loop();

			}
		}).start();

		// 重启Activity
		Intent intent = new Intent(app, MainActivity.class);
		PendingIntent p = PendingIntent.getActivity(app, 555, intent,Intent.FLAG_ACTIVITY_NEW_TASK);
		// 稍等一会再重新起动
		AlarmManager alarmManager = (AlarmManager) app
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager
				.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, p);

		//结束应用所有的Activity
		app.finishActivity();
	}

}
