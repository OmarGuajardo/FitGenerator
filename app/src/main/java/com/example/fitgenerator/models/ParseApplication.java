package com.example.fitgenerator.models;

import android.app.Application;

import com.example.fitgenerator.BuildConfig;
import com.example.fitgenerator.models.Closet;
import com.example.fitgenerator.models.ClothingItem;
import com.facebook.FacebookSdk;
import com.google.android.gms.common.server.response.FastJsonResponse;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.facebook.ParseFacebookUtils;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Use for troubleshooting -- remove this line for production
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        //Registering subclasses
        ParseObject.registerSubclass(Closet.class);
        ParseObject.registerSubclass(ClothingItem.class);
        ParseObject.registerSubclass(Fit.class);



        // Use for monitoring Parse OkHttp traffic
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        // See http://square.github.io/okhttp/3.x/logging-interceptor/ to see the options.
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);

        //Facebook
        FacebookSdk.setApplicationId(BuildConfig.FACEBOOK_APP_ID);

        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(BuildConfig.APP_ID) // should correspond to APP_ID env variable
                .clientKey(BuildConfig.MASTER_KEY)  // set explicitly unless clientKey is explicitly configured on Parse server
                .clientBuilder(builder)
                .server(BuildConfig.SERVER_URL).build());




        //Facebook Parse
        ParseFacebookUtils.initialize(getApplicationContext());

    }
}
