package io.inodream.wallet;

import android.app.Application;
import android.view.View;

import com.blankj.utilcode.util.Utils;

import java.util.concurrent.TimeUnit;

import io.inodream.wallet.refer.retrofit.data.TokenInfosData;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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

   public static final int REQUEST_VALIDATOR_TIME = 300;

   public int requestValidatorTime = REQUEST_VALIDATOR_TIME;
   private CompositeDisposable compositeDisposable = new CompositeDisposable();

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

   public void tickerRequestValidatorTime() {
      if(requestValidatorTime == REQUEST_VALIDATOR_TIME) {
         compositeDisposable.add(Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
                 .observeOn(Schedulers.io())
                 .subscribe(
                         (item) -> {
                            requestValidatorTime--;
                            if(requestValidatorTime == 0) initRequestValidatorTime();
                         }
                 ));
      }
   }

   private void initRequestValidatorTime() {
      requestValidatorTime = REQUEST_VALIDATOR_TIME;
      compositeDisposable.clear();
   }
}
