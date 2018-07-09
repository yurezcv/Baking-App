package ua.yurezcv.bakingapp.ui.steps;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.yurezcv.bakingapp.R;
import ua.yurezcv.bakingapp.data.model.RecipeStep;

public class StepDetailFragment extends Fragment {

    @BindView(R.id.tv_sample)
    TextView mTextView;

    @BindView(R.id.exo_player)
    PlayerView mPlayerView;

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
                updateViews(recipeStep);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        ButterKnife.bind(this, rootView);

        initPlayer();

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
        // hideSystemUi();
        if ((Util.SDK_INT <= 23 || mPlayer == null)) {
            initPlayer();
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

    private void updateViews(RecipeStep recipeStep) {
        mTextView.setText(recipeStep.getDescription());
    }

    private void initPlayer() {
        if(mRecipeStep.hasVideo()) {
            if (mPlayer == null) {
                mPlayer = ExoPlayerFactory.newSimpleInstance(
                        new DefaultRenderersFactory(getContext()),
                        new DefaultTrackSelector(), new DefaultLoadControl());

                mPlayerView.setPlayer(mPlayer);
                mPlayerView.requestFocus();

                mPlayer.setPlayWhenReady(playWhenReady);
                mPlayer.seekTo(currentWindow, playbackPosition);

                Uri uri = Uri.parse(mRecipeStep.getVideoUrl());
                MediaSource mediaSource = buildMediaSource(uri);
                mPlayer.prepare(mediaSource, true, false);
            }
        } else {
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

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("bakingapp-exoplayer")).
                createMediaSource(uri);
    }

}
