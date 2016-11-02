package com.mdxx.qmmz.network.progress;

/**
 * Created by Rays on 16/5/12.
 */
public interface IProgressIndicator {
    void showProgress();
    void showProgress(String message);
    void dismissProgress();
}
