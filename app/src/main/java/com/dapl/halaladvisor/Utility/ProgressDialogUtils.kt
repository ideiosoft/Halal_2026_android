package  com.dapl.halaladvisor.Utility;



import android.app.Activity
import android.content.Context
import android.graphics.Color
//import cc.cloudist.acplibrary.ACProgressConstant
//import cc.cloudist.acplibrary.ACProgressFlower
import com.dapl.halaladvisor.R


//class ProgressDialogUtils {
//
//
//
//    companion object {
//        private var dialog: ACProgressFlower? = null
//
//        fun showDialog(activity: Context?) {
//
//
//            dialog = ACProgressFlower.Builder(activity)
//                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
//                    .themeColor(Color.WHITE)
//                    .text("Please wait")
//                    .fadeColor(R.color.blue).build()
//            if (dialog != null && !dialog!!.isShowing()) {
//                dialog!!.show()
//                dialog!!.setCanceledOnTouchOutside(false)
//            }
//
//        }
//
//        fun showDialog(activity: Activity, Canceled: Boolean) {
//
//
//            dialog = ACProgressFlower.Builder(activity)
//                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
//                    .themeColor(Color.WHITE)
//                    .text("Please wait")
//                    .fadeColor(Color.DKGRAY).build()
//
//            dialog!!.show()
//            dialog!!.setCanceledOnTouchOutside(Canceled)
//        }
//
//
//        fun dismissDialog(activity: Context?) {
//            if (dialog != null && dialog!!.isShowing()) {
//                dialog!!.dismiss()
//            }
//        }
//    }
//
//
//
//}