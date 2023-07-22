package io.inodream.wallet.activitys;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.zxing.Result;
import com.king.zxing.CaptureActivity;
import com.king.zxing.DecodeConfig;
import com.king.zxing.DecodeFormatManager;
import com.king.zxing.analyze.MultiFormatAnalyzer;
import com.king.zxing.config.AspectRatioCameraConfig;

/**
 * <pre>
 *     author : zhen
 *     time   : 2023/07/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class CameraCaptureActivity extends CaptureActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化解码配置
        DecodeConfig decodeConfig = new DecodeConfig();
        decodeConfig.setHints(DecodeFormatManager.QR_CODE_HINTS)//如果只有识别二维码的需求，这样设置效率会更高，不设置默认为DecodeFormatManager.DEFAULT_HINTS
                .setFullAreaScan(false)//设置是否全区域识别，默认false
                .setAreaRectRatio(0.8f)//设置识别区域比例，默认0.8，设置的比例最终会在预览区域裁剪基于此比例的一个矩形进行扫码识别
                .setAreaRectVerticalOffset(0)//设置识别区域垂直方向偏移量，默认为0，为0表示居中，可以为负数
                .setAreaRectHorizontalOffset(0);//设置识别区域水平方向偏移量，默认为0，为0表示居中，可以为负数

        // 在启动预览之前，设置分析器，只识别二维码
        getCameraScan()
                .setCameraConfig(new AspectRatioCameraConfig(this))//设置相机配置，使用 AspectRatioCameraConfig
                .setVibrate(true)//设置是否震动，默认为false
                .setAnalyzer(new MultiFormatAnalyzer(decodeConfig));//设置分析器,如果内置实现的一些分析器不满足您的需求，你也可以自定义去实现
    }

    @Override
    public boolean onScanResultCallback(Result result) {
        Intent intent = new Intent();
        intent.putExtra("result", result.getText());
        setResult(RESULT_OK, intent);
        finish();
        return true;
    }
}
