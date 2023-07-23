package io.inodream.wallet;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

import io.inodream.wallet.refer.retrofit.data.TokenInfosData;

/**
 * <pre>
 *     author : zhen
 *     time   : 2023/07/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class App extends Application {

   private static App mApp;
   private TokenInfosData mTokenInfosData;

   @Override
   public void onCreate() {
      super.onCreate();
      Utils.init(this);
      mApp = this;
   }

   public static App getInstance() {
      return mApp;
   }

   public TokenInfosData getTokenInfosData() {
      return mTokenInfosData;
   }

   public void setTokenInfosData(TokenInfosData tokenInfosData) {
      mTokenInfosData = tokenInfosData;
   }
}
