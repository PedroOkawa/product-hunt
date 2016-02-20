package com.okawa.pedro.producthunt.network;

import com.google.gson.JsonObject;
import com.okawa.pedro.producthunt.model.CategoryResponse;
import com.okawa.pedro.producthunt.model.PostResponse;

import java.util.List;
import java.util.Map;

import greendao.Category;
import greendao.Session;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by pokawa on 19/02/16.
 */
public interface ApiInterface {

    String URL = "https://api.producthunt.com/";
    String VERSION = "v1/";
    String BASE_URL = URL.concat(VERSION);

    String PATH_TOKEN = "oauth/token";
    String FIELD_CLIENT_ID = "client_id";
    String FIELD_CLIENT_SECRET = "client_secret";
    String FIELD_GRANT_TYPE = "grant_type";

    String FIELD_AUTHORIZATION = "Authorization";

    String PATH_CATEGORY = "categories";

    String PATH_POSTS = "posts/all";
    String PATH_CATEGORY_ID = "categoryId";
    String FIELD_OLDER = "older";
    String FIELD_NEWER = "newer";
    String FIELD_PER_PAGE = "per_page";

    String PATH_POSTS_CATEGORY = "categories/{categoryId}/posts";

    @FormUrlEncoded
    @POST(PATH_TOKEN)
    Observable<Session> grantAccess(@Field(FIELD_CLIENT_ID) String clientId,
                                    @Field(FIELD_CLIENT_SECRET) String clientSecret,
                                    @Field(FIELD_GRANT_TYPE) String grantType);

    @GET(PATH_CATEGORY)
    Observable<CategoryResponse> categories(@Header(FIELD_AUTHORIZATION) String authorization);

    @GET(PATH_POSTS)
    Observable<PostResponse> postsToday(@Header(FIELD_AUTHORIZATION) String authorization,
                                        @QueryMap Map<String, String> parameters);

    @GET(PATH_POSTS_CATEGORY)
    Observable<PostResponse> postsByCategory(@Header(FIELD_AUTHORIZATION) String authorization,
                                             @Path(PATH_CATEGORY_ID) String categoryId,
                                             @QueryMap Map<String, String> parameters);
}
