/*

 This is the source code of bladeGram for Android.

 We do not and cannot prevent the use of our code,
 but be respectful and credit the original author.

 Copyright @immat0x1, 2023

*/

package com.bladegram.messenger.components;

import android.annotation.SuppressLint;
import android.content.Context;

import com.bladegram.messenger.BladeConfig;
import com.bladegram.messenger.utils.PopupUtils;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.Theme;

import java.util.Arrays;

@SuppressLint("ViewConstructor")
public class TranslateBeforeSendWrapper extends ActionBarMenuSubItem {

    public TranslateBeforeSendWrapper(Context context, boolean top, boolean bottom, Theme.ResourcesProvider resourcesProvider) {
        super(context, top, bottom, resourcesProvider);
        setTextAndIcon(LocaleController.getString("TranslateTo", R.string.TranslateTo), R.drawable.msg_translate);
        setSubtext(BladeConfig.getCurrentLangName());
        setMinimumWidth(AndroidUtilities.dp(196));
        setItemHeight(56);
        setOnClickListener(v -> onClick());
        setRightIcon(R.drawable.msg_arrowright);
        getRightIcon().setOnClickListener(v -> PopupUtils.showDialog(BladeConfig.supportedLanguages, LocaleController.getString("Language", R.string.Language), Arrays.asList(BladeConfig.supportedLanguages).indexOf(BladeConfig.targetLanguage), context, i -> {
            BladeConfig.editor.putString("targetLanguage", BladeConfig.targetLanguage = (String) BladeConfig.supportedLanguages[i]).apply();
            setSubtext(BladeConfig.getCurrentLangName());
        }));
    }

    protected void onClick() {
    }
}
