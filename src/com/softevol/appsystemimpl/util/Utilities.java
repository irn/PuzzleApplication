package com.softevol.appsystemimpl.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import com.softevol.appsystemimpl.R;

public class Utilities {

//	public static void email(Context context, String emailTo, String emailCC, String subject, String emailText, String[] filePaths) {
//		final Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
//		emailIntent.setType("application/puzzle");
//		emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { emailTo });
//		emailIntent.putExtra(Intent.EXTRA_CC, new String[] { emailCC });
//		emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
//		emailIntent.putExtra(Intent.EXTRA_TEXT, emailText);
//		if (filePaths != null) {
//			ArrayList<Uri> uris = new ArrayList<Uri>();
//			for (String file : filePaths) {
//				File fileIn = new File(file);
//				Uri u = Uri.fromFile(fileIn);
//				uris.add(u);
//			}
//			emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
//		}
//		context.startActivity(Intent.createChooser(emailIntent, context.getString(R.string.send_puzzle)));
//	}

	public static void startActivityWithAnimation(Activity oldActivity, Class<? extends Activity> newActivity) {
		oldActivity.startActivity(new Intent(oldActivity, newActivity));
		oldActivity.finish();
		oldActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

	public static void confirmDialog(final View v, String title, String message, final Class<? extends Activity> newActivity) {
		AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
		builder.setCancelable(true);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				startActivityWithAnimation((Activity) v.getContext(), newActivity);
			}
		});
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

//	public static FirstRunChecker firstRunChecker(Context context) {
//		return new FirstRunChecker(context.getSharedPreferences(FirstRunChecker.class.getName(), Context.MODE_PRIVATE));
//	}
}
