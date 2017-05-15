package com.oureda.thunder.pobooks.listener;

/**
 * Created by thunder on 17-5-2.
 */

public interface OnBookStatusChangeListen {
    void onChapterChanged(int chapter);

    void onPageChanged(int chapter, int page);

    void onLoadChapterFailure(int chapter);

    void onCenterClick();

    void onFlip();
}
