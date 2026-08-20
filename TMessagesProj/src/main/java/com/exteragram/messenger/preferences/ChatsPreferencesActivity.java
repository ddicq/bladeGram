/*

 This is the source code of bladeGram for Android.

 We do not and cannot prevent the use of our code,
 but be respectful and credit the original author.

 Copyright @immat0x1, 2023

*/

package com.bladegram.messenger.preferences;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bladegram.messenger.BladeConfig;
import com.bladegram.messenger.preferences.components.AltSeekbar;
import com.bladegram.messenger.preferences.components.DoubleTapCell;
import com.bladegram.messenger.preferences.components.StickerShapeCell;
import com.bladegram.messenger.preferences.components.StickerSizePreviewCell;
import com.bladegram.messenger.utils.LocaleUtils;
import com.bladegram.messenger.utils.PopupUtils;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextCheckCell2;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SlideChooseView;

import java.util.Locale;

public class ChatsPreferencesActivity extends BasePreferencesActivity implements NotificationCenter.NotificationCenterDelegate {

    private ActionBarMenuItem resetItem;
    private StickerSizeCell stickerSizeCell;
    private DoubleTapCell doubleTapCell;

    private final CharSequence[] doubleTapActions = new CharSequence[]{
            LocaleController.getString("Disable", R.string.Disable),
            LocaleController.getString("Reactions", R.string.Reactions),
            LocaleController.getString("Reply", R.string.Reply),
            LocaleController.getString("Copy", R.string.Copy),
            LocaleController.getString("Forward", R.string.Forward),
            LocaleController.getString("Edit", R.string.Edit),
            LocaleController.getString("Save", R.string.Save),
            LocaleController.getString("Delete", R.string.Delete)
    }, bottomButton = new CharSequence[]{
            LocaleController.getString("Hide", R.string.Hide),
            LocaleUtils.capitalize(LocaleController.getString("ChannelMute", R.string.ChannelMute)),
            LocaleUtils.capitalize(LocaleController.getString("ChannelDiscuss", R.string.ChannelDiscuss))
    }, videoMessagesCamera = new CharSequence[]{
            LocaleController.getString("VideoMessagesCameraFront", R.string.VideoMessagesCameraFront),
            LocaleController.getString("VideoMessagesCameraRear", R.string.VideoMessagesCameraRear),
            LocaleController.getString("VideoMessagesCameraAsk", R.string.VideoMessagesCameraAsk)

    }, doubleTapSeekDuration = new CharSequence[]{
            LocaleController.formatPluralString("Seconds", 5),
            LocaleController.formatPluralString("Seconds", 10),
            LocaleController.formatPluralString("Seconds", 15),
            LocaleController.formatPluralString("Seconds", 30)
    };
    private final int[] doubleTapIcons = new int[]{
            R.drawable.msg_block,
            BladeConfig.useSolarIcons ? R.drawable.msg_reactions : R.drawable.msg_saved_14,
            R.drawable.msg_reply,
            R.drawable.msg_copy,
            R.drawable.msg_forward,
            R.drawable.msg_edit,
            R.drawable.msg_saved,
            R.drawable.msg_delete
    };

    private int stickerSizeRow;

    private int stickerShapeHeaderRow;
    private int stickerShapeRow;
    private int stickerShapeDividerRow;

    private int stickersHeaderRow;
    private int hideStickerTimeRow;
    private int unlimitedRecentStickersRow;
    private int hideCategoriesRow;
    private int stickersDividerRow;

    private int doubleTapHeaderRow;
    private int doubleTapRow;
    private int doubleTapActionOutOwnerRow;
    private int doubleTapActionRow;
    private int doubleTapReactionRow;
    private int doubleTapDividerRow;

    private int chatsHeaderRow;
    private int bottomButtonRow;
    private int disableJumpToNextChannelRow;
    private int hideKeyboardOnScrollRow;
    private int adminShortcutsRow;
    private int permissionsRow;
    private int administratorsRow;
    private int membersRow;
    private int recentActionsRow;
    private int showActionTimestampsRow;
    private int addCommaAfterMentionRow;
    private int chatsDividerRow;

    private int messagesHeaderRow;
    private int hideShareButtonRow;
    private int messageMenuRow;
    private int copyPhotoRow;
    private int saveRow;
    private int clearRow;
    private int historyRow;
    private int reportRow;
    private int detailsRow;
    private int messagesDividerRow;

    private int photosHeaderRow;
    private int photosQualityChooserRow;
    private int hideCounterRow;
    private int hideCameraTileRow;
    private int photosDividerRow;

    private int videosHeaderRow;
    private int staticZoomRow;
    private int videoMessagesCameraRow;
    private int rememberLastUsedCameraRow;
    private int pauseOnMinimizeRow;
    private int disablePlaybackRow;
    private int doubleTapSeekDurationRow;
    private int videosDividerRow;

    private boolean adminShortcutsExpanded;
    private boolean messageMenuExpanded;

