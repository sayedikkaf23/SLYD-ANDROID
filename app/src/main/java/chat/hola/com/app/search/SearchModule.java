package chat.hola.com.app.search;

import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.dagger.FragmentScoped;
import chat.hola.com.app.search.channel.ChannelFragment;
import chat.hola.com.app.search.channel.ChannelModule;
import chat.hola.com.app.search.locations.LocationsFragment;
import chat.hola.com.app.search.locations.LocationsModule;
import chat.hola.com.app.search.otherSearch.OtherSearchFrag;
import chat.hola.com.app.search.otherSearch.OtherSearchModule;
import chat.hola.com.app.search.people.PeopleFragment;
import chat.hola.com.app.search.people.PeopleModule;
import chat.hola.com.app.search.tags.TagsFragment;
import chat.hola.com.app.search.tags.TagsModule;
import chat.hola.com.app.search.tags.TagsUtilModule;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by ankit on 24/2/18.
 */

//@ActivityScoped
@Module
public interface SearchModule {

    @FragmentScoped
    @ContributesAndroidInjector(modules = LocationsModule.class)
    LocationsFragment locationsFragment();

    @FragmentScoped
    @ContributesAndroidInjector(modules = PeopleModule.class)
    PeopleFragment peopleFragment();

    @FragmentScoped
    @ContributesAndroidInjector(modules = {TagsModule.class, TagsUtilModule.class})
    TagsFragment tagsFragment();

    @FragmentScoped
    @ContributesAndroidInjector(modules = {ChannelModule.class})
    ChannelFragment channelFragment();

    @FragmentScoped
    @ContributesAndroidInjector(modules = OtherSearchModule.class)
    OtherSearchFrag otherSearchFrag();

    @ActivityScoped
    @Binds
    SearchContract.Presenter searchPresenter(SearchPresenter presenter);

    @ActivityScoped
    @Binds
    SearchContract.View view(SearchActivity activity);

}
