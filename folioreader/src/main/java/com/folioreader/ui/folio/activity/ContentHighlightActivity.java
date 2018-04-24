package com.folioreader.ui.folio.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.folioreader.Config;
import com.folioreader.Constants;
import com.folioreader.R;
import com.folioreader.ui.folio.fragment.HighlightFragment;
import com.folioreader.ui.tableofcontents.view.TableOfContentFragment;
import com.folioreader.util.AppUtil;
import com.folioreader.util.FolioReader;
import com.folioreader.util.UiUtil;

public class ContentHighlightActivity extends AppCompatActivity {
    private Config mConfig;
    private Config.ColorMode colorMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_highlight);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        mConfig = AppUtil.getSavedConfig(this);
        colorMode = mConfig != null ? mConfig.getColorMode() : Config.ColorMode.black;
        initViews();
    }

    private void initViews() {
        UiUtil.setColorToImage(this, mConfig.getThemeColor(), ((ImageView) findViewById(R.id.btn_close)).getDrawable());
        findViewById(R.id.layout_content_highlights).setBackgroundDrawable(UiUtil.getShapeDrawable(this, mConfig.getThemeColor()));
        if (colorMode == Config.ColorMode.black) {
            findViewById(R.id.toolbar).setBackgroundColor(Color.BLACK);
            findViewById(R.id.btn_contents).setBackgroundDrawable(UiUtil.convertColorIntoStateDrawable(this, mConfig.getThemeColor(), R.color.black));
            findViewById(R.id.btn_highlights).setBackgroundDrawable(UiUtil.convertColorIntoStateDrawable(this, mConfig.getThemeColor(), R.color.black));
            ((TextView) findViewById(R.id.btn_contents)).setTextColor(UiUtil.getColorList(this, R.color.black, mConfig.getThemeColor()));
            ((TextView) findViewById(R.id.btn_highlights)).setTextColor(UiUtil.getColorList(this, R.color.black, mConfig.getThemeColor()));

        } else if (colorMode == Config.ColorMode.white) {
            ((TextView) findViewById(R.id.btn_contents)).setTextColor(UiUtil.getColorList(this, R.color.white, mConfig.getThemeColor()));
            ((TextView) findViewById(R.id.btn_highlights)).setTextColor(UiUtil.getColorList(this, R.color.white, mConfig.getThemeColor()));
            findViewById(R.id.btn_contents).setBackgroundDrawable(UiUtil.convertColorIntoStateDrawable(this, mConfig.getThemeColor(), R.color.white));
            findViewById(R.id.btn_highlights).setBackgroundDrawable(UiUtil.convertColorIntoStateDrawable(this, mConfig.getThemeColor(), R.color.white));
        } else if (colorMode == Config.ColorMode.beige) {
            ((TextView) findViewById(R.id.btn_contents)).setTextColor(UiUtil.getColorList(this, R.color.beige, mConfig.getThemeColor()));
            ((TextView) findViewById(R.id.btn_highlights)).setTextColor(UiUtil.getColorList(this, R.color.beige, mConfig.getThemeColor()));
            findViewById(R.id.btn_contents).setBackgroundDrawable(UiUtil.convertColorIntoStateDrawable(this, mConfig.getThemeColor(), R.color.beige));
            findViewById(R.id.btn_highlights).setBackgroundDrawable(UiUtil.convertColorIntoStateDrawable(this, mConfig.getThemeColor(), R.color.beige));
        }

        loadContentFragment();
        findViewById(R.id.btn_close).setOnClickListener(v -> finish());

        findViewById(R.id.btn_contents).setOnClickListener(v -> loadContentFragment());

        findViewById(R.id.btn_highlights).setOnClickListener(v -> loadHighlightsFragment());
    }

    private void loadContentFragment() {
        findViewById(R.id.btn_contents).setSelected(true);
        findViewById(R.id.btn_highlights).setSelected(false);
        TableOfContentFragment contentFrameLayout
                = TableOfContentFragment.newInstance(getIntent().getStringExtra(Constants.CHAPTER_SELECTED),
                getIntent().getStringExtra(Constants.BOOK_TITLE));
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.parent, contentFrameLayout);
        ft.commit();
    }

    private void loadHighlightsFragment() {
        findViewById(R.id.btn_contents).setSelected(false);
        findViewById(R.id.btn_highlights).setSelected(true);
        String bookId = getIntent().getStringExtra(FolioReader.INTENT_BOOK_ID);
        String bookTitle = getIntent().getStringExtra(Constants.BOOK_TITLE);
        HighlightFragment highlightFragment = HighlightFragment.newInstance(bookId, bookTitle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.parent, highlightFragment);
        ft.commit();
    }

}
