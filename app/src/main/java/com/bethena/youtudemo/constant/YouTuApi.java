package com.bethena.youtudemo.constant;

import com.bethena.youtudemo.bean.ApiResult;
import com.bethena.youtudemo.bean.ReqTag;
import com.bethena.youtudemo.bean.Tag;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface YouTuApi {
    @POST("/youtu/imageapi/imagetag")
    Flowable<ApiResult<Tag>> imageTag(@Body ReqTag reqTag);
}
