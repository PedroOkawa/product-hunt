package com.okawa.pedro.producthunt.util.manager;

import android.content.Context;
import android.os.AsyncTask;

import com.okawa.pedro.producthunt.database.DatabaseRepository;
import com.okawa.pedro.producthunt.model.response.CategoryResponse;
import com.okawa.pedro.producthunt.model.response.CommentResponse;
import com.okawa.pedro.producthunt.model.response.PostResponse;
import com.okawa.pedro.producthunt.model.response.VoteResponse;
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

    public static final int PROCESS_SESSION_ID = 0x0000;
    public static final int PROCESS_CATEGORIES_ID = 0x0001;
    public static final int PROCESS_POSTS_ID = 0x0002;
    public static final int PROCESS_COMMENTS_ID = 0x0003;
    public static final int PROCESS_VOTES_ID = 0x0004;

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

    /* SESSION */

    public void validateSession(final ApiListener apiListener) {
        if(!configHelper.isConnected(context)) {
            apiListener.onError(ConfigHelper.CONNECTION_ERROR);
            return;
        }

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

    /* CATEGORY */

    public void requestCategories(final ApiListener apiListener) {
        if(!configHelper.isConnected(context)) {
            apiListener.onDataLoaded(PROCESS_CATEGORIES_ID);
            return;
        }

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

    /* POST */

    public void requestPostsByDate(ApiListener apiListener, Date date) {
        if(!configHelper.isConnected(context)) {
            apiListener.onDataLoaded(PROCESS_POSTS_ID);
            return;
        }

        Map<String, String> parameters = new HashMap<>();

        if(!databaseRepository.checkIsToday(date)) {
            parameters.put(ApiInterface.FIELD_DAY, databaseRepository.convertDateToString(date));
        }
    }

    public void requestPostsByDaysAgo(final ApiListener apiListener) {
        if(!configHelper.isConnected(context)) {
            apiListener.onDataLoaded(PROCESS_POSTS_ID);
            return;
        }

        Map<String, String> parameters = new HashMap<>();
        parameters.put(ApiInterface.FIELD_DAYS_AGO, databaseRepository.getDaysAgo());
        apiInterface
                .postsByCategory(
                        databaseRepository.selectSession().getToken(),
                        databaseRepository.getCurrentCategoryName(),
                        parameters)
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
                        databaseRepository.addDayAgo();
                    }

                    @Override
                    public void onError(Throwable e) {
                        apiListener.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Post> posts) {
                        new PersistencePost(apiListener, posts).execute();
                    }
                });
    }

    protected class PersistencePost extends AsyncTask<Void, Void, Void> {

        private ApiListener apiListener;
        private List<Post> posts;

        protected PersistencePost(ApiListener apiListener, List<Post> posts) {
            this.apiListener = apiListener;
            this.posts = posts;
        }

        @Override
        protected Void doInBackground(Void... params) {

            for(Post post : posts) {
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
            apiListener.onDataLoaded(PROCESS_POSTS_ID);
            super.onPostExecute(aVoid);
        }
    }

    /* COMMENTS */

    public void requestCommentsByPost(final ApiListener apiListener, long postId) {
        if(!configHelper.isConnected(context)) {
            apiListener.onDataLoaded(PROCESS_COMMENTS_ID);
            return;
        }

        apiInterface
                .commentsByPost(databaseRepository.selectSession().getToken(), postId, null)
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
                        apiListener.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Comment> comments) {
                        new PersistenceComment(apiListener, comments).execute();
                    }
                });
    }

    protected class PersistenceComment extends AsyncTask<Void, Void, Void> {

        private ApiListener apiListener;
        private List<Comment> comments;

        protected PersistenceComment(ApiListener apiListener, List<Comment> comments) {
            this.apiListener = apiListener;
            this.comments = comments;
        }

        @Override
        protected Void doInBackground(Void... params) {

            for(Comment comment : comments) {
                comment.sync();
                syncChildren(comment);
            }

            databaseRepository.updateComments(comments);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            apiListener.onDataLoaded(PROCESS_COMMENTS_ID);
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

    /* COMMENTS */

    public void requestVotesByPost(final ApiListener apiListener, long postId) {
        if(!configHelper.isConnected(context)) {
            apiListener.onDataLoaded(PROCESS_VOTES_ID);
            return;
        }

        apiInterface
                .votesByPost(databaseRepository.selectSession().getToken(), postId, null)
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
                        apiListener.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Vote> votes) {
                        new PersistenceVote(apiListener, votes).execute();
                    }
                });
    }

    protected class PersistenceVote extends AsyncTask<Void, Void, Void> {

        private ApiListener apiListener;
        private List<Vote> votes;

        protected PersistenceVote(ApiListener apiListener, List<Vote> votes) {
            this.apiListener = apiListener;
            this.votes = votes;
        }

        @Override
        protected Void doInBackground(Void... params) {

            for(Vote vote : votes) {
                vote.sync();

                databaseRepository.updateUser(vote.getUser());
                databaseRepository.updateAvatar(vote.getUser().getAvatar());
            }

            databaseRepository.updateVotes(votes);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            apiListener.onDataLoaded(PROCESS_VOTES_ID);
            super.onPostExecute(aVoid);
        }
    }

}
