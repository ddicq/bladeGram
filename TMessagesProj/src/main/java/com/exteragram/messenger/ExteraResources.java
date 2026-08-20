/*

 This is the source code of bladeGram for Android.

 We do not and cannot prevent the use of our code,
 but be respectful and credit the original author.

 Copyright @immat0x1, 2023

*/

package com.bladegram.messenger;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bladegram.messenger.icons.BaseIconSet;

public class BladeResources extends Resources {

    private final Resources mResources;
    private BaseIconSet current = BladeConfig.getIconPack();

    public void getActiveIconPack() {
        current = BladeConfig.getIconPack();
    }

    public BladeResources(@NonNull Resources resources) {
        super(resources.getAssets(), resources.getDisplayMetrics(), resources.getConfiguration());
        mResources = resources;
    }

    @Nullable
    @Override
    public Drawable getDrawableForDensity(int id, int density, @Nullable Theme theme) {
        return mResources.getDrawableForDensity(current.getIcon(id), density, theme);
    }

}
