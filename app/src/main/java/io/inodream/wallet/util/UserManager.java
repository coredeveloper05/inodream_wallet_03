package io.inodream.wallet.util;

import android.text.TextUtils;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;

import io.inodream.wallet.refer.retrofit.data.GoogleAuthData;

/**
 * <pre>
 *     author : zhen
 *     time   : 2023/07/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class UserManager {

    private UserManager mUserManager;
    private GoogleAuthData mAuthData;

    private static class HOLDER {
        private static final UserManager sInstance = new UserManager();
    }

    public static UserManager getInstance() {
        return HOLDER.sInstance;
    }

    public void setUser(GoogleAuthData data) {
        if (data == null) {
            return;
        }
        mAuthData = data;
        SPUtils.getInstance("user").put("auth", new Gson().toJson(data));
    }

    public void clearData() {
        mAuthData = null;
        SPUtils.getInstance("user").clear();
    }

    private GoogleAuthData getData() {
        if (mAuthData == null) {
            String json = SPUtils.getInstance("user").getString("auth");
            if (!TextUtils.isEmpty(json)) {
                mAuthData = new Gson().fromJson(json, GoogleAuthData.class);
            }
        }
        return mAuthData;
    }

    public String getRefreshToken() {
        try {
            return getData().getRefreshToken();
        } catch (Exception ignore) {
        }
        return "";
    }

    public String getAccToken() {
        try {
            return getData().getAccessToken();
        } catch (Exception ignore) {
        }
        return "";
    }

    public void setAccToken(String token) {
        try {
            getData().setAccessToken(token);
            setUser(getData());
        } catch (Exception ignore) {
        }
    }

    public String getUid() {
        try {
            return getData().getUid();
        } catch (Exception ignore) {
        }
        return "";
    }

    public String getAddress() {
        try {
            return getData().getWallet().getAddress();
        } catch (Exception ignore) {
        }
        return "";
    }

    public GoogleAuthData.WalletData getWalletData() {
        try {
            return getData().getWallet();
        } catch (Exception ignore) {
        }
        return null;
    }

    public void setWallet(GoogleAuthData.WalletData data) {
        try {
            getData().setWallet(data);
            setUser(getData());
        } catch (Exception ignore) {
        }
    }

    public boolean isSetPwd() {
        try {
            return getData().getWithdrawPw();
        } catch (Exception ignore) {
        }
        return true;
    }

    public void setPwd(boolean withdrawPw) {
        try {
            getData().setWithdrawPw(withdrawPw);
            setUser(getData());
        } catch (Exception ignore) {
        }
    }
}
