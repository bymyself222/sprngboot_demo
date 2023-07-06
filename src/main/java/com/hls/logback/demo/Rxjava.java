package com.hls.logback.demo;

import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

@Slf4j
public class Rxjava {

    /**
     * 正常形式
     */
    public void duExecute(){
        //观察者
        Observer<String> observer = new Observer<String>() {
            /**
             * 被观察者发布结束事件后，该方法被执行
             */
            @Override
            public void onCompleted() {
                log.info("find over");
            }

            /**
             * 被观察者发布事件期间，和观察者处理事件期间，发生异常时。执行该方法
             * @param throwable
             */
            @Override
            public void onError(Throwable throwable) {
                log.info("exception", throwable);
            }

            /**
             *  被观察者发布事件后，该方法被执行
             * @param s
             */
            @Override
            public void onNext(String s) {
                log.info("next event");
            }
        };

        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("next start");
                subscriber.onCompleted();
            }
        });
        log.info("开始订阅");
        observable.subscribe(observer);
        log.info("结束");
    }

    /**
     * 简化观察者
     */
    public void doAction(){

        Action1<String> action1 = new Action1<String>() {
            @Override
            public void call(String s) {
                log.info("action1");
            }
        };

        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("next start");
                subscriber.onCompleted();
            }
        });
        log.info("开始订阅");
        observable.subscribe(action1);
        log.info("结束");
    }

    /**
     * 全部简化
     */
    public void doJustChain(){
        Observable.just("one","two").subscribe(s->log.info("subscribe"+s));
    }

    /**
     * .map处理事件
     */
    public void doMap(){
        Observable.just(101,102)
                .map(intValue-> intValue+"")
                .subscribe(s-> log.info("subscribe"+s));
    }

    /**
     * 发布的事件中在发布多个事件
     */
    public void doFlatMap(){
        Observable.just(101,102,103)
                .flatMap(new Func1<Integer, Observable<String>>() {
                    @Override
                    public Observable<String> call(Integer integer) {
                        return Observable.create(new Observable.OnSubscribe<String>() {
                            @Override
                            public void call(Subscriber<? super String> subscriber) {
                                subscriber.onNext("hello "+integer);
                                subscriber.onNext("word "+integer);
                            }
                        });
                    }
                })
                .subscribe(s->log.info(s));
    }

    /**
     * 线程处理
     */
    public void rxSchedule(){
        Observable.create(subscriber -> {
            subscriber.onNext("hello");
            subscriber.onNext("word");
            subscriber.onCompleted();
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .flatMap(str->{
                    return Observable.create(s->{
                       s.onNext("again"+str);
                    });
                })
                .observeOn(Schedulers.newThread())
                .subscribe(s->log.info((String) s));

    }
}
