/*
 *    Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.therisingtechie.geello.data;


import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.therisingtechie.geello.data.listeners.DataListener;
import com.therisingtechie.geello.data.local.PreferencesHelper;
import com.therisingtechie.geello.data.remote.ApiHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by amitshekhar on 13/01/17.
 */
@Singleton
public class DataManager {

    private final PreferencesHelper mPreferencesHelper;
    private final ApiHelper mApiHelper;

    @Inject
    public DataManager(PreferencesHelper preferencesHelper, ApiHelper apiHelper) {
        this.mPreferencesHelper = preferencesHelper;
        this.mApiHelper = apiHelper;
    }

    public void getData(final DataListener listener) {

        final String data = mPreferencesHelper.getData();

        if (data != null) {
            listener.onResponse(data);
            return;
        }

        mApiHelper.getData(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                mPreferencesHelper.putData(response);
                listener.onResponse(response);
            }

            @Override
            public void onError(ANError anError) {
                listener.onError(anError.getErrorDetail());
            }
        });

    }
}
