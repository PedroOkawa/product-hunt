package com.okawa.pedro.producthunt.util.manager;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.okawa.pedro.producthunt.database.DatabaseRepository;
import com.okawa.pedro.producthunt.model.event.ApiEvent;
import com.okawa.pedro.producthunt.model.response.CategoryResponse;
import com.okawa.pedro.producthunt.model.response.CommentResponse;
import com.okawa.pedro.producthunt.model.response.PostResponse;
import com.okawa.pedro.producthunt.model.response.VoteResponse;
import com.okawa.pedro.producthunt.network.ApiInterface;
import com.okawa.pedro.producthunt.util.helper.ConfigHelper;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import greendao.Category;
import greendao.Comment;
import greendao.Post;
import greendao.Session;
import greendao.Vote;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by pokawa on 19/02/16.
 */
public class ApiManager {

    private static final String TEST_TOKEN = "Bearer 7dac67657ca38f8204bd0298e5cea4dfb5ba190d1443f6fc9a278e15b7e154f1";

    private static final String TEST_CATEGORIES_ALL_JSON = "categories_all.json";
    private static final String TEST_POSTS_ALL_JSON = "posts_all.json";
    private static final String TEST_POSTS_TECH_JSON = "posts_tech.json";
    private static final String TEST_POSTS_GAMES_JSON = "posts_games.json";
    private static final String TEST_POSTS_PODCASTS_JSON = "posts_podcasts.json";
    private static final String TEST_POSTS_BOOKS_JSON = "posts_books.json";

    private static final String API_PROPERTIES = "api.properties";
    private static final String API_ID = "api_id";
    private static final String API_SECRET = "api_secret";
    private static final String ACCESS_GRANT_TYPE = "client_credentials";

    private boolean isTest;
    private Gson gson;
    private Context context;
    private ApiInterface apiInterface;
    private ConfigHelper configHelper;
    private DatabaseRepository databaseRepository;

    public ApiManager(boolean isTest,
                      Gson gson,
                      Context context,
                      ApiInterface apiInterface,
                      ConfigHelper configHelper,
                      DatabaseRepository databaseRepository) {
        this.isTest = isTest;
        this.gson = gson;
        this.context = context;
        this.apiInterface = apiInterface;
        this.configHelper = configHelper;
        this.databaseRepository = databaseRepository;
    }

    /* SESSION */

