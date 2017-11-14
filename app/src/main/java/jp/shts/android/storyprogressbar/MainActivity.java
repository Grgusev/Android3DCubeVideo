package jp.shts.android.storyprogressbar;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import jp.shts.android.storiesprogressview.StoriesProgressView;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private MomentPageAdapter mPageAdapter;

    private VideoView video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.pager_layout);
        mPageAdapter = new MomentPageAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setPageTransformer(true, new CubeOutTransformer(), ViewCompat.LAYER_TYPE_NONE);
//        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        // Very important !
        super.onDestroy();
    }

    public abstract class BaseTransformer implements ViewPager.PageTransformer {
        public BaseTransformer() {
        }

        protected abstract void onTransform(View var1, float var2);

        public void transformPage(View view, float position) {
            this.onPreTransform(view, position);
            this.onTransform(view, position);
            this.onPostTransform(view, position);
        }

        protected boolean hideOffscreenPages() {
            return true;
        }

        protected boolean isPagingEnabled() {
            return false;
        }

        protected void onPreTransform(View view, float position) {
            float width = (float)view.getWidth();
            view.setRotationX(0.0F);
            view.setRotationY(0.0F);
            view.setRotation(0.0F);
            view.setScaleX(1.0F);
            view.setScaleY(1.0F);
            view.setPivotX(0.0F);
            view.setPivotY(0.0F);
            view.setTranslationY(0.0F);
            view.setTranslationX(this.isPagingEnabled()?0.0F:-width * position);
            if(this.hideOffscreenPages()) {
                view.setAlpha(position > -1.0F && position < 1.0F?1.0F:0.0F);
            } else {
                view.setAlpha(1.0F);
            }
        }

        protected void onPostTransform(View view, float position) {
        }
    }

    public class CubeOutTransformer extends BaseTransformer {
        public CubeOutTransformer() {
        }

        protected void onTransform(View view, float position) {
            view.setPivotX(position < 0.0F?(float)view.getWidth():0.0F);
            view.setPivotY((float)view.getHeight() * 0.5F);
            view.setRotationY(90.0F * position);
        }

        public boolean isPagingEnabled() {
            return true;
        }
    }
}
