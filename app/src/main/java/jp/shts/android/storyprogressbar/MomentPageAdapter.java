package jp.shts.android.storyprogressbar;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;

import java.io.IOException;

import jp.shts.android.storiesprogressview.StoriesProgressView;

/**
 * Created by antonyagov on 11/9/17.
 */

class MomentPageAdapter extends PagerAdapter implements StoriesProgressView.StoriesListener {

    private static final int PROGRESS_COUNT = 6;

    private Context mContext;
    private FragmentManager mFragmentManager;
    private LayoutInflater mLayoutInflater;

    private StoriesProgressView storiesProgressView;
    private RelativeLayout videoContainer;

    private int counter = 0;
    private final int[] resources = new int[]{
            R.drawable.sample1,
            R.drawable.sample2,
            R.drawable.sample3,
            R.drawable.sample4,
            R.drawable.sample5,
            R.drawable.sample6,
    };

    private final long[] durations = new long[]{
            5000L, 10000L, 15000L, 40000L, 50000L, 10000,
    };

    long pressTime = 0L;
    long limit = 500L;

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.e("PageAdapter", "Touch listener");
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressTime = System.currentTimeMillis();
                    storiesProgressView.pause();
                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    storiesProgressView.resume();
                    return limit < now - pressTime;
            }
            return false;
        }
    };

    public MomentPageAdapter(Context context, FragmentManager supportFragmentManager) {
        mContext = context;
        mFragmentManager = supportFragmentManager;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View itemView = mLayoutInflater.inflate(R.layout.fragment_page, container, false);
        itemView.setTag("View" + position);
        container.addView(itemView);

        storiesProgressView = (StoriesProgressView) itemView.findViewById(R.id.stories);
        storiesProgressView.setStoriesCount(PROGRESS_COUNT);
        storiesProgressView.setStoryDuration(30000L);
        // or
        // storiesProgressView.setStoriesCountWithDurations(durations);
        storiesProgressView.setStoriesListener(this);
        storiesProgressView.startStories();

        ImageView image = (ImageView) itemView.findViewById(R.id.image);
        image.setImageResource(resources[counter]);
//        image.setImageResource(R.drawable.boglio);


        // bind reverse view
        View reverse = itemView.findViewById(R.id.reverse);
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.reverse();
            }
        });
        reverse.setOnTouchListener(onTouchListener);

        // bind skip view
        View skip = itemView.findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.skip();
            }
        });
        skip.setOnTouchListener(onTouchListener);

        TextureView video = (TextureView) itemView.findViewById(R.id.videolayout);

//        try {
//            mediaPlayer.setDataSource(mContext, Uri.parse("http://clips.vorwaerts-gmbh.de/VfE_html5.mp4"));
//            VideoSurfaceView mVideoView     = new VideoSurfaceView(mContext, mediaPlayer);
//
//            video.addView(mVideoView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
//            mVideoView.onResume();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        video.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                Surface sur = new Surface(surface);
                try {
                    MediaPlayer mMediaPlayer= new MediaPlayer();
                    mMediaPlayer.setDataSource("http://clips.vorwaerts-gmbh.de/VfE_html5.mp4");
                    mMediaPlayer.setSurface(sur);
                    mMediaPlayer.prepareAsync();
                    // Play video when the media source is ready for playback.
                    mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            mediaPlayer.start();
                        }
                    });
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (SecurityException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });

//        MediaController mediaController = new MediaController(mContext);
//        mediaController.setAnchorView(video);
//        video.setMediaController(mediaController);
//        video.setVideoURI(Uri.parse("http://clips.vorwaerts-gmbh.de/VfE_html5.mp4"));
//
//        video.start();

        return itemView;
    }

    public RelativeLayout getVideoContainer() {
        return videoContainer;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public void onNext() {
        counter++;
        if(counter >= 6)
            counter = 0;

//        image.setImageResource(resources[counter]);
    }

    @Override
    public void onPrev() {
        if ((counter - 1) < 0) return;
//        image.setImageResource(resources[--counter]);
    }


    @Override
    public void onComplete() {
    }
}
