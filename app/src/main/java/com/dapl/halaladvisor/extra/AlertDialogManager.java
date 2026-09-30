package com.dapl.halaladvisor.extra;

/**
 * Created by DAT-Asset-131 on 21-06-2016.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.dapl.halaladvisor.interfaces.OnClickOkDelegate;


public class AlertDialogManager {

    public static OnClickOkDelegate delegate;
    /**
     * Function to display simple Alert Dialog
     *
     * @param context - application context
     * @param title   - alert dialog title
     * @param message - alert message
     * @param status  - success/failure (used to set icon)
     *                - pass null if you don't want icon
     */
    public static void showAlertDialog(final Context context, String title, String message,
                                       Boolean status, final Boolean isOkIntent) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        if (status != null)
            // Setting alert dialog icon
//            alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

            // Setting OK Button
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                    "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (isOkIntent) {
                            /*Intent intent = new Intent(context, DetailsActivity.class);
                            context.startActivity(intent);*/
                               /* Activity activity = (Activity) context;
                                activity.finish();*/
                            }
                        }
                    });

        // Showing Alert Message
        if(!((Activity) context).isFinishing())
        {
            //show dialog
            alertDialog.show();
        }
    }





    public static void showOkCancelAlertDialog(final Context context, String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
//            alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

        // Setting OK Button
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            /*Intent intent = new Intent(context, DetailsActivity.class);
                            context.startActivity(intent);*/
                                /*Activity activity = (Activity) context;
                                activity.finish();*/
                        delegate.onClickOkDelegate(true);
                        dialogInterface.dismiss();
                    }
                });

        // Setting CANCEL Button
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                "CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            /*Intent intent = new Intent(context, DetailsActivity.class);
                            context.startActivity(intent);*/
                            /*Activity activity = (Activity) context;
                            activity.finish();*/
                        dialogInterface.dismiss();
                    }
                });

        // Showing Alert Message
        if (!((Activity)context).isFinishing()) {
            alertDialog.show();
        }

    }


    public void setLogoutDelegate(OnClickOkDelegate delegate) {
        this.delegate = delegate;
    }
}
