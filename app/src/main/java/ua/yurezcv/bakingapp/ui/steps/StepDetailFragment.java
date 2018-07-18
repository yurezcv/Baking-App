package ua.yurezcv.bakingapp.ui.steps;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.yurezcv.bakingapp.R;
import ua.yurezcv.bakingapp.data.model.RecipeStep;

public class StepDetailFragment extends Fragment {

    private static final String STATE_PLAY_WHEN_READY = "state-play-when-ready";
    private static final String STATE_PLAYBACK_POSITION = "state-playback-position";
    private static final String STATE_CURRENT_WINDOW = "state-current-window";

    @BindView(R.id.tv_sample)
    TextView mStepDescTextView;

    @BindView(R.id.exo_player)
    PlayerView mPlayerView;

    @BindView(R.id.iv_step_thumbnail)
    ImageView mStepThumbnail;

    private SimpleExoPlayer mPlayer;

    private RecipeStep mRecipeStep;

    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady = true;

    public StepDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StepViewModel stepViewModel = ViewModelProviders.of(getActivity()).get(StepViewModel.class);
        mRecipeStep = stepViewModel.getSelectedStep().getValue();

        stepViewModel.getSelectedStep().observe(this, new Observer<RecipeStep>() {
            @Override
            public void onChanged(@Nullable RecipeStep recipeStep) {
                mRecipeStep = recipeStep;
                updateViews();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        ButterKnife.bind(this, rootView);

        if(savedInstanceState != null) {
            playbackPosition = savedInstanceState.getLong(STATE_PLAYBACK_POSITION);
            currentWindow = savedInstanceState.getInt(STATE_CURRENT_WINDOW);
            playWhenReady = savedInstanceState.getBoolean(STATE_PLAY_WHEN_READY);
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initPlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mPlayer == null)) {
            initPlayer();
        }

        if (mPlayer != null && playbackPosition != 0) {
            mPlayer.seekTo(currentWindow, playbackPosition);
            mPlayer.setPlayWhenReady(playWhenReady);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        // save the player state
        if(mPlayer != null) {
            playbackPosition = mPlayer.getCurrentPosition();
            currentWindow = mPlayer.getCurrentWindowIndex();
            playWhenReady = mPlayer.getPlayWhenReady();

            outState.putLong(STATE_PLAYBACK_POSITION, playbackPosition);
            outState.putInt(STATE_CURRENT_WINDOW, currentWindow);
            outState.putBoolean(STATE_PLAY_WHEN_READY, playWhenReady);
        }

        super.onSaveInstanceState(outState);
    }

    private void updateViews() {
        mStepDescTextView.setText(mRecipeStep.getDescription());

        // show the ImageVIew if a step has a thumbnail picture
        if(mRecipeStep.hasThumbnail()) {
            Picasso.get().load(mRecipeStep.getThumbnailUrl()).into(mStepThumbnail);
            mStepThumbnail.setVisibility(View.VISIBLE);
        }

        // init video player
        initPlayer();
    }

    private void initPlayer() {
        if(mRecipeStep.hasVideo()) {
            if (mPlayer == null) {
                mPlayer = ExoPlayerFactory.newSimpleInstance(
                        new DefaultRenderersFactory(getContext()),
                        new DefaultTrackSelector(), new DefaultLoadControl());

                mPlayerView.setPlayer(mPlayer);
                mPlayerView.requestFocus();

                mPlayer.seekTo(currentWindow, playbackPosition);
                mPlayer.setPlayWhenReady(playWhenReady);

                boolean resetPosition = playbackPosition == 0;

                MediaSource mediaSource = buildMediaSource(mRecipeStep.getVideoUrl());
                mPlayer.prepare(mediaSource, resetPosition, false);

                mPlayerView.setVisibility(View.VISIBLE);
            } else {
                MediaSource mediaSource = buildMediaSource(mRecipeStep.getVideoUrl());
                mPlayer.prepare(mediaSource, true, false);
                mPlayerView.setVisibility(View.VISIBLE);
            }
        } else {
            if(mPlayer != null && mPlayer.getPlayWhenReady()) {
                mPlayer.stop();
            }
            mPlayerView.setVisibility(View.GONE);
        }
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            playbackPosition = mPlayer.getCurrentPosition();
            currentWindow = mPlayer.getCurrentWindowIndex();
            playWhenReady = mPlayer.getPlayWhenReady();
            mPlayer.release();
            mPlayer = null;
        }
    }

    private MediaSource buildMediaSource(String videoUrl) {
        Uri uri = Uri.parse(videoUrl);

        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("bakingapp-exoplayer")).
                createMediaSource(uri);
    }

}
