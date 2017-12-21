package com.taiji.fxsqjw.views.activities;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by lightkin on 17-6-29.
 */

public class BaseFragment extends Fragment {

    private Activity activity;

    public Context getContext(){
        if(activity == null){
            return BaseApplication.getInstance();
        }
        return activity;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activity = getActivity();
    }
}
