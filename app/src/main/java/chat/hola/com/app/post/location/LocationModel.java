package chat.hola.com.app.post.location;


import java.util.List;

import javax.inject.Inject;

import chat.hola.com.app.models.Place;

/**
 * <h1>LocationModel</h1>
 *
 * @author DELL
 * @version 1.0
 * @since 23 September 2019
 */
public class LocationModel {

    @Inject
    List<Place> places;
    @Inject
    LocationAdapter locationAdapter;

    @Inject
    public LocationModel() {
    }

    void addPlaces(List<Place> places) {
        this.places.clear();
        this.places.addAll(places);
        locationAdapter.notifyDataSetChanged();
    }
}