    private class StickerSizeCell extends FrameLayout {

        private final StickerSizePreviewCell messagesCell;
        private final AltSeekbar seekBar;
        int startStickerSize = 4, endStickerSize = 20;
        private int lastWidth;

        public StickerSizeCell(Context context) {
            super(context);
            setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            setWillNotDraw(false);

            seekBar = new AltSeekbar(context, (float p) -> {
                BladeConfig.editor.putFloat("stickerSize", BladeConfig.stickerSize = p).apply();
                invalidate();
                if (resetItem.getVisibility() != VISIBLE) {
                    AndroidUtilities.updateViewVisibilityAnimated(resetItem, true, 0.5f, true);
                }
            }, startStickerSize, endStickerSize, LocaleController.getString("StickerSize", R.string.StickerSize), LocaleController.getString("StickerSizeLeft", R.string.StickerSizeLeft), LocaleController.getString("StickerSizeRight", R.string.StickerSizeRight));
            seekBar.setProgress((BladeConfig.stickerSize - startStickerSize) / (float) (endStickerSize - startStickerSize));
            addView(seekBar, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

            messagesCell = new StickerSizePreviewCell(context, ChatsPreferencesActivity.this, parentLayout);
            messagesCell.setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS);
            addView(messagesCell, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.LEFT | Gravity.TOP, 0, 112, 0, 0));
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int width = MeasureSpec.getSize(widthMeasureSpec);
            if (lastWidth != width) {
                lastWidth = width;
            }
        }

