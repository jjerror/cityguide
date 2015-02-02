package com.example.joshua.cityguide.common;

import com.example.joshua.cityguide.CityGuideApplication;
import com.example.joshua.cityguide.activities.MainActivity;
import com.example.joshua.cityguide.fragments.PlaceListFragment;
import com.example.joshua.cityguide.models.GooglePlacesModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(injects = {MainActivity.class, PlaceListFragment.class})
public class ProdAppModule {

    @SuppressWarnings({"UnusedDeclaration", "FieldCanBeLocal"})
    private final CityGuideApplication mApplication;

    public ProdAppModule(CityGuideApplication application) {
        this.mApplication = application;
    }

    @Provides
    @Singleton
    GooglePlacesModel provideGooglePlacesModel() {
        return new GooglePlacesModel();
    }

}
