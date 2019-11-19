package com.zhangqiang.permissionrequest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.zhangqiang.holderfragment.HolderFragment;
import com.zhangqiang.holderfragment.SimpleLifecycleCallback;

public class PermissionRequestHelper {

    private static final RequestCodeGetter mRequestCodeGetter = new RequestCodeGetter();


    public static void requestPermission(FragmentActivity activity, final String[] permissions, final Callback callback) {

        final int requestCode = mRequestCodeGetter.getRequestCode();
        final HolderFragment holderFragment = HolderFragment.forActivity(activity);
        holderFragment.registerPermissionResultCallback(new HolderFragment.OnPermissionsResultCallback() {
            @Override
            public void onPermissionsResult(int requestCode2, @NonNull String[] permissions, @NonNull int[] grantResults) {

                if (requestCode == requestCode2) {
                    holderFragment.unregisterPermissionResultCallback(this);
                    if (callback != null) {
                        callback.onPermissionsResult(permissions,grantResults);
                    }
                }
            }
        });
        if (holderFragment.getActivity() == null) {
            holderFragment.registerLifecycleCallback(new SimpleLifecycleCallback() {
                @Override
                public void onAttach(Context context) {
                    super.onAttach(context);
                    holderFragment.unregisterLifecycleCallback(this);
                    holderFragment.requestPermissions(permissions, requestCode);
                }
            });
        } else {
            holderFragment.requestPermissions(permissions, requestCode);
        }
    }


    private static class RequestCodeGetter {

        private int mRequestCode = 0;

        private int getRequestCode() {
            final int targetRequestCode = mRequestCode;
            if (mRequestCode == Integer.MAX_VALUE) {
                mRequestCode = 0;
            } else {
                mRequestCode++;
            }
            return targetRequestCode;
        }

    }


    public interface Callback{

        void onPermissionsResult(@NonNull String[] permissions, @NonNull int[] grantResults);
    }
}
