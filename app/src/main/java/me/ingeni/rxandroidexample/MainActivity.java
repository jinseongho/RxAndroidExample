package me.ingeni.rxandroidexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.NoSuchElementException;
import java.util.logging.Logger;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Observable.just("one", "two", "three", "four", "five")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        Log.d("MainActivity @@@ : ", "s : " + s);
                    }
                });

        Flowable.just(1, 2, 3)
                .doOnCancel(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d("MainActivity @@@ : ", "doOnCancel");
                    }
                })
                .take(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Log.d("MainActivity @@@ : ", "integer : " + integer);
                    }
                });

        Subscriber<Integer> subscriber = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            public void onNext(Integer t) {
                if (t == 1) {
                    throw new IllegalArgumentException();
                }
            }

            public void onError(Throwable e) {
                if (e instanceof IllegalArgumentException) {
                    throw new UnsupportedOperationException();
                }
            }

            public void onComplete() {
                throw new NoSuchElementException();
            }
        };
        Flowable.just(1).subscribe(subscriber);
    }
}