        @Override
        public void invalidate() {
            super.invalidate();
            lastWidth = -1;
            messagesCell.invalidate();
            seekBar.invalidate();
        }
    }

    @Override
    public View createView(Context context) {
        View fragmentView = super.createView(context);

        ActionBarMenu menu = actionBar.createMenu();
        resetItem = menu.addItem(0, R.drawable.msg_reset);
        resetItem.setContentDescription(LocaleController.getString("Reset", R.string.Reset));
        resetItem.setVisibility(BladeConfig.stickerSize != 14.0f ? View.VISIBLE : View.GONE);
        resetItem.setTag(null);
        resetItem.setOnClickListener(v -> {
            AndroidUtilities.updateViewVisibilityAnimated(resetItem, false, 0.5f, true);
            ValueAnimator animator = ValueAnimator.ofFloat(BladeConfig.stickerSize, 14.0f);
            animator.setDuration(200);
            animator.addUpdateListener(valueAnimator -> {
                BladeConfig.editor.putFloat("stickerSize", BladeConfig.stickerSize = (Float) valueAnimator.getAnimatedValue()).apply();
                stickerSizeCell.invalidate();
            });
            animator.start();
        });

        return fragmentView;
    }

    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
        return true;
    }

    @Override
    public void didReceivedNotification(int id, int account, Object... args) {
        if (id == NotificationCenter.emojiLoaded) {
            if (getListView() != null) {
                getListView().invalidateViews();
            }
        }
    }

    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
    }

    @Override
    protected void updateRowsId() {
        super.updateRowsId();

        stickerSizeRow = newRow();

        stickerShapeHeaderRow = newRow();
        stickerShapeRow = newRow();
        stickerShapeDividerRow = newRow();

        stickersHeaderRow = newRow();
        hideStickerTimeRow = newRow();
        unlimitedRecentStickersRow = newRow();
        hideCategoriesRow = newRow();
        stickersDividerRow = newRow();

        doubleTapHeaderRow = newRow();
        doubleTapRow = newRow();
        doubleTapActionRow = newRow();
        doubleTapActionOutOwnerRow = newRow();
        doubleTapReactionRow = BladeConfig.doubleTapAction == 1 || BladeConfig.doubleTapActionOutOwner == 1 ? newRow() : -1;
        doubleTapDividerRow = newRow();

        chatsHeaderRow = newRow();
        bottomButtonRow = newRow();
        adminShortcutsRow = newRow();
        if (adminShortcutsExpanded) {
            permissionsRow = newRow();
            administratorsRow = newRow();
            membersRow = newRow();
            recentActionsRow = newRow();
        } else {
            permissionsRow = -1;
            administratorsRow = -1;
            membersRow = -1;
            recentActionsRow = -1;
        }
        disableJumpToNextChannelRow = newRow();
        hideKeyboardOnScrollRow = newRow();
        addCommaAfterMentionRow = newRow();
        chatsDividerRow = newRow();

        messagesHeaderRow = newRow();
        hideShareButtonRow = newRow();
        messageMenuRow = newRow();
        if (messageMenuExpanded) {
            copyPhotoRow = newRow();
            saveRow = newRow();
            clearRow = newRow();
            historyRow = newRow();
            reportRow = newRow();
            detailsRow = newRow();
        } else {
            copyPhotoRow = -1;
            saveRow = -1;
            clearRow = -1;
            historyRow = -1;
            reportRow = -1;
            detailsRow = -1;
        }
        showActionTimestampsRow = newRow();
        messagesDividerRow = newRow();

        photosHeaderRow = newRow();
        photosQualityChooserRow = newRow();
        hideCounterRow = newRow();
        hideCameraTileRow = newRow();
        photosDividerRow = newRow();

        videosHeaderRow = newRow();
        doubleTapSeekDurationRow = newRow();
        videoMessagesCameraRow = newRow();
        rememberLastUsedCameraRow = BladeConfig.videoMessagesCamera != 2 ? newRow() : -1;
        staticZoomRow = newRow();
        pauseOnMinimizeRow = newRow();
        disablePlaybackRow = newRow();
        videosDividerRow = newRow();
    }

    @Override
    protected void onItemClick(View view, int position, float x, float y) {
        if (position == hideStickerTimeRow) {
            BladeConfig.editor.putBoolean("hideStickerTime", BladeConfig.hideStickerTime ^= true).apply();
            ((TextCheckCell) view).setChecked(BladeConfig.hideStickerTime);
            stickerSizeCell.invalidate();
        } else if (position == unlimitedRecentStickersRow) {
            BladeConfig.editor.putBoolean("unlimitedRecentStickers", BladeConfig.unlimitedRecentStickers ^= true).apply();
            ((TextCheckCell) view).setChecked(BladeConfig.unlimitedRecentStickers);
        } else if (position == hideCategoriesRow) {
            BladeConfig.editor.putBoolean("hideCategories", BladeConfig.hideCategories ^= true).apply();
            ((TextCheckCell) view).setChecked(BladeConfig.hideCategories);
        } else if (position == addCommaAfterMentionRow) {
            BladeConfig.editor.putBoolean("addCommaAfterMention", BladeConfig.addCommaAfterMention ^= true).apply();
            ((TextCheckCell) view).setChecked(BladeConfig.addCommaAfterMention);
        } else if (position == hideKeyboardOnScrollRow) {
            BladeConfig.editor.putBoolean("hideKeyboardOnScroll", BladeConfig.hideKeyboardOnScroll ^= true).apply();
            ((TextCheckCell) view).setChecked(BladeConfig.hideKeyboardOnScroll);
        } else if (position == hideShareButtonRow) {
            BladeConfig.editor.putBoolean("hideShareButton", BladeConfig.hideShareButton ^= true).apply();
            ((TextCheckCell) view).setChecked(BladeConfig.hideShareButton);
        } else if (position == bottomButtonRow) {
            if (getParentActivity() == null) {
                return;
            }
            PopupUtils.showDialog(bottomButton, LocaleController.getString("BottomButton", R.string.BottomButton), BladeConfig.bottomButton, getContext(), which -> {
                BladeConfig.editor.putInt("bottomButton", BladeConfig.bottomButton = which).apply();
                listAdapter.notifyItemChanged(bottomButtonRow, payload);
            });
        } else if (position == disableJumpToNextChannelRow) {
            BladeConfig.editor.putBoolean("disableJumpToNextChannel", BladeConfig.disableJumpToNextChannel ^= true).apply();
            ((TextCheckCell) view).setChecked(BladeConfig.disableJumpToNextChannel);
            parentLayout.rebuildAllFragmentViews(false, false);
        } else if (position == showActionTimestampsRow) {
            BladeConfig.editor.putBoolean("showActionTimestamps", BladeConfig.showActionTimestamps ^= true).apply();
            ((TextCheckCell) view).setChecked(BladeConfig.showActionTimestamps);
            listAdapter.notifyItemChanged(messagesDividerRow);
            parentLayout.rebuildAllFragmentViews(false, false);
        } else if (position == staticZoomRow) {
            BladeConfig.editor.putBoolean("staticZoom", BladeConfig.staticZoom ^= true).apply();
            ((TextCheckCell) view).setChecked(BladeConfig.staticZoom);
        } else if (position == videoMessagesCameraRow) {
            if (getParentActivity() == null) {
                return;
            }
            PopupUtils.showDialog(videoMessagesCamera, LocaleController.getString("VideoMessagesCamera", R.string.VideoMessagesCamera), BladeConfig.videoMessagesCamera, getContext(), which -> {
                int old = BladeConfig.videoMessagesCamera;
                BladeConfig.editor.putInt("videoMessagesCamera", BladeConfig.videoMessagesCamera = which).apply();
                if (old == which) {
                    return;
                }
                if (old == 2 && BladeConfig.videoMessagesCamera != 2) {
                    updateRowsId();
                    listAdapter.notifyItemInserted(rememberLastUsedCameraRow);
                } else if (old != 2 && BladeConfig.videoMessagesCamera == 2) {
                    listAdapter.notifyItemRemoved(rememberLastUsedCameraRow);
                    updateRowsId();
                }
                listAdapter.notifyItemChanged(videoMessagesCameraRow, payload);
            });
        } else if (position == rememberLastUsedCameraRow) {
            BladeConfig.editor.putBoolean("rememberLastUsedCamera", BladeConfig.rememberLastUsedCamera ^= true).apply();
            ((TextCheckCell) view).setChecked(BladeConfig.rememberLastUsedCamera);
        } else if (position == hideCameraTileRow) {
            BladeConfig.editor.putBoolean("hideCameraTile", BladeConfig.hideCameraTile ^= true).apply();
            ((TextCheckCell) view).setChecked(BladeConfig.hideCameraTile);
        } else if (position == pauseOnMinimizeRow) {
            BladeConfig.editor.putBoolean("pauseOnMinimize", BladeConfig.pauseOnMinimize ^= true).apply();
            ((TextCheckCell) view).setChecked(BladeConfig.pauseOnMinimize);
        } else if (position == disablePlaybackRow) {
            BladeConfig.editor.putBoolean("disablePlayback", BladeConfig.disablePlayback ^= true).apply();
            ((TextCheckCell) view).setChecked(BladeConfig.disablePlayback);
            showBulletin();
        } else if (position == doubleTapSeekDurationRow) {
            if (getParentActivity() == null) {
                return;
            }
            PopupUtils.showDialog(doubleTapSeekDuration, LocaleController.getString("DoubleTapSeekDuration", R.string.DoubleTapSeekDuration), BladeConfig.doubleTapSeekDuration, getContext(), which -> {
                int old = BladeConfig.doubleTapSeekDuration;
                BladeConfig.editor.putInt("doubleTapSeekDuration", BladeConfig.doubleTapSeekDuration = which).apply();
                if (old == which) {
                    return;
                }
                updateRowsId();
                listAdapter.notifyItemChanged(doubleTapSeekDurationRow, payload);
            });

        } else if (position == hideCounterRow) {
            BladeConfig.editor.putBoolean("hidePhotoCounter", BladeConfig.hidePhotoCounter ^= true).apply();
            ((TextCheckCell) view).setChecked(BladeConfig.hidePhotoCounter);
            parentLayout.rebuildAllFragmentViews(false, false);
        } else if (position == doubleTapActionRow || position == doubleTapActionOutOwnerRow) {
            if (getParentActivity() == null) {
                return;
            }
            PopupUtils.showDialog(doubleTapActions, doubleTapIcons, LocaleController.getString("DoubleTap", R.string.DoubleTap), position == doubleTapActionRow ? BladeConfig.doubleTapAction : BladeConfig.doubleTapActionOutOwner, getContext(), i -> {
                if (position == doubleTapActionOutOwnerRow) {
                    int old = BladeConfig.doubleTapActionOutOwner;
                    if (old == i)
                        return;
                    doubleTapCell.updateIcons(2, true);
                    BladeConfig.editor.putInt("doubleTapActionOutOwner", BladeConfig.doubleTapActionOutOwner = i).apply();
                    if (old == 1 && BladeConfig.doubleTapAction != 1) {
                        listAdapter.notifyItemRemoved(doubleTapReactionRow);
                        updateRowsId();
                    } else if (i == 1 && BladeConfig.doubleTapAction != 1) {
                        updateRowsId();
                        listAdapter.notifyItemInserted(doubleTapReactionRow);
                    }
                    listAdapter.notifyItemChanged(doubleTapActionOutOwnerRow, payload);
                } else {
                    int old = BladeConfig.doubleTapAction;
                    if (old == i) return;
                    doubleTapCell.updateIcons(1, true);
                    BladeConfig.editor.putInt("doubleTapAction", BladeConfig.doubleTapAction = i).apply();
                    if (old == 1 && BladeConfig.doubleTapActionOutOwner != 1) {
                        listAdapter.notifyItemRemoved(doubleTapReactionRow);
                        updateRowsId();
                    } else if (i == 1 && BladeConfig.doubleTapActionOutOwner != 1) {
                        updateRowsId();
                        listAdapter.notifyItemInserted(doubleTapReactionRow);
                    }
                    listAdapter.notifyItemChanged(doubleTapActionOutOwnerRow);
                    listAdapter.notifyItemChanged(doubleTapActionRow, payload);
                }
            });
        } else if (position == doubleTapReactionRow) {
            if (view.getY() >= listView.getBottom() / 3f) {
                listView.smoothScrollBy(0, (int) Math.abs(view.getY()));
            }
            DoubleTapCell.SetReactionCell.showSelectStatusDialog((DoubleTapCell.SetReactionCell) view, this);
        } else if (position == adminShortcutsRow) {
            adminShortcutsExpanded ^= true;
            updateRowsId();
            listAdapter.notifyItemChanged(adminShortcutsRow, payload);
            if (adminShortcutsExpanded) {
                listAdapter.notifyItemRangeInserted(adminShortcutsRow + 1, 4);
            } else {
                listAdapter.notifyItemRangeRemoved(adminShortcutsRow + 1, 4);
            }
        } else if (position == messageMenuRow) {
            messageMenuExpanded ^= true;
            updateRowsId();
            listAdapter.notifyItemChanged(messageMenuRow, payload);
            if (messageMenuExpanded) {
                listAdapter.notifyItemRangeInserted(messageMenuRow + 1, 6);
            } else {
                listAdapter.notifyItemRangeRemoved(messageMenuRow + 1, 6);
            }
        } else if (position >= permissionsRow && position <= recentActionsRow) {
            if (position == permissionsRow) {
                BladeConfig.editor.putBoolean("permissionsShortcut", BladeConfig.permissionsShortcut ^= true).apply();
                listAdapter.notifyItemChanged(permissionsRow, payload);
            } else if (position == administratorsRow) {
                BladeConfig.editor.putBoolean("administratorsShortcut", BladeConfig.administratorsShortcut ^= true).apply();
                listAdapter.notifyItemChanged(administratorsRow, payload);
            } else if (position == membersRow) {
                BladeConfig.editor.putBoolean("membersShortcut", BladeConfig.membersShortcut ^= true).apply();
                listAdapter.notifyItemChanged(membersRow, payload);
            } else if (position == recentActionsRow) {
                BladeConfig.editor.putBoolean("recentActionsShortcut", BladeConfig.recentActionsShortcut ^= true).apply();
                listAdapter.notifyItemChanged(recentActionsRow, payload);
            }
            listAdapter.notifyItemChanged(adminShortcutsRow, payload);
        } else if (position >= copyPhotoRow && position <= detailsRow) {
            if (position == copyPhotoRow) {
                BladeConfig.editor.putBoolean("showCopyPhotoButton", BladeConfig.showCopyPhotoButton ^= true).apply();
                listAdapter.notifyItemChanged(copyPhotoRow, payload);
            } else if (position == clearRow) {
                BladeConfig.editor.putBoolean("showClearButton", BladeConfig.showClearButton ^= true).apply();
                listAdapter.notifyItemChanged(clearRow, payload);
            } else if (position == saveRow) {
                BladeConfig.editor.putBoolean("showSaveMessageButton", BladeConfig.showSaveMessageButton ^= true).apply();
                listAdapter.notifyItemChanged(saveRow, payload);
            } else if (position == reportRow) {
                BladeConfig.editor.putBoolean("showReportButton", BladeConfig.showReportButton ^= true).apply();
                listAdapter.notifyItemChanged(reportRow, payload);
            } else if (position == historyRow) {
                BladeConfig.editor.putBoolean("showHistoryButton", BladeConfig.showHistoryButton ^= true).apply();
                listAdapter.notifyItemChanged(historyRow, payload);
            } else if (position == detailsRow) {
                BladeConfig.editor.putBoolean("showDetailsButton", BladeConfig.showDetailsButton ^= true).apply();
                listAdapter.notifyItemChanged(detailsRow, payload);
            }
            listAdapter.notifyItemChanged(messageMenuRow, payload);
        }
    }

    @Override
    protected String getTitle() {
        return LocaleController.getString("SearchAllChatsShort", R.string.SearchAllChatsShort);
    }

    @Override
    protected BaseListAdapter createAdapter(Context context) {
        return new ListAdapter(context);
    }

    private void setShortcutsEnabled(boolean enabled) {
        BladeConfig.editor.putBoolean("permissionsShortcut", BladeConfig.permissionsShortcut = enabled).apply();
        BladeConfig.editor.putBoolean("administratorsShortcut", BladeConfig.administratorsShortcut = enabled).apply();
        BladeConfig.editor.putBoolean("membersShortcut", BladeConfig.membersShortcut = enabled).apply();
        BladeConfig.editor.putBoolean("recentActionsShortcut", BladeConfig.recentActionsShortcut = enabled).apply();
        AndroidUtilities.updateVisibleRows(listView);
    }

    private int getShortcutsSelectedCount() {
        int i = 0;
        if (BladeConfig.permissionsShortcut)
            i++;
        if (BladeConfig.administratorsShortcut)
            i++;
        if (BladeConfig.membersShortcut)
            i++;
        if (BladeConfig.recentActionsShortcut)
            i++;
        return i;
    }

    private void setMessageMenuEnabled(boolean enabled) {
        BladeConfig.editor.putBoolean("showCopyPhotoButton", BladeConfig.showCopyPhotoButton = enabled).apply();
        BladeConfig.editor.putBoolean("showClearButton", BladeConfig.showClearButton = enabled).apply();
        BladeConfig.editor.putBoolean("showSaveMessageButton", BladeConfig.showSaveMessageButton = enabled).apply();
        BladeConfig.editor.putBoolean("showReportButton", BladeConfig.showReportButton = enabled).apply();
        BladeConfig.editor.putBoolean("showHistoryButton", BladeConfig.showHistoryButton = enabled).apply();
        BladeConfig.editor.putBoolean("showDetailsButton", BladeConfig.showDetailsButton = enabled).apply();
        AndroidUtilities.updateVisibleRows(listView);
    }

    private int getMessageMenuSelectedCount() {
        int i = 0;
        if (BladeConfig.showCopyPhotoButton)
            i++;
        if (BladeConfig.showSaveMessageButton)
            i++;
        if (BladeConfig.showClearButton)
            i++;
        if (BladeConfig.showReportButton)
            i++;
        if (BladeConfig.showHistoryButton)
            i++;
        if (BladeConfig.showDetailsButton)
            i++;
        return i;
    }

    private class ListAdapter extends BaseListAdapter {

        public ListAdapter(Context context) {
            super(context);
        }

        @Override
        public int getItemCount() {
            return rowCount;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
            switch (type) {
                case 10:
                    StickerShapeCell stickerShapeCell = new StickerShapeCell(mContext) {
                        @Override
                        protected void updateStickerPreview() {
                            parentLayout.rebuildAllFragmentViews(false, false);
                            stickerSizeCell.invalidate();
                        }
                    };
                    stickerShapeCell.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
                    return new RecyclerListView.Holder(stickerShapeCell);
                case 11:
                    stickerSizeCell = new StickerSizeCell(mContext);
                    stickerSizeCell.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
                    return new RecyclerListView.Holder(stickerSizeCell);
                case 15:
                    doubleTapCell = new DoubleTapCell(mContext);
                    doubleTapCell.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
                    return new RecyclerListView.Holder(doubleTapCell);
                case 16:
                    DoubleTapCell.SetReactionCell reactionCell = new DoubleTapCell.SetReactionCell(mContext);
                    reactionCell.update(false);
                    reactionCell.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
                    return new RecyclerListView.Holder(reactionCell);
                default:
                    return super.onCreateViewHolder(parent, type);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, boolean payload) {
            switch (holder.getItemViewType()) {
                case 1:
                    holder.itemView.setBackground(Theme.getThemedDrawable(mContext, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                    break;
                case 3:
                    HeaderCell headerCell = (HeaderCell) holder.itemView;
                    if (position == stickersHeaderRow) {
                        headerCell.setText(LocaleController.getString(R.string.StickersName));
                    } else if (position == chatsHeaderRow) {
                        headerCell.setText(LocaleController.getString("GroupsAndChannelsLimitTitle", R.string.GroupsAndChannelsLimitTitle));
                    } else if (position == messagesHeaderRow) {
                        headerCell.setText(LocaleController.getString("MessagesChartTitle", R.string.MessagesChartTitle));
                    } else if (position == videosHeaderRow) {
                        headerCell.setText(LocaleController.getString("AutoDownloadVideos", R.string.AutoDownloadVideos));
                    } else if (position == stickerShapeHeaderRow) {
                        headerCell.setText(LocaleController.getString("StickerShape", R.string.StickerShape));
                    } else if (position == doubleTapHeaderRow) {
                        headerCell.setText(LocaleController.getString("DoubleTap", R.string.DoubleTap));
                    } else if (position == photosHeaderRow) {
                        headerCell.setText(LocaleController.getString("AutoDownloadPhotos", R.string.AutoDownloadPhotos));
                    }
                    break;
                case 5:
                    TextCheckCell textCheckCell = (TextCheckCell) holder.itemView;
                    textCheckCell.setEnabled(true, null);
                    if (position == hideStickerTimeRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("StickerTime", R.string.StickerTime), BladeConfig.hideStickerTime, true);
                    } else if (position == unlimitedRecentStickersRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("UnlimitedRecentStickers", R.string.UnlimitedRecentStickers), BladeConfig.unlimitedRecentStickers, true);
                    } else if (position == hideCategoriesRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("HideCategories", R.string.HideCategories), BladeConfig.hideCategories, false);
                    } else if (position == addCommaAfterMentionRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("AddCommaAfterMention", R.string.AddCommaAfterMention), BladeConfig.addCommaAfterMention, false);
                    } else if (position == hideKeyboardOnScrollRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("HideKeyboardOnScroll", R.string.HideKeyboardOnScroll), BladeConfig.hideKeyboardOnScroll, true);
                    } else if (position == hideShareButtonRow) {
                        textCheckCell.setTextAndCheck(LocaleController.formatString("HideShareButton", R.string.HideShareButton, LocaleController.getString("ShareFile", R.string.ShareFile)), BladeConfig.hideShareButton, true);
                    } else if (position == disableJumpToNextChannelRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("DisableJumpToNextChannel", R.string.DisableJumpToNextChannel), BladeConfig.disableJumpToNextChannel, true);
                    } else if (position == showActionTimestampsRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("ShowActionTimestamps", R.string.ShowActionTimestamps), BladeConfig.showActionTimestamps, false);
                    } else if (position == staticZoomRow) {
                        textCheckCell.setTextAndValueAndCheck(LocaleController.getString("StaticZoom", R.string.StaticZoom), LocaleController.getString("StaticZoomInfo", R.string.StaticZoomInfo), BladeConfig.staticZoom, true, true);
                    } else if (position == rememberLastUsedCameraRow) {
                        textCheckCell.setTextAndValueAndCheck(LocaleController.getString("RememberLastUsedCamera", R.string.RememberLastUsedCamera), LocaleController.getString("RememberLastUsedCameraInfo", R.string.RememberLastUsedCameraInfo), BladeConfig.rememberLastUsedCamera, true, true);
                    } else if (position == hideCameraTileRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("HideCameraTile", R.string.HideCameraTile), BladeConfig.hideCameraTile, false);
                    } else if (position == pauseOnMinimizeRow) {
                        textCheckCell.setTextAndValueAndCheck(LocaleController.getString("PauseOnMinimize", R.string.PauseOnMinimize), LocaleController.getString("PauseOnMinimizeInfo", R.string.PauseOnMinimizeInfo), BladeConfig.pauseOnMinimize, true, true);
                    } else if (position == disablePlaybackRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("DisablePlayback", R.string.DisablePlayback), BladeConfig.disablePlayback, false);
                    } else if (position == hideCounterRow) {
                        textCheckCell.setTextAndValueAndCheck(LocaleController.getString("HidePhotoCounter", R.string.HidePhotoCounter), LocaleController.getString("HidePhotoCounterInfo", R.string.HidePhotoCounterInfo), BladeConfig.hidePhotoCounter, true, true);
                    }
                    break;
                case 7:
                    TextSettingsCell textSettingsCell = (TextSettingsCell) holder.itemView;
                    if (position == doubleTapActionOutOwnerRow) {
                        textSettingsCell.setTextAndValue(LocaleController.getString("DoubleTapOutgoing", R.string.DoubleTapOutgoing), doubleTapActions[BladeConfig.doubleTapActionOutOwner], payload, doubleTapReactionRow != -1);
                    } else if (position == doubleTapActionRow) {
                        textSettingsCell.setTextAndValue(LocaleController.getString("DoubleTapIncoming", R.string.DoubleTapIncoming), doubleTapActions[BladeConfig.doubleTapAction], payload, true);
                    } else if (position == bottomButtonRow) {
                        textSettingsCell.setTextAndValue(LocaleController.getString("BottomButton", R.string.BottomButton), LocaleUtils.capitalize((String) bottomButton[BladeConfig.bottomButton]), payload, true);
                    } else if (position == videoMessagesCameraRow) {
                        textSettingsCell.setTextAndValue(LocaleController.getString("VideoMessagesCamera", R.string.VideoMessagesCamera), videoMessagesCamera[BladeConfig.videoMessagesCamera], payload, true);
                    } else if (position == doubleTapSeekDurationRow) {
                        textSettingsCell.setTextAndValue(LocaleController.getString("DoubleTapSeekDuration", R.string.DoubleTapSeekDuration), doubleTapSeekDuration[BladeConfig.doubleTapSeekDuration], payload, true);
                    }
                    break;
                case 8:
                    TextInfoPrivacyCell cell = (TextInfoPrivacyCell) holder.itemView;
                    if (position == doubleTapDividerRow) {
                        cell.setText(LocaleController.getString("DoubleTapInfo", R.string.DoubleTapInfo));
                    } else if (position == photosDividerRow) {
                        cell.setText(LocaleController.getString("HideCameraTileInfo", R.string.HideCameraTileInfo));
                    } else if (position == chatsDividerRow) {
                        cell.setText(LocaleUtils.formatWithUsernames(LocaleController.getString("AddCommaAfterMentionInfo", R.string.AddCommaAfterMentionInfo), ChatsPreferencesActivity.this));
                    } else if (position == stickersDividerRow) {
                        cell.setText(LocaleController.getString("HideCategoriesInfo", R.string.HideCategoriesInfo));
                    } else if (position == videosDividerRow) {
                        cell.setText(LocaleController.getString("DisablePlaybackInfo", R.string.DisablePlaybackInfo));
                    } else if (position == messagesDividerRow) {
                        cell.getTextView().setMovementMethod(null);
                        String value = LocaleController.getString("EventLogGroupJoined", R.string.EventLogGroupJoined);
                        value = value.replace("un1", "**immat0x1**");
                        if (BladeConfig.showActionTimestamps)
                            value += " " + LocaleController.formatString("TodayAtFormatted", R.string.TodayAtFormatted, "12:34");
                        cell.setText(AndroidUtilities.replaceTags(value));
                    }
                    break;
                case 13:
                    SlideChooseView slide = (SlideChooseView) holder.itemView;
                    if (position == photosQualityChooserRow) {
                        slide.setNeedDivider(true);
                        slide.setCallback(index -> BladeConfig.editor.putInt("sendPhotosQuality", BladeConfig.sendPhotosQuality = index).apply());
                        slide.setOptions(BladeConfig.sendPhotosQuality, "800px", "1280px", "2560px");
                    }
                    break;
                case 18:
                    TextCheckCell2 checkCell = (TextCheckCell2) holder.itemView;
                    if (position == adminShortcutsRow) {
                        int shortcutsSelectedCount = getShortcutsSelectedCount();
                        checkCell.setTextAndCheck(LocaleController.getString("AdminShortcuts", R.string.AdminShortcuts), shortcutsSelectedCount > 0, true, true);
                        checkCell.setCollapseArrow(String.format(Locale.US, "%d/4", shortcutsSelectedCount), !adminShortcutsExpanded, () -> {
                            boolean checked = !checkCell.isChecked();
                            checkCell.setChecked(checked);
                            setShortcutsEnabled(checked);
                        });
                    } else if (position == messageMenuRow) {
                        int messageMenuSelectedCount = getMessageMenuSelectedCount();
                        checkCell.setTextAndCheck(LocaleController.getString("MessageMenu", R.string.MessageMenu), messageMenuSelectedCount > 0, true, true);
                        checkCell.setCollapseArrow(String.format(Locale.US, "%d/6", messageMenuSelectedCount), !messageMenuExpanded, () -> {
                            boolean checked = !checkCell.isChecked();
                            checkCell.setChecked(checked);
                            setMessageMenuEnabled(checked);
                        });
                    }
                    checkCell.getCheckBox().setColors(Theme.key_switchTrack, Theme.key_switchTrackChecked, Theme.key_windowBackgroundWhite, Theme.key_windowBackgroundWhite);
                    checkCell.getCheckBox().setDrawIconType(0);
                    break;
                case 19:
                    CheckBoxCell checkBoxCell = (CheckBoxCell) holder.itemView;
                    if (position == permissionsRow) {
                        checkBoxCell.setText(LocaleController.getString("ChannelPermissions", R.string.ChannelPermissions), "", BladeConfig.permissionsShortcut, true, true);
                    } else if (position == administratorsRow) {
                        checkBoxCell.setText(LocaleController.getString("ChannelAdministrators", R.string.ChannelAdministrators), "", BladeConfig.administratorsShortcut, true, true);
                    } else if (position == membersRow) {
                        checkBoxCell.setText(LocaleController.getString("ChannelMembers", R.string.ChannelMembers), "", BladeConfig.membersShortcut, true, true);
                    } else if (position == recentActionsRow) {
                        checkBoxCell.setText(LocaleController.getString("EventLog", R.string.EventLog), "", BladeConfig.recentActionsShortcut, true, true);
                    } else if (position == copyPhotoRow) {
                        checkBoxCell.setText(LocaleController.getString("CopyPhoto", R.string.CopyPhoto), "", BladeConfig.showCopyPhotoButton, true, true);
                    } else if (position == clearRow) {
                        checkBoxCell.setText(LocaleController.getString("Clear", R.string.Clear), "", BladeConfig.showClearButton, true, true);
                    } else if (position == saveRow) {
                        checkBoxCell.setText(LocaleController.getString("Save", R.string.Save), "", BladeConfig.showSaveMessageButton, true, true);
                    } else if (position == reportRow) {
                        checkBoxCell.setText(LocaleController.getString("ReportChat", R.string.ReportChat), "", BladeConfig.showReportButton, true, true);
                    } else if (position == historyRow) {
                        checkBoxCell.setText(LocaleController.getString("MessageHistory", R.string.MessageHistory), "", BladeConfig.showHistoryButton, true, true);
                    } else if (position == detailsRow) {
                        checkBoxCell.setText(LocaleController.getString("Details", R.string.Details), "", BladeConfig.showDetailsButton, true, true);
                    }
                    checkBoxCell.setPad(1);
                    break;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == stickerShapeDividerRow) {
                return 1;
            } else if (position == stickersHeaderRow || position == chatsHeaderRow || position == videosHeaderRow || position == stickerShapeHeaderRow ||
                    position == doubleTapHeaderRow || position == photosHeaderRow || position == messagesHeaderRow) {
                return 3;
            } else if (position == doubleTapActionRow || position == doubleTapActionOutOwnerRow || position == bottomButtonRow || position == videoMessagesCameraRow || position == doubleTapSeekDurationRow) {
                return 7;
            } else if (position == doubleTapDividerRow || position == photosDividerRow || position == chatsDividerRow || position == stickersDividerRow || position == videosDividerRow || position == messagesDividerRow) {
                return 8;
            } else if (position == stickerShapeRow) {
                return 10;
            } else if (position == stickerSizeRow) {
                return 11;
            } else if (position == photosQualityChooserRow) {
                return 13;
            } else if (position == doubleTapRow) {
                return 15;
            } else if (position == doubleTapReactionRow) {
                return 16;
            } else if (position == adminShortcutsRow || position == messageMenuRow) {
                return 18;
            } else if (position >= permissionsRow && position <= recentActionsRow || position >= copyPhotoRow && position <= detailsRow) {
                return 19;
            }
            return 5;
        }
    }
}