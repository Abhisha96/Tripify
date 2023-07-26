package com.group9.group09.service;

import com.group9.group09.DTO.RequestDTO.*;
import com.group9.group09.DTO.ResponseDTO.*;
import com.group9.group09.config.JwtService;
import com.group9.group09.model.*;
import com.group9.group09.repository.interfaces.*;
import com.group9.group09.service.interfaces.HomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HomePageServiceImpl implements HomePageService {


    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private ItemsRepository itemsRepository;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private ItineraryRepository itineraryRepository;

    @Autowired
    private ReviewsPlaceRepository reviewsPlaceRepository;

    @Autowired
    private ReviewsActivityRepository reviewsActivityRepository;

    private static Logger logger = LoggerFactory.getLogger(HomePageServiceImpl.class);
    
    /**
     * Handles the choice selection service.
     *
     * @param choice the ChoiceRequestDTO object
     * @return the ChoiceResponseDTO object
     */
    @Override
    public ChoiceResponseDTO choiceSelectorService(ChoiceRequestDTO choice) {

        ChoiceResponseDTO choiceResponseDTO = new ChoiceResponseDTO();

        String username = jwtService.extractUsername(choice.getToken());
        Optional<User> user = userRepository.findByUsermail(username);
        List<Country> countryList;
        Optional<Country> country;
        List<State> stateList;

        if(choice.getRegion().equalsIgnoreCase("International")){
            countryList = countryRepository.getCountries();
            choiceResponseDTO.setRegion(choice.getRegion());
            choiceResponseDTO.setRegionList(countryList);

        } else if (choice.getRegion().equalsIgnoreCase("domestic")) {
            country = countryRepository.findByCountryId(user.get().getHomeCountry());
            stateList = stateRepository.getStatesbyCountryID(country.get().getCountryID());
            choiceResponseDTO.setRegion(choice.getRegion());
            choiceResponseDTO.setRegionList(stateList);
        }

        return choiceResponseDTO;
    }
    /**
     * Handles the location selection service.
     *
     * @param location the LocationRequestDTO object
     * @return the LocationResponseDTO object
     */
    @Override
    public LocationResponseDTO locationSelectorService(LocationRequestDTO location) {

        LocationResponseDTO locationResponseDTO = new LocationResponseDTO();

        String username = jwtService.extractUsername(location.getToken());
        Optional<User> user = userRepository.findByUsermail(username);
        Optional<State> state = stateRepository.findByStateName(location.getLocation());
        List<City> cityList = cityRepository.getCitiesbyStateID(state.get().getStateID());

        List<String> citiesStringList = new ArrayList<>();
        for (City city : cityList) {
            citiesStringList.add(city.getCityName());
        }

        locationResponseDTO.setDescription(state.get().getDescription());
        locationResponseDTO.setCities(cityList);

        return locationResponseDTO;
    }


    /**
     * Handles the city selection service.
     *
     * @param cityRequestDTO the CityRequestDTO object
     * @return the CityResponseDTO object
     */
    public CityResponseDTO citySelectorService(CityRequestDTO cityRequestDTO) {

        CityResponseDTO cityResponseDTO = new CityResponseDTO();

        String username = jwtService.extractUsername(cityRequestDTO.getToken());
        Optional<User> user = userRepository.findByUsermail(username);
        Optional<City> city = cityRepository.findByCityId(cityRequestDTO.getCityID());
        List<Place> placeList = placeRepository.getPlacesbyCityID(city.get().getCityId());
        List<Activity> activityList = activityRepository.getActivitiesbyCityID(city.get().getCityId());

        List<String> placeStringList = new ArrayList<>();

        for (Place place : placeList) {
            placeStringList.add(place.getPlaceName());
        }

        cityResponseDTO.setCityID(city.get().getCityId());
        cityResponseDTO.setCityName(city.get().getCityName());
        cityResponseDTO.setDescription(city.get().getDescription());
        cityResponseDTO.setPlaceObjectResponseList(placeList);
        cityResponseDTO.setActivityList(activityList);

        return cityResponseDTO;
    }

    /**
     * Handles the place selection service.
     *
     * @param placeRequestDTO the PlaceRequestDTO object
     * @return the PlaceResponseDTO object
     */
    @Override
    public PlaceResponseDTO placeSelectorService(PlaceRequestDTO placeRequestDTO) {
        PlaceResponseDTO placeResponseDTO = new PlaceResponseDTO();

        String username = jwtService.extractUsername(placeRequestDTO.getToken());
        Optional<User> user = userRepository.findByUsermail(username);
        Optional<Place> place = placeRepository.findByPlaceId(placeRequestDTO.getPlaceID());
        List<Activity> activityList = activityRepository.getActivitiesbyCityID(place.get().getCityId());

        List<String> activityStringList = new ArrayList<>();
        for (Activity activity : activityList) {
            activityStringList.add(activity.getActivityName());
        }

        placeResponseDTO.setPlaceName(place.get().getPlaceName());
        placeResponseDTO.setPlaceID(place.get().getPlaceId());
        placeResponseDTO.setDescription(place.get().getDescription());
        placeResponseDTO.setActivityObjectsResponseList(activityList);
        return placeResponseDTO;
    }

    /**
     * Handles the activities service.
     *
     * @param activityRequestDTO the ActivityRequestDTO object
     * @return the ActivityResponseDTO object
     */
    @Override
    public ActivityResponseDTO getActivitiesService(ActivityRequestDTO activityRequestDTO) {
        ActivityResponseDTO activityResponseDTO = new ActivityResponseDTO();

        String username = jwtService.extractUsername(activityRequestDTO.getToken());
        Optional<User> user = userRepository.findByUsermail(username);
        List<Activity> activityList = activityRepository.getAllActivities();

        activityResponseDTO.setActivityObjectsResponseList(activityList);


        return activityResponseDTO;
    }

    /**
     * Handles the items to carry service.
     *
     * @return the ItemsToCarryResponseDTO object
     */
    @Override
    public ItemsToCarryResponseDTO getItemstoCarry() {

        ItemsToCarryResponseDTO itemsToCarryResponseDTO = new ItemsToCarryResponseDTO();

        List<ItemstoCarry> itemstoCarryList = itemsRepository.getAllItems();
        itemsToCarryResponseDTO.setItemstoCarryResponseList(itemstoCarryList);

        return itemsToCarryResponseDTO;
    }

    /**
     * Handles the wishlist service.
     *
     * @param requestDTO the WishListRequestDTO object
     * @return the WishListResponseDTO object
     */
    @Override
    public WishListResponseDTO getWishListService(RequestDTO requestDTO) {

        WishListResponseDTO wishListResponseDTO = new WishListResponseDTO();

        String token = requestDTO.getToken().replace("Bearer ","");
        String username = jwtService.extractUsername(token);
        Optional<User> user = userRepository.findByUsermail(username);

        if(user.isPresent()){

        List<wishList> wishlistList = wishlistRepository.getWishListbyUserID(Integer.parseInt(user.get().getUserId()));
        wishListResponseDTO.setWishLists(wishlistList);

        }

        return wishListResponseDTO;
    }

    /**
     * Handles the itinerary service.
     *
     * @param requestDTO the ItineraryRequestDTO object
     * @return the ItineraryResponseDTO object
     */
    @Override
    public ItineraryResponseDTO getItinerary(RequestDTO requestDTO) {

        ItineraryResponseDTO itineraryResponseDTO = new ItineraryResponseDTO();
        String token = requestDTO.getToken().replace("Bearer ","");
        String username = jwtService.extractUsername(token);
        Optional<User> user = userRepository.findByUsermail(username);

        if(user.isPresent()){

                List<Itinerary> itineraryList = itineraryRepository.getItineraryList(Integer.parseInt(user.get().getUserId()));
                itineraryResponseDTO.setItineraryObjectList(itineraryList);
        }

        return itineraryResponseDTO;
    }

    @Override
    public ReviewsPlaceResponseDTO getReviewDetails(ReviewsPlaceRequestDTO reviewsPlaceRequestDTO) {

        ReviewsPlaceResponseDTO reviewsPlaceResponseDTO = new ReviewsPlaceResponseDTO();
        List<ReviewsPlace> reviewsPlaces = reviewsPlaceRepository.getReviewsPlacebyPlaceId(reviewsPlaceRequestDTO.getPlaceid());
        reviewsPlaceResponseDTO.setReviewsPlaces(reviewsPlaces);

        return reviewsPlaceResponseDTO;
    }

    @Override
    public ReviewsActivityResponseDTO getReviewActivityDetails(ReviewsActivityRequestDTO reviewsActivityRequestDTO) {

        ReviewsActivityResponseDTO reviewsActivityResponseDTO = new ReviewsActivityResponseDTO();
        List<ReviewsActivity> reviewsActivity = reviewsActivityRepository.getReviewsActivitybyActivityId(reviewsActivityRequestDTO.getActivityid());
        reviewsActivityResponseDTO.setReviewsActivities(reviewsActivity);

        return reviewsActivityResponseDTO;
    }

    @Override
    public CountryResponseDTO countrySelectorService(CountryRequestDTO countryRequestDTO) {

        CountryResponseDTO countryResponseDTO = new CountryResponseDTO();

        String username = jwtService.extractUsername(countryRequestDTO.getToken());
        Optional<User> user = userRepository.findByUsermail(username);
        Optional<Country> country = countryRepository.findByCountryName(countryRequestDTO.getCountry_name());

        List<State> states = stateRepository.getStatesbyCountryID(country.get().getCountryID());


        for (State state : states) {
         state.setCityList(cityRepository.getCitiesbyStateID(state.getStateID()));
        }

        for (State state : states) {
            List<City> cityList = state.getCityList();
            for (City city : cityList) {
                city.setPlaces(placeRepository.getPlacesbyCityID(city.getCityId()));
            }
        }
        countryResponseDTO.setCountryName(country.get().getCountryName());
        countryResponseDTO.setDescription(country.get().getDescription());
        countryResponseDTO.setStateList(states);

        return countryResponseDTO;

    }

}
