package com.alazar.aggregator.di;

import com.alazar.aggregator.db.DbProviderImpl;
import com.alazar.aggregator.screen.FeedFragment;
import com.alazar.aggregator.screen.FeedPresenter;
import com.alazar.aggregator.util.Networker;
import com.alazar.aggregator.util.Toaster;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(
    modules = {
        BaseModule.class,
        AppModule.class,
    }
)
public interface AppComponent {
    void inject(FeedFragment fragment);
    void inject(FeedPresenter presenter);
    void inject(DbProviderImpl db);
    void inject(Networker wrapper);
    void inject(Toaster toaster);
}
