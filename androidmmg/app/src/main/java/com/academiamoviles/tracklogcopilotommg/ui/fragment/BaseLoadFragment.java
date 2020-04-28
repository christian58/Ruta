package com.academiamoviles.tracklogcopilotommg.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BaseLoadFragment extends BaseFragment {

    static final int INTERNAL_PROGRESS_CONTAINER_ID = 0x00ff0001;
    static final int INTERNAL_CONTENT_CONTAINER_ID = 0x00ff0002;

    View mProgressContainer;
    View mContentContainer;
    boolean mContentShown;

    private View mContentView;



    static final int INTERNAL_ERRORLOAD_CONTAINER_ID = 0x00ff0003;
    View mErrorLoadContainer;
    boolean mErrorLoadShown =false;

    public BaseLoadFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context context = getActivity();

        FrameLayout root = new FrameLayout(context);

        // ------------------------------------------------------------------

        LinearLayout pframe = new LinearLayout(context);
        pframe.setId(INTERNAL_PROGRESS_CONTAINER_ID);
        pframe.setOrientation(LinearLayout.VERTICAL);
        pframe.setVisibility(View.GONE);
        pframe.setGravity(Gravity.CENTER);

        ProgressBar progress = new ProgressBar(context, null,
                android.R.attr.progressBarStyle);

        pframe.addView(progress, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        root.addView(pframe, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // ------------------------------------------------------------------

        FrameLayout lframe = new FrameLayout(context);
        lframe.setId(INTERNAL_CONTENT_CONTAINER_ID);

        root.addView(lframe, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // ------------------------------------------------------------------

        LinearLayout dframe = new LinearLayout(context);
        dframe.setId(INTERNAL_ERRORLOAD_CONTAINER_ID);
        dframe.setOrientation(LinearLayout.VERTICAL);
        dframe.setVisibility(View.GONE);

        int margin= convertDpToPixel(8);

        TextView textView = new TextView(context, null);
        textView.setBackgroundColor(Color.RED);
        textView.setTextColor(getResources().getColor(android.R.color.white));
        textView.setText("Error en la conexión");
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(margin,margin,margin, margin);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setErrorLoadShown(false);
                reload();
            }
        });

        dframe.addView(textView, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        root.addView(dframe, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // ------------------------------------------------------------------

        root.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return root;
    }

    private int convertDpToPixel(int dp) {
        return Math.round(dp * getContext().getResources().getDisplayMetrics().density);
    }

    public void reload(){

    }

    public View inflateView(int resource, ViewGroup root){
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        return inflater.inflate(resource, root, false);
    }

    public void setView(View v) {

        FrameLayout lframe = (FrameLayout) getView().findViewById(INTERNAL_CONTENT_CONTAINER_ID);
        lframe.addView(v, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mContentView = v;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ensureView();
    }

    @Override
    public void onDestroyView() {
        mContentShown = false;
        mProgressContainer = mContentContainer = mContentView  = null;
        super.onDestroyView();
    }


    public void setContentShown(boolean shown) {
        setContentShown(shown, true);
    }

    public void setContentShownNoAnimation(boolean shown) {
        setContentShown(shown, false);
    }

    private void setContentShown(boolean shown, boolean animate) {

        ensureView();

        if (mContentShown == shown) {
            return;
        }
        mContentShown = shown;
        if (shown) {
            if (animate) {
                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_out));
                mContentContainer.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_in));
            } else {
                mProgressContainer.clearAnimation();
                mContentContainer.clearAnimation();
            }
            mProgressContainer.setVisibility(View.GONE);
            mContentContainer.setVisibility(View.VISIBLE);

            mErrorLoadContainer.setVisibility(View.GONE);
        } else {
            if (animate) {
                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_in));
                mContentContainer.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_out));
            } else {
                mProgressContainer.clearAnimation();
                mContentContainer.clearAnimation();
            }
            mProgressContainer.setVisibility(View.VISIBLE);
            mContentContainer.setVisibility(View.GONE);
            mErrorLoadContainer.setVisibility(View.GONE);
        }
    }



    private void ensureView() {
        if (mContentContainer != null && mProgressContainer != null) {
            return;
        }
        View root = getView();
        if (root == null) {
            throw new IllegalStateException("Content view not yet created");
        }
        mProgressContainer = root.findViewById(INTERNAL_PROGRESS_CONTAINER_ID);
        if (mProgressContainer == null) {
            throw new IllegalStateException("Can't be used with a custom content view");
        }

        mContentContainer = root.findViewById(INTERNAL_CONTENT_CONTAINER_ID);
        if (mContentContainer == null) {
            throw new IllegalStateException("Can't be used with a custom content view");
        }

        mErrorLoadContainer = root.findViewById(INTERNAL_ERRORLOAD_CONTAINER_ID);
        if (mErrorLoadContainer == null) {
            throw new IllegalStateException("Can't be used with a custom content view");
        }

        mContentShown = true;
        if (mContentView == null) {
            setContentShown(false, false);
        }

    }

    public void setErrorLoadShown(boolean shown) {
        setErrorLoadShown(shown, true);
    }
    public void setErrorLoadShownNoAnimation(boolean shown) {
        setErrorLoadShown(shown, false);
    }
    private void setErrorLoadShown(boolean shown, boolean animate) {

        ensureView();

        if (mErrorLoadShown == shown) {
            return;
        }

        if (mContentShown) {
            return;
        }
        mErrorLoadShown = shown;
        if (shown) {
            if (animate) {
                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_out));
                mErrorLoadContainer.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_in));
            } else {
                mProgressContainer.clearAnimation();
                mErrorLoadContainer.clearAnimation();
            }
            mProgressContainer.setVisibility(View.GONE);
            mContentContainer.setVisibility(View.GONE);
            mErrorLoadContainer.setVisibility(View.VISIBLE);
        } else {
            if (animate) {
                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_in));
                mErrorLoadContainer.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_out));
            } else {
                mProgressContainer.clearAnimation();
                mErrorLoadContainer.clearAnimation();
            }
            mProgressContainer.setVisibility(View.VISIBLE);
            mContentContainer.setVisibility(View.GONE);
            mErrorLoadContainer.setVisibility(View.GONE);
        }
    }

}
