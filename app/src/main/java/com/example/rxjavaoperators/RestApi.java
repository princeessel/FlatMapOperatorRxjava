package com.example.rxjavaoperators;

import androidx.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApi {
    private String BASE_URL = "https://jsonplaceholder.typicode.com/";

    private final RestClient restClient;
    private static RestApi instance;

    private RestApi() {

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        restClient = retrofit.create(RestClient.class);


    }

    @NonNull
    public static synchronized RestApi getInstance() {
        if (instance == null) {
            instance = new RestApi();
        }
        return instance;
    }


    Observable<User> getUsersObservable() {

        return restClient.getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<List<User>, ObservableSource<User>>() {
                    @Override
                    public ObservableSource<User> apply(List<User> users) throws Exception {
                        return Observable.fromIterable(users)
                                .subscribeOn(Schedulers.io());
                    }
                });
    }

    public Observable<User> getUserAlbumsObservable(final User user) {

        return restClient.getUserAlbums(user.getId())
                .map(new Function<List<Album>, User>() {
                    @Override
                    public User apply(List<Album> albums) throws Exception {

                        user.setAlbums(albums);
                        return user;
                    }
                })
                .subscribeOn(Schedulers.io());
    }
}
