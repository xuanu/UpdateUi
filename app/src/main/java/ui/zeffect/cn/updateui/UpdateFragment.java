package ui.zeffect.cn.updateui;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import ui.zeffect.cn.updatelib.UpdateDialog;

/**
 * Created by zeffect on 19-1-23.
 */

public class UpdateFragment extends UpdateDialog {
    @Override
    public void okClick() {
        //点击安装时
        boolean inSdcard = false;//本地是否存在下载好的APK
        installBtn.setVisibility(inSdcard ? View.VISIBLE : View.GONE);
        numberProgressBar.setVisibility(inSdcard ? View.GONE : View.VISIBLE);
        if (inSdcard) {
            //调用安装应用
        } else {
            //下载应用
            progressHandler.sendEmptyMessage(0x10);
        }
    }

    private int progress = 0;


    private Handler progressHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x10) {
                if (progress < 100) {
                    progress += 1;
                    numberProgressBar.setProgress(progress);
                    sendEmptyMessageDelayed(0x10, 100);
                } else {
                    numberProgressBar.setProgress(100);
                    numberProgressBar.setVisibility(View.GONE);
                    installBtn.setVisibility(View.VISIBLE);
                    installBtn.setText("安装");
                    progress = 0;
                    //自动调用安装也可以
                }
            }
        }
    };

    @Override
    public String updateHtml() {
        //更新内容，HTML代码
        return "";
    }

    @Override
    public void initView() {
        //base初始化完成后会调用
        boolean inSdcard = false;//本地是否存在下载好的APK
        installBtn.setTag(inSdcard ? "安装" : "升级");
    }


}
