package ui.zeffect.cn.updatelib;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import ui.zeffect.cn.updatelib.views.NumberProgressBar;

/**
 * Created by zeffect on 19-1-23.
 */

public abstract class UpdateDialog extends DialogFragment implements View.OnClickListener {

    private View rootView;

    public abstract void okClick();

    public abstract String updateHtml();


    public abstract void initView();

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.updatelib_update_dialog, container, false);
            baseInitView();
            initView();
        }
        return rootView;
    }

    private WebView webView;

    protected NumberProgressBar numberProgressBar;
    protected Button installBtn;

    private final void baseInitView() {
        rootView.findViewById(R.id.iv_close).setOnClickListener(this);
        installBtn = (Button) rootView.findViewById(R.id.btn_ok);
        installBtn.setOnClickListener(this);
        webView = (WebView) rootView.findViewById(R.id.webView);
        initSettings(webView);
        numberProgressBar = (NumberProgressBar) rootView.findViewById(R.id.npb);
        if (updateHtml() != null) {
            webView.loadDataWithBaseURL(null, updateHtml(), "text/html", "utf-8", null);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            boolean isPad = ScreenUtils.isPad(getContext());
//            boolean isLand = ScreenUtils.isLand(getContext());
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            getDialog().setCanceledOnTouchOutside(false);
            getDialog().setCancelable(false);
            getDialog().getWindow().setLayout((int) (displayMetrics.widthPixels * 0.8), (int) (displayMetrics.heightPixels * 0.8));
        }
    }

    @Override
    @CallSuper
    public void onClick(View view) {
        if (view.getId() == R.id.iv_close) {
            this.dismiss();
        } else if (view.getId() == R.id.btn_ok) {
            okClick();
        }
    }

    public static void initSettings(WebView mWebView) {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setUserAgentString("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36");
        //支持获取手势焦点，输入用户名、密码或其他
        webSettings.setJavaScriptEnabled(true);  //支持js
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true);  //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setSupportZoom(true);  //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。
        //若上面是false，则该WebView不可缩放，这个不管设置什么都不能缩放。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        }
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
        webSettings.supportMultipleWindows();  //多窗口
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setAllowFileAccess(true);  //设置可以访问文件
        webSettings.setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
    }
}
