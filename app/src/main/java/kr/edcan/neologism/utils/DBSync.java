package kr.edcan.neologism.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.realm.Realm;
import kr.edcan.neologism.model.DicDBData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by JunseokOh on 2016. 9. 19..
 */
public class DBSync {
    static Realm realm;
    static NetworkInterface service;
    static Call<ResponseBody> getWordList;
    static Call<String> getCurrentVersion;
    static int currentVersion;

    static void convertDBfromJson(JSONArray array) {
        realm.beginTransaction();
        realm.where(DicDBData.class).findAll().deleteAllFromRealm();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject content = array.getJSONObject(i);
                DicDBData data = realm.createObject(DicDBData.class);
                data.setContents(content.getString("id"), content.getString("word"), content.getString("mean"), content.getString("ex"));
            } catch (JSONException e) {
                Log.e("asdf", e.getMessage());
                e.printStackTrace();
            }
        }
        realm.commitTransaction();
    }

    public DBSync() {
    }

    public static void syncDB(Context c) {
        final DataManager manager = new DataManager(c);
        realm = Realm.getDefaultInstance();
        service = NetworkHelper.getNetworkInstance();
        currentVersion = manager.getCurrentDatabaseVersion();
        getCurrentVersion = service.getDataBaseVersion();
        getCurrentVersion.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                int remoteDatabaseVersion = Integer.parseInt(response.body());
                Log.e("asdf", "Current Version : " + currentVersion + " DataBase Version : " + remoteDatabaseVersion);
                if (remoteDatabaseVersion != currentVersion) {
                    manager.saveCurrentDatabaseVersion(remoteDatabaseVersion);
                    getWordList = service.getWordList();
                    getWordList.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            switch (response.code()) {
                                case 200:
                                    try {
                                        convertDBfromJson(new JSONArray(response.body().string()));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case 401:
                                    break;
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("asdf", t.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("asdf", t.getMessage());
            }
        });
    }
}