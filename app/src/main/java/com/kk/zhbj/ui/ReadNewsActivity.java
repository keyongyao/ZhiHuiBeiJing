package com.kk.zhbj.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;

import com.kk.zhbj.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class ReadNewsActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "main";
    @ViewInject(R.id.ib_readNews_share)
    ImageButton mIbshare;
    @ViewInject(R.id.ib_readNews_textSize)
    ImageButton mIbTextSize;
    @ViewInject(R.id.ib_readNews_backArrow)
    ImageButton mIbBack;
    @ViewInject(R.id.ib_contentTitle_menu)
    ImageButton mIbMenu;
    @ViewInject(R.id.wv_readNewActivity)
    WebView mWvNews;
    /**
     * 单出选择字体大小的对话框
     */
    int retureChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_news);
        ViewUtils.inject(this, this);
        initUI();
        setListener();
    }

    private void initUI() {
        mIbBack.setVisibility(View.VISIBLE);
        mIbTextSize.setVisibility(View.VISIBLE);
        mIbshare.setVisibility(View.VISIBLE);
        mIbMenu.setVisibility(View.INVISIBLE);
        Intent intent = getIntent();
        String url = (String) intent.getExtras().get("url");
        mWvNews.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

        });
        mWvNews.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                Log.i(TAG, "ReadNewsActivity.onProgressChanged: " + newProgress);
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                Log.i(TAG, "ReadNewsActivity.onReceivedTitle: " + title);
                super.onReceivedTitle(view, title);
            }
        });
        WebSettings settings = mWvNews.getSettings();
        settings.setJavaScriptEnabled(true);  //支持JS
        settings.setDisplayZoomControls(true); // 支持放大 按钮
        settings.setSupportZoom(true); // 支持缩放
        settings.setTextSize(WebSettings.TextSize.NORMAL);
        mWvNews.loadUrl(url); // 打开URL

    }

    // 设置监视器
    private void setListener() {
        mIbBack.setOnClickListener(this);
        mIbshare.setOnClickListener(this);
        mIbTextSize.setOnClickListener(this);
    }

    /**
     * 处理按钮点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_readNews_backArrow:
                onBackPressed();
                break;
            case R.id.ib_readNews_share:
                Toast.makeText(this, "处理分享代码", Toast.LENGTH_SHORT).show();
                // TODO: 2016/10/13
                showShare();
                break;
            case R.id.ib_readNews_textSize:
                WebSettings settings = mWvNews.getSettings();
                showTextSizeChoice(settings.getTextSize().ordinal());
                break;
        }
    }

    private void showTextSizeChoice(final int def) {
        retureChoice = def;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择字体大小");
        String[] choices = {"最小", "较小", "平常", "较大", "最大"};
        final WebSettings settings = mWvNews.getSettings();
        builder.setSingleChoiceItems(choices, def, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ReadNewsActivity.this, "" + which, Toast.LENGTH_SHORT).show();
                retureChoice = which;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (retureChoice) {
                    case 0:
                        settings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;
                    case 1:
                        settings.setTextSize(WebSettings.TextSize.SMALLER);

                        break;
                    case 2:
                        settings.setTextSize(WebSettings.TextSize.NORMAL);

                        break;
                    case 3:
                        settings.setTextSize(WebSettings.TextSize.LARGER);

                        break;
                    case 4:
                        settings.setTextSize(WebSettings.TextSize.LARGEST);
                        break;
                }
            }
        });
        builder.setNegativeButton("取消", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * 单出分享 的页面
     */
    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("智慧我家很实用哦，赶快来下载吧");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://www.baidu.com");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("看新闻 处理家事 等等");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        //  oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath(getResources().getAssets().toString() + "icon_150.png");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }

}
