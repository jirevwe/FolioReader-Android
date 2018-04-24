package com.folioreader;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by mahavir on 4/12/16.
 */
public class Config implements Parcelable {
    public static final String INTENT_CONFIG = "config";
    public static final String CONFIG_FONT = "font";
    public static final String CONFIG_FONT_SIZE = "font_size";
    public static final String CONFIG_IS_COLOR_MODE = "color_mode";
    public static final String CONFIG_IS_THEME_COLOR = "theme_color";
    public static final String CONFIG_IS_TTS = "is_tts";
    public static final String INTENT_PORT = "port";
    public static final Creator<Config> CREATOR = new Creator<Config>() {
        @Override
        public Config createFromParcel(Parcel source) {
            return new Config(source);
        }

        @Override
        public Config[] newArray(int size) {
            return new Config[size];
        }
    };
    private int font;
    private int fontSize;
    private int themeColor;
    private boolean showTts;
    private ColorMode colorMode;

    public Config(int font, int fontSize, ColorMode colorMode, int iconColor, boolean showTts) {
        this.font = font;
        this.fontSize = fontSize;
        this.colorMode = colorMode;
        this.themeColor = iconColor;
        this.showTts = showTts;
    }

    private Config(ConfigBuilder configBuilder) {
        font = configBuilder.mFont;
        fontSize = configBuilder.mFontSize;
        colorMode = configBuilder.colorMode;
        themeColor = configBuilder.mThemeColor;
        showTts = configBuilder.mShowTts;
    }

    public Config(JSONObject jsonObject) {
        font = jsonObject.optInt(CONFIG_FONT);
        fontSize = jsonObject.optInt(CONFIG_FONT_SIZE);
        colorMode = ColorMode.values()[jsonObject.optInt(CONFIG_IS_COLOR_MODE)];
        themeColor = jsonObject.optInt(CONFIG_IS_THEME_COLOR);
        showTts = jsonObject.optBoolean(CONFIG_IS_TTS);
    }

    private Config() {
        fontSize = 2;
        font = 3;
        colorMode = ColorMode.white;
        themeColor = R.color.app_green;
        showTts = true;
    }

    protected Config(Parcel in) {
        this.font = in.readInt();
        this.fontSize = in.readInt();
        this.themeColor = in.readInt();
        this.showTts = in.readByte() != 0;
        int tmpColorMode = in.readInt();
        this.colorMode = tmpColorMode == -1 ? null : ColorMode.values()[tmpColorMode];
    }

    public ColorMode getColorMode() {
        return colorMode;
    }

    public void setColorMode(ColorMode colorMode) {
        this.colorMode = colorMode;
    }

    public int getFont() {
        return font;
    }

    public void setFont(int font) {
        this.font = font;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(int themeColor) {
        this.themeColor = themeColor;
    }

    public boolean isShowTts() {
        return showTts;
    }

    public void setShowTts(boolean showTts) {
        this.showTts = showTts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Config)) return false;

        Config config = (Config) o;

        return font == config.font && fontSize == config.fontSize && colorMode == config.colorMode;
    }

    @Override
    public int hashCode() {
        int result = font;
        result = 31 * result
                + fontSize;
        result = 31 * result
                + colorMode.getValue();
        return result;
    }

    @Override
    public String toString() {
        return "Config{"
                + "font=" + font
                + ", fontSize=" + fontSize
                + ", colorMode=" + colorMode.toString()
                + '}';
    }

    private void readFromParcel(Parcel in) {
        font = in.readInt();
        fontSize = in.readInt();
        themeColor = in.readInt();
        showTts = in.readInt() == 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.font);
        dest.writeInt(this.fontSize);
        dest.writeInt(this.themeColor);
        dest.writeByte(this.showTts ? (byte) 1 : (byte) 0);
        dest.writeInt(this.colorMode == null ? -1 : this.colorMode.getValue());
    }

    public enum ColorMode {
        white(0), black(1), beige(2);

        private final int id;

        ColorMode(int id) {
            this.id = id;
        }

        public int getValue() {
            return id;
        }
    }

    public static class ConfigBuilder {
        private int mFont = 3;
        private int mFontSize = 2;
        private ColorMode colorMode = ColorMode.white;
        private int mThemeColor = R.color.app_green;
        private boolean mShowTts = true;

        public ConfigBuilder font(int font) {
            mFont = font;
            return this;
        }

        public ConfigBuilder fontSize(int fontSize) {
            mFontSize = fontSize;
            return this;
        }

        public ConfigBuilder colorMode(ColorMode colorMode) {
            this.colorMode = colorMode;
            return this;
        }

        public ConfigBuilder themeColor(int themeColor) {
            mThemeColor = themeColor;
            return this;
        }

        public ConfigBuilder setShowTts(boolean showTts) {
            mShowTts = showTts;
            return this;
        }


        public Config build() {
            return new Config(this);
        }
    }
}


