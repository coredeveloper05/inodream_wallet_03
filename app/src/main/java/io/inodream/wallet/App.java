package io.inodream.wallet;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

/**
 * <pre>
 *     author : zhen
 *     time   : 2023/07/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class App extends Application {

   @Override
   public void onCreate() {
      super.onCreate();
      Utils.init(this);
   }
}
