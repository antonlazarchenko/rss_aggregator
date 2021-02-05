package com.alazar.aggregator.di;

import com.alazar.aggregator.screen.FeedFragment;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(
    modules = {
        AppModule.class,
    }
)
public interface AppComponent {
    void inject(FeedFragment fragment);
}