    public void validateSession() {
        final ApiEvent apiEvent = new ApiEvent();

        /* TEST CASE */

        if(isTest) {
            Session session = new Session();
            session.setAccessToken(TEST_TOKEN);
            databaseRepository.updateSession(session);

            apiEvent.setType(ApiEvent.PROCESS_SESSION_ID);
            apiEvent.setError(false);
            EventBus.getDefault().post(apiEvent);
            return;
        }

        if(!configHelper.isConnected(context)) {
            apiEvent.setType(ApiEvent.PROCESS_SESSION_ID);
            apiEvent.setError(true);
            apiEvent.setMessage(ConfigHelper.CONNECTION_ERROR);
            EventBus.getDefault().post(apiEvent);
            return;
        }

        Properties properties = new Properties();

        try {
            properties.load(context.getAssets().open(API_PROPERTIES));
        } catch (IOException e) {
            e.printStackTrace();
            apiEvent.setType(ApiEvent.PROCESS_SESSION_ID);
            apiEvent.setError(true);
            apiEvent.setMessage(ConfigHelper.CONNECTION_ERROR);
            EventBus.getDefault().post(apiEvent);
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
                        apiEvent.setType(ApiEvent.PROCESS_SESSION_ID);
                        apiEvent.setError(false);
                        EventBus.getDefault().post(apiEvent);
                    }

                    @Override
                    public void onError(Throwable e) {
                        apiEvent.setType(ApiEvent.PROCESS_SESSION_ID);
                        apiEvent.setError(true);
                        apiEvent.setMessage(ConfigHelper.CONNECTION_ERROR);
                        EventBus.getDefault().post(apiEvent);
                    }

                    @Override
                    public void onNext(Session session) {
                        databaseRepository.updateSession(session);
                    }
                });
    }

    /* CATEGORY */

    public void requestCategories() {
        final ApiEvent apiEvent = new ApiEvent();

        if(!configHelper.isConnected(context) && !isTest) {
            apiEvent.setType(ApiEvent.PROCESS_CATEGORIES_ID);
            apiEvent.setError(false);
            EventBus.getDefault().post(apiEvent);
            return;
        }

        Observable<CategoryResponse> response;

        if(isTest) {

            /* TEST CASE */

            BufferedReader json = configHelper.getJsonFromAssets(context, TEST_CATEGORIES_ALL_JSON);
            response = Observable.just(gson.fromJson(json, CategoryResponse.class));
        } else {
            response = apiInterface.categories(databaseRepository.selectSession().getToken());
        }

        response
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
                        apiEvent.setType(ApiEvent.PROCESS_CATEGORIES_ID);
                        apiEvent.setError(false);
                        EventBus.getDefault().post(apiEvent);
                    }

                    @Override
                    public void onError(Throwable e) {
                        apiEvent.setType(ApiEvent.PROCESS_CATEGORIES_ID);
                        apiEvent.setError(true);
                        apiEvent.setMessage(ConfigHelper.CONNECTION_ERROR);
                        EventBus.getDefault().post(apiEvent);
                    }

                    @Override
                    public void onNext(List<Category> categories) {
                        databaseRepository.updateCategories(categories);
                    }
                });
    }

    /* POST */

    public void requestPosts(Map<String, String> parameters) {
        final ApiEvent apiEvent = new ApiEvent();

        if(!configHelper.isConnected(context) && !isTest) {
            apiEvent.setType(ApiEvent.PROCESS_POSTS_ID);
            apiEvent.setError(false);
            EventBus.getDefault().post(apiEvent);
            return;
        }

        Observable<PostResponse> response;

        if(isTest) {

            /* TEST CASE */

            BufferedReader json = configHelper.getJsonFromAssets(context, TEST_POSTS_ALL_JSON);
            response = Observable.just(gson.fromJson(json, PostResponse.class));
        } else {
            response = apiInterface.postsAll(databaseRepository.selectSession().getToken(), parameters);
        }

        response
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<PostResponse, Observable<List<Post>>>() {
                    @Override
                    public Observable<List<Post>> call(PostResponse postResponse) {
                        return Observable.just(postResponse.getPosts());
                    }
                })
                .subscribe(new Observer<List<Post>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        apiEvent.setType(ApiEvent.PROCESS_POSTS_ID);
                        apiEvent.setError(true);
                        apiEvent.setMessage(ConfigHelper.CONNECTION_ERROR);
                        EventBus.getDefault().post(apiEvent);
                    }

                    @Override
                    public void onNext(List<Post> posts) {
                        new PersistencePost(posts).execute();
                    }
                });
    }

    public void requestPostsByCategory(Map<String, String> parameters) {
        final ApiEvent apiEvent = new ApiEvent();

        if(!configHelper.isConnected(context) && !isTest) {
            apiEvent.setType(ApiEvent.PROCESS_POSTS_ID);
            apiEvent.setError(false);
            EventBus.getDefault().post(apiEvent);
            return;
        }

        Observable<PostResponse> response;

        if(isTest) {

            /* TEST CASE */

            String fileName = "";

            if(databaseRepository.getCurrentCategoryId() == Category.CATEGORY_TECH_ID) {
                fileName = TEST_POSTS_TECH_JSON;
            } else if (databaseRepository.getCurrentCategoryId() == Category.CATEGORY_GAMES_ID) {
                fileName = TEST_POSTS_GAMES_JSON;
            } else if (databaseRepository.getCurrentCategoryId() == Category.CATEGORY_PODCASTS_ID) {
                fileName = TEST_POSTS_PODCASTS_JSON;
            } else if (databaseRepository.getCurrentCategoryId() == Category.CATEGORY_BOOKS_ID) {
                fileName = TEST_POSTS_BOOKS_JSON;
            }

            BufferedReader json = configHelper.getJsonFromAssets(context, fileName);
            response = Observable.just(gson.fromJson(json, PostResponse.class));
        } else {
            response = apiInterface.postsByCategory(databaseRepository.selectSession().getToken(), databaseRepository.getCurrentCategorySlug(), parameters);
        }

        response
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<PostResponse, Observable<List<Post>>>() {
                    @Override
                    public Observable<List<Post>> call(PostResponse postResponse) {
                        return Observable.just(postResponse.getPosts());
                    }
                })
                .subscribe(new Observer<List<Post>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        apiEvent.setType(ApiEvent.PROCESS_POSTS_ID);
                        apiEvent.setError(true);
                        apiEvent.setMessage(ConfigHelper.CONNECTION_ERROR);
                        EventBus.getDefault().post(apiEvent);
                    }

                    @Override
                    public void onNext(List<Post> posts) {
                        new PersistencePost(posts).execute();
                    }
                });
    }

    protected class PersistencePost extends AsyncTask<Void, Void, Void> {

        private List<Post> posts;

        protected PersistencePost(List<Post> posts) {
            this.posts = posts;
        }

        @Override
        protected Void doInBackground(Void... params) {

            for(Post post : posts) {
                post.setUpdateDate(new Date());
                post.sync();

                databaseRepository.updateScreenshot(post.getScreenshot());
                databaseRepository.updateThumbnail(post.getThumbnail());
                databaseRepository.updateUser(post.getUser());
                databaseRepository.updateAvatar(post.getUser().getAvatar());
            }

            databaseRepository.updatePosts(posts);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ApiEvent apiEvent = new ApiEvent();
            apiEvent.setType(ApiEvent.PROCESS_POSTS_ID);
            apiEvent.setError(false);
            EventBus.getDefault().post(apiEvent);
            super.onPostExecute(aVoid);
        }
    }

    /* COMMENTS */

    public void requestCommentsByPost(long postId, Map<String, String> parameters) {
        final ApiEvent apiEvent = new ApiEvent();

        if(!configHelper.isConnected(context) && !isTest) {
            apiEvent.setType(ApiEvent.PROCESS_COMMENTS_ID);
            apiEvent.setError(false);
            EventBus.getDefault().post(apiEvent);
            return;
        }

        apiInterface
                .commentsByPost(databaseRepository.selectSession().getToken(), postId, parameters)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<CommentResponse, Observable<List<Comment>>>() {
                    @Override
                    public Observable<List<Comment>> call(CommentResponse commentResponse) {
                        return Observable.just(commentResponse.getComments());
                    }
                })
                .subscribe(new Observer<List<Comment>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        apiEvent.setType(ApiEvent.PROCESS_COMMENTS_ID);
                        apiEvent.setError(true);
                        apiEvent.setMessage(ConfigHelper.CONNECTION_ERROR);
                        EventBus.getDefault().post(apiEvent);
                    }

                    @Override
                    public void onNext(List<Comment> comments) {
                        new PersistenceComment(comments).execute();
                    }
                });
    }

    protected class PersistenceComment extends AsyncTask<Void, Void, Void> {

        private List<Comment> comments;

        protected PersistenceComment(List<Comment> comments) {
            this.comments = comments;
        }

        @Override
        protected Void doInBackground(Void... params) {

            for(Comment comment : comments) {
                comment.setUpdateDate(new Date());
                comment.sync();
                syncChildren(comment);
            }

            databaseRepository.updateComments(comments);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ApiEvent apiEvent = new ApiEvent();
            apiEvent.setType(ApiEvent.PROCESS_COMMENTS_ID);
            apiEvent.setError(false);
            EventBus.getDefault().post(apiEvent);
            super.onPostExecute(aVoid);
        }

        private void syncChildren(Comment comment) {
            comment.sync();

            databaseRepository.updateUser(comment.getUser());
            databaseRepository.updateAvatar(comment.getUser().getAvatar());

            for(Comment child : comment.getChildren()) {
                syncChildren(child);
            }

            databaseRepository.updateComments(comment.getChildren());
        }
    }

    /* VOTES */

    public void requestVotesByPost(final long postId, Map<String, String > parameters) {
        final ApiEvent apiEvent = new ApiEvent();

        if(!configHelper.isConnected(context) && !isTest) {
            apiEvent.setType(ApiEvent.PROCESS_VOTES_ID);
            apiEvent.setError(false);
            EventBus.getDefault().post(apiEvent);
            return;
        }

        apiInterface
                .votesByPost(databaseRepository.selectSession().getToken(), postId, parameters)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<VoteResponse, Observable<List<Vote>>>() {
                    @Override
                    public Observable<List<Vote>> call(VoteResponse voteResponse) {
                        return Observable.just(voteResponse.getVotes());
                    }
                })
                .subscribe(new Observer<List<Vote>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        apiEvent.setType(ApiEvent.PROCESS_VOTES_ID);
                        apiEvent.setError(true);
                        apiEvent.setMessage(ConfigHelper.CONNECTION_ERROR);
                        EventBus.getDefault().post(apiEvent);
                    }

                    @Override
                    public void onNext(List<Vote> votes) {
                        new PersistenceVote(votes, postId).execute();
                    }
                });
    }

    protected class PersistenceVote extends AsyncTask<Void, Void, Void> {

        private List<Vote> votes;
        private long postId;

        protected PersistenceVote(List<Vote> votes, long postId) {
            this.votes = votes;
            this.postId = postId;
        }

        @Override
        protected Void doInBackground(Void... params) {

            for(Vote vote : votes) {
                vote.setUpdateDate(new Date());
                vote.sync();

                databaseRepository.updateUser(vote.getUser());
                databaseRepository.updateAvatar(vote.getUser().getAvatar());
            }

            databaseRepository.updateVotes(postId, votes);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ApiEvent apiEvent = new ApiEvent();
            apiEvent.setType(ApiEvent.PROCESS_VOTES_ID);
            apiEvent.setError(false);
            EventBus.getDefault().post(apiEvent);
            super.onPostExecute(aVoid);
        }
    }

}
