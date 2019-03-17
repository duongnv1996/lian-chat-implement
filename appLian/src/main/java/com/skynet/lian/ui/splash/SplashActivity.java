package com.skynet.lian.ui.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.blankj.utilcode.util.LogUtils;
import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.ui.main.MainActivity;
import com.skynet.lian.ui.views.ProgressDialogCustom;

import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SplashActivity extends AppCompatActivity implements SlideContract.View {


    private ProgressDialogCustom dialogCustom;
    private SlideContract.PresenterI presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        dialogCustom = new ProgressDialogCustom(this);
        presenter = new SlidePresenterI(this);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
////                if (!AppController.getInstance().getmSetting().getBoolean("isShown", false)) {
//////                    startActivity(new Intent(SplashActivity.this, ShowcaseActivity.class));
////                    return;
////                } else {
////                }
//            }
//        }, 500);
//        Profile profile =  new Profile();
////        profile.setId("3ed572a6-0278-11e9-b7ce-005056b2404b");  //son
////        profile.setId("b307b18c-09af-11e9-b7ce-005056b2404b");  //tram
//        profile.setId("663ccc4b-0278-11e9-b7ce-005056b2404b");  //tam
//        AppController.getInstance().setmProfileUser(profile);
        presenter.getInfor();

//        getDialogProgress().hideDialog();
//        MainApplication.getInstance().setDay(day);


//        Spannable wordtoSpan = new SpannableString("VINENGLISH");
//        wordtoSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#ecac1d")), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        tvName.setText(wordtoSpan);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            tvName.setText(Html.fromHtml(getString(R.string.splash_name), Html.FROM_HTML_MODE_LEGACY));
//        } else {
//            tvName.setText(Html.fromHtml(getString(R.string.splash_name)));
//        }
    }


    @Override
    public void onSuccessGetInfor() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public Context getMyContext() {
        return this;
    }

    @Override
    public void showProgress() {
        dialogCustom.showDialog();
    }

    @Override
    public void hiddenProgress() {
        dialogCustom.hideDialog();
    }

    @OnClick(R2.id.button2)
    public void onClickMain() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onErrorApi(String message) {
        LogUtils.e(message);
//        startActivity(new Intent(SplashActivity.this, AuthActivity.class));
//        finish();
    }

    @Override
    public void onError(String message) {
        LogUtils.e(message);
//        startActivity(new Intent(SplashActivity.this, AuthActivity.class));
//        finish();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroyView();
        super.onDestroy();
    }

    @Override
    public void onErrorAuthorization() {
//        startActivity(new Intent(SplashActivity.this, AuthActivity.class));
//        finish();
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
