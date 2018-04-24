package com.folioreader.view;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.folioreader.Config;
import com.folioreader.Constants;
import com.folioreader.R;
import com.folioreader.model.event.ReloadDataEvent;
import com.folioreader.util.AppUtil;
import com.folioreader.util.UiUtil;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by mobisys2 on 11/16/2016.
 */

public class ConfigBottomSheetDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    public static final int DAY_BUTTON = 30;
    public static final int NIGHT_BUTTON = 31;
    public static final int BEIGE_BUTTON = 32;
    private static final int FADE_DAY_NIGHT_MODE = 500;

    private CoordinatorLayout.Behavior mBehavior;
    private Config.ColorMode colorMode;

    private RelativeLayout mContainer;
    private View mDayButton;
    private View mNightButton;
    private View mBeigeButton;
    private SeekBar mFontSizeSeekBar;
    private View mDialogView;
    private ConfigDialogCallback mConfigDialogCallback;
    private Config mConfig;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_config, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
                FrameLayout bottomSheet = (FrameLayout)
                        dialog.findViewById(android.support.design.R.id.design_bottom_sheet);
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setPeekHeight(0);
            }
        });

        mDialogView = view;
        mConfig = AppUtil.getSavedConfig(getActivity());
        initViews();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDialogView.getViewTreeObserver().addOnGlobalLayoutListener(null);
    }

    private void initViews() {
        inflateView();
        configFonts();
        mFontSizeSeekBar.setProgress(mConfig.getFontSize());
        configSeekBar();
        selectFont(mConfig.getFont(), false);
        colorMode = mConfig.getColorMode();
        if (colorMode == Config.ColorMode.black) {
            mContainer.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.night));
            mNightButton.setSelected(true);
            mDayButton.setSelected(false);
            mBeigeButton.setSelected(false);
        } else if (colorMode == Config.ColorMode.white) {
            mContainer.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
            mDayButton.setSelected(true);
            mNightButton.setSelected(false);
            mBeigeButton.setSelected(false);
        } else if (colorMode == Config.ColorMode.beige) {
            mContainer.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.beige));
            mBeigeButton.setSelected(true);
            mDayButton.setSelected(false);
            mNightButton.setSelected(false);
        }

        mConfigDialogCallback = (ConfigDialogCallback) getActivity();
    }

    private void inflateView() {
        mContainer = (RelativeLayout) mDialogView.findViewById(R.id.container);
        mFontSizeSeekBar = (SeekBar) mDialogView.findViewById(R.id.seekbar_font_size);
        mDayButton = mDialogView.findViewById(R.id.day_button);
        mNightButton = mDialogView.findViewById(R.id.night_button);
        mBeigeButton = mDialogView.findViewById(R.id.beige_button);
        mDayButton.setTag(DAY_BUTTON);
        mNightButton.setTag(NIGHT_BUTTON);
        mBeigeButton.setTag(BEIGE_BUTTON);
        mDayButton.setOnClickListener(this);
        mNightButton.setOnClickListener(this);
        mBeigeButton.setOnClickListener(this);
        mDialogView.findViewById(R.id.btn_vertical_orentation).setSelected(true);
    }

    private void configFonts() {
        ((StyleableTextView) mDialogView.findViewById(R.id.btn_font_andada)).setTextColor(UiUtil.getColorList(getActivity(), mConfig.getThemeColor(), R.color.grey_color));
        ((StyleableTextView) mDialogView.findViewById(R.id.btn_font_lato)).setTextColor(UiUtil.getColorList(getActivity(), mConfig.getThemeColor(), R.color.grey_color));
        ((StyleableTextView) mDialogView.findViewById(R.id.btn_font_lora)).setTextColor(UiUtil.getColorList(getActivity(), mConfig.getThemeColor(), R.color.grey_color));
        ((StyleableTextView) mDialogView.findViewById(R.id.btn_font_raleway)).setTextColor(UiUtil.getColorList(getActivity(), mConfig.getThemeColor(), R.color.grey_color));
        mDialogView.findViewById(R.id.btn_font_andada).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFont(Constants.FONT_ANDADA, true);
            }
        });

        mDialogView.findViewById(R.id.btn_font_lato).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFont(Constants.FONT_LATO, true);
            }
        });

        mDialogView.findViewById(R.id.btn_font_lora).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFont(Constants.FONT_LORA, true);
            }
        });

        mDialogView.findViewById(R.id.btn_font_raleway).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFont(Constants.FONT_RALEWAY, true);
            }
        });


        mDialogView.findViewById(R.id.btn_horizontal_orentation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConfigDialogCallback.onOrientationChange(1);
                mDialogView.findViewById(R.id.btn_horizontal_orentation).setSelected(true);
                mDialogView.findViewById(R.id.btn_vertical_orentation).setSelected(false);
            }
        });

        mDialogView.findViewById(R.id.btn_vertical_orentation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConfigDialogCallback.onOrientationChange(0);
                mDialogView.findViewById(R.id.btn_horizontal_orentation).setSelected(false);
                mDialogView.findViewById(R.id.btn_vertical_orentation).setSelected(true);
            }
        });
    }

    private void selectFont(int selectedFont, boolean isReloadNeeded) {
        if (selectedFont == Constants.FONT_ANDADA) {
            mDialogView.findViewById(R.id.btn_font_andada).setSelected(true);
            mDialogView.findViewById(R.id.btn_font_lato).setSelected(false);
            mDialogView.findViewById(R.id.btn_font_lora).setSelected(false);
            mDialogView.findViewById(R.id.btn_font_raleway).setSelected(false);
        } else if (selectedFont == Constants.FONT_LATO) {
            mDialogView.findViewById(R.id.btn_font_andada).setSelected(false);
            mDialogView.findViewById(R.id.btn_font_lato).setSelected(true);
            mDialogView.findViewById(R.id.btn_font_lora).setSelected(false);
            mDialogView.findViewById(R.id.btn_font_raleway).setSelected(false);
        } else if (selectedFont == Constants.FONT_LORA) {
            mDialogView.findViewById(R.id.btn_font_andada).setSelected(false);
            mDialogView.findViewById(R.id.btn_font_lato).setSelected(false);
            mDialogView.findViewById(R.id.btn_font_lora).setSelected(true);
            mDialogView.findViewById(R.id.btn_font_raleway).setSelected(false);
        } else if (selectedFont == Constants.FONT_RALEWAY) {
            mDialogView.findViewById(R.id.btn_font_andada).setSelected(false);
            mDialogView.findViewById(R.id.btn_font_lato).setSelected(false);
            mDialogView.findViewById(R.id.btn_font_lora).setSelected(false);
            mDialogView.findViewById(R.id.btn_font_raleway).setSelected(true);
        }

        mConfig.setFont(selectedFont);
        //if (mConfigDialogCallback != null) mConfigDialogCallback.onConfigChange();
        if (isAdded() && isReloadNeeded) {
            AppUtil.saveConfig(getActivity(), mConfig);
            EventBus.getDefault().post(new ReloadDataEvent());
        }
    }

    private void toggleBlackTheme(int color) {

        int day = getResources().getColor(R.color.white);
        int night = getResources().getColor(R.color.night);
        int beige = getResources().getColor(R.color.beige);
        int[] colors = {day, night, beige};

        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colors[colorMode.getValue()], colors[color]);
        colorAnimation.setDuration(FADE_DAY_NIGHT_MODE);
        colorAnimation.addUpdateListener(animator -> {
            int value = (int) animator.getAnimatedValue();
            mContainer.setBackgroundColor(value);
        });

        colorAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                colorMode = Config.ColorMode.values()[color];
                mConfig.setColorMode(colorMode);
                AppUtil.saveConfig(getActivity(), mConfig);
                EventBus.getDefault().post(new ReloadDataEvent());
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });

        colorAnimation.setDuration(FADE_DAY_NIGHT_MODE);
        colorAnimation.start();
    }

    private void configSeekBar() {
        Drawable thumbDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.seekbar_thumb);
        UiUtil.setColorToImage(getActivity(), mConfig.getThemeColor(), (thumbDrawable));
        UiUtil.setColorToImage(getActivity(), R.color.grey_color, mFontSizeSeekBar.getProgressDrawable());
        mFontSizeSeekBar.setThumb(thumbDrawable);

        mFontSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mConfig.setFontSize(progress);
                AppUtil.saveConfig(getActivity(), mConfig);
                EventBus.getDefault().post(new ReloadDataEvent());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (((Integer) v.getTag())) {
            case DAY_BUTTON:
                if (colorMode != Config.ColorMode.white) {
                    colorMode = Config.ColorMode.white;
                    toggleBlackTheme(0);
                    mDayButton.setSelected(true);
                    mNightButton.setSelected(false);
                    mBeigeButton.setSelected(false);
                    setToolBarColor();
                    setAudioPlayerBackground();
                }
                break;
            case NIGHT_BUTTON:
                if (colorMode != Config.ColorMode.black) {
                    colorMode = Config.ColorMode.black;
                    toggleBlackTheme(1);
                    mDayButton.setSelected(false);
                    mNightButton.setSelected(true);
                    mBeigeButton.setSelected(false);
                    setToolBarColor();
                    setAudioPlayerBackground();
                }
                break;
            case BEIGE_BUTTON:
                if (colorMode != Config.ColorMode.beige) {
                    colorMode = Config.ColorMode.beige;
                    toggleBlackTheme(2);
                    mDayButton.setSelected(false);
                    mNightButton.setSelected(false);
                    mBeigeButton.setSelected(true);
                    setToolBarColor();
                    setAudioPlayerBackground();
                }
                break;
            default:
                break;
        }
    }

    private void setToolBarColor() {
        if (colorMode == Config.ColorMode.black) {
            ((Activity) getContext()).findViewById(R.id.toolbar).
                    setBackgroundColor(getContext().getResources().getColor(R.color.white));
            ((TextView) ((Activity) getContext()).
                    findViewById(R.id.lbl_center)).
                    setTextColor(getResources().getColor(R.color.black));
        } else if (colorMode == Config.ColorMode.white) {
            ((Activity) getContext()).findViewById(R.id.toolbar).
                    setBackgroundColor(getContext().getResources().getColor(R.color.black));
            ((TextView) ((Activity) getContext()).
                    findViewById(R.id.lbl_center)).
                    setTextColor(getResources().getColor(R.color.white));
        } else if (colorMode == Config.ColorMode.beige) {
            ((Activity) getContext()).findViewById(R.id.toolbar).
                    setBackgroundColor(getContext().getResources().getColor(R.color.beige));
            ((TextView) ((Activity) getContext()).
                    findViewById(R.id.lbl_center)).
                    setTextColor(getResources().getColor(R.color.text_color));
        }

    }

    private void setAudioPlayerBackground() {
        if (colorMode == Config.ColorMode.black) {
            ((Activity) getContext()).
                    findViewById(R.id.container).
                    setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        } else if (colorMode == Config.ColorMode.white) {
            ((Activity) getContext()).
                    findViewById(R.id.container).
                    setBackgroundColor(ContextCompat.getColor(getContext(), R.color.night));
        } else if (colorMode == Config.ColorMode.beige) {
            ((Activity) getContext()).
                    findViewById(R.id.container).
                    setBackgroundColor(ContextCompat.getColor(getContext(), R.color.rusted));
        }
    }

    public interface ConfigDialogCallback {
        void onOrientationChange(int orentation);
    }
}
