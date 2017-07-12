package cn.itcast.odh.smsreposter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.itcast.odh.smsreposter.R;
import cn.itcast.odh.smsreposter.utils.SPUtils;
import cn.itcast.odh.smsreposter.utils.UIUtils;

/**
 * Created by rhinofly on 2016/11/24 time:0:15.
 */

public class WelcomeActivity extends AppCompatActivity {

    private static final String IS_FIRST_TIMEM = "FIRST";
    @BindView(R.id.welcome)
    RelativeLayout welcome;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initView();

    }

    private void initView() {
        UIUtils.stateBarSetting(this);
        animation();
    }

    private void animation() {
        // 旋转动画，基于中心点，360度
        RotateAnimation rotateAnimation = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setFillAfter(true);

        // 缩放动画，从无到有
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0, 1,
                0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);


        // 渐变动画，从无到有
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setFillAfter(true);

        // 三个动画并行
        AnimationSet animationSet = new AnimationSet(true);// 是否共享插值器
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(rotateAnimation);
        //设置动画
        welcome.startAnimation(animationSet);
        //动画监听
        animationSet.setAnimationListener(new myAnimationListener());

    }

    public class myAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            Boolean isFirstTime = SPUtils.getBoolean(WelcomeActivity.this, IS_FIRST_TIMEM, true);
            if (isFirstTime){
                finish();
                Intent intent=new Intent(WelcomeActivity.this,SplashActivity.class);
                startActivity(intent);
            }else{
                finish();
                Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(intent);

            }

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

}
