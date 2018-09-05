package com.bethena.youtudemo.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.bethena.youtudemo.R;
import com.bethena.youtudemo.base.BaseActivity;
import com.bethena.youtudemo.bean.ApiResult;
import com.bethena.youtudemo.bean.ReqTag;
import com.bethena.youtudemo.bean.Tag;
import com.bethena.youtudemo.constant.Constants;
import com.bethena.youtudemo.utils.BitmapUtil;
import com.bethena.youtudemo.utils.FileUtil;
import com.bethena.youtudemo.utils.HttpUtil;
import com.google.gson.Gson;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import timber.log.Timber;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });
    }


    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            Uri uri = data.getData();
            String path = FileUtil.getPath(this, uri);
            Bitmap bitmap = BitmapUtil.getBitmap(path, 100, 100);

            final ReqTag reqTag = new ReqTag();
            reqTag.app_id = Constants.APP_ID;
            try {
                reqTag.image = BitmapUtil.bitmapToBase64(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            reqTag.seq = "1234";


            Flowable<ApiResult<Tag>> flowable = HttpUtil.getApi().imageTag(reqTag);


//            new HandlerThread("hello"){
//                @Override
//                protected void onLooperPrepared() {
//                    super.onLooperPrepared();
//                    Gson gson = new Gson();
//                    String json = gson.toJson(reqTag);
//                    OkHttpClient client = HttpUtil.getHttpClient();
//                    RequestBody requestBody = RequestBody.create(JSON, json);
//                    Request request = new Request.Builder()
//                            .url("http://api.youtu.qq.com/youtu/imageapi/imagetag")
//                            .post(requestBody)
//                            .build();
//                    try {
//                        Response response = client.newCall(request).execute();
//
//                        Timber.tag(TAG).d("body = " + response.body().string());
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }.start();




            Flowable.just(1)
                    .subscribe(new Subscriber<Integer>() {
                        @Override
                        public void onSubscribe(Subscription s) {
                            Timber.tag(TAG).d("onSubscribe");
                        }

                        @Override
                        public void onNext(Integer integer) {
                            Timber.tag(TAG).d("tagApiResult = " + integer);
                        }

                        @Override
                        public void onError(Throwable t) {
                            t.printStackTrace();
                            Timber.tag(TAG).e("error = " + t.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            Timber.tag(TAG).e("onComplete");
                        }
                    });
        }
    }


}
