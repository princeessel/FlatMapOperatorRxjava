package com.example.rxjavaoperators;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestClient {

    @GET("users")
    Observable<List<User>> getUsers();

    @GET("users/{id}/albums")
    Observable<List<Album>> getUserAlbums(
            @Path("id") int id);
}
