package com.okawa.pedro.producthunt.util.manager;

import android.content.Context;
import android.util.Log;

import com.okawa.pedro.producthunt.database.DatabaseRepository;
import com.okawa.pedro.producthunt.model.CategoryResponse;
import com.okawa.pedro.producthunt.model.PostResponse;
import com.okawa.pedro.producthunt.network.ApiInterface;
import com.okawa.pedro.producthunt.util.helper.ConfigHelper;
import com.okawa.pedro.producthunt.util.listener.ApiListener;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import greendao.Category;
import greendao.Post;
import greendao.Session;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by pokawa on 19/02/16.
 */
public class ApiManager {

    public static final int PROCESS_SESSION_ID = 0x0000;
    public static final int PROCESS_CATEGORIES_ID = 0x0001;
    public static final int PROCESS_POSTS_ID = 0x0002;

    private static final String API_PROPERTIES = "api.properties";
    private static final String API_ID = "api_id";
    private static final String API_SECRET = "api_secret";
    private static final String ACCESS_GRANT_TYPE = "client_credentials";

    private Context context;
    private ApiInterface apiInterface;
    private ConfigHelper configHelper;
    private DatabaseRepository databaseRepository;

    public ApiManager(Context context,
                      ApiInterface apiInterface,
                      ConfigHelper configHelper,
                      DatabaseRepository databaseRepository) {
        this.context = context;
        this.apiInterface = apiInterface;
        this.configHelper = configHelper;
        this.databaseRepository = databaseRepository;
    }

    public void validateSession(final ApiListener apiListener) {
        Properties properties = new Properties();

        try {
            properties.load(context.getAssets().open(API_PROPERTIES));
        } catch (IOException e) {
            e.printStackTrace();
            apiListener.onError(e.getMessage());
            return;
        }

        String apiId = properties.getProperty(API_ID);
        String apiSecret = properties.getProperty(API_SECRET);

        apiInterface
                .grantAccess(apiId, apiSecret, ACCESS_GRANT_TYPE)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Session>() {
                    @Override
                    public void onCompleted() {
                        apiListener.onDataLoaded(PROCESS_SESSION_ID);
                    }

                    @Override
                    public void onError(Throwable e) {
                        apiListener.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(Session session) {
                        databaseRepository.updateSession(session);
                    }
                });
    }

    public void requestCategories(final ApiListener apiListener) {
        apiInterface
                .categories(databaseRepository.selectSession().getToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<CategoryResponse, Observable<List<Category>>>() {
                    @Override
                    public Observable<List<Category>> call(CategoryResponse categoryResponse) {
                        return Observable.just(categoryResponse.getCategories());
                    }
                })
                .subscribe(new Observer<List<Category>>() {
                    @Override
                    public void onCompleted() {
                        apiListener.onDataLoaded(PROCESS_CATEGORIES_ID);
                    }

                    @Override
                    public void onError(Throwable e) {
                        apiListener.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Category> categories) {
                        databaseRepository.updateCategories(categories);
                    }
                });
    }

    public void requestPostsByDate(final ApiListener apiListener, Date date) {
        Map<String, String> parameters = new HashMap<>();

        if(!configHelper.checkIsToday(date)) {
            parameters.put(ApiInterface.FIELD_DAY, configHelper.convertDateToString(date));
        }

        latestPosts(apiListener, parameters);
    }

    public void requestPostsByDaysAgo(final ApiListener apiListener) {
        Map<String, String> parameters = new HashMap<>();

        String page = configHelper.getDaysAgo();

        parameters.put(ApiInterface.FIELD_DAYS_AGO, page);

        latestPosts(apiListener, parameters);
    }

    private void latestPosts(final ApiListener apiListener, Map<String, String> parameters) {
        apiInterface
                .postsByDate(databaseRepository.selectSession().getToken(), parameters)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<PostResponse, Observable<List<Post>>>() {
                    @Override
                    public Observable<List<Post>> call(PostResponse postResponse) {
                        return Observable.just(postResponse.getPosts());
                    }
                })
                .flatMapIterable(new Func1<List<Post>, Iterable<Post>>() {
                    @Override
                    public Iterable<Post> call(List<Post> posts) {
                        return posts;
                    }
                })
                .doOnNext(new Action1<Post>() {
                    @Override
                    public void call(Post post) {
                        post.sync();
                        post.getUser().sync();

                        databaseRepository.updateScreenshot(post.getScreenshot());
                        databaseRepository.updateThumbnail(post.getThumbnail());
                        databaseRepository.updateUser(post.getUser());
                        databaseRepository.updateAvatar(post.getUser().getAvatar());
                    }
                })
                .toList()
                .subscribe(new Observer<List<Post>>() {
                    @Override
                    public void onCompleted() {
                        apiListener.onDataLoaded(PROCESS_POSTS_ID);
                    }

                    @Override
                    public void onError(Throwable e) {
                        apiListener.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Post> posts) {
                        databaseRepository.updatePosts(posts);
                    }
                });
    }

    public void requestPostsByCategory(final ApiListener apiListener, String category) {
        apiInterface
                .postsByCategory(databaseRepository.selectSession().getToken(), category, null)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<PostResponse, Observable<List<Post>>>() {
                    @Override
                    public Observable<List<Post>> call(PostResponse postResponse) {
                        return Observable.just(postResponse.getPosts());
                    }
                })
                .flatMapIterable(new Func1<List<Post>, Iterable<Post>>() {
                    @Override
                    public Iterable<Post> call(List<Post> posts) {
                        return posts;
                    }
                })
                .doOnNext(new Action1<Post>() {
                    @Override
                    public void call(Post post) {
                        post.sync();
                        post.getUser().sync();
                    }
                })
                .toList()
                .subscribe(new Observer<List<Post>>() {
                    @Override
                    public void onCompleted() {
                        apiListener.onDataLoaded(PROCESS_POSTS_ID);
                    }

                    @Override
                    public void onError(Throwable e) {
                        apiListener.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Post> posts) {
                        databaseRepository.updatePosts(posts);
                    }
                });
    }

}
