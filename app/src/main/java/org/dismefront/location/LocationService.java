package org.dismefront.location;

import org.dismefront.location.dto.LocationDTO;
import org.dismefront.location.geography.City;
import org.dismefront.location.geography.CityRepository;
import org.dismefront.location.geography.Country;
import org.dismefront.location.geography.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private LocationRepository locationRepository;

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    public City getCityById(Long id) {
        return cityRepository.findById(id).orElse(null);
    }

    public Location createLocation(LocationDTO dto) {
        Location location = new Location();
        location.setCity(this.getCityById(dto.getCityId()));
        location.setDistrict(dto.getDistrict());
        location.setApartmentFloor(dto.getApartmentFloor());
        location.setApartmentNumber(dto.getApartmentNumber());
        location.setBuildingNumber(dto.getBuildingNumber());
        location.setBuildingFloorNumber(dto.getBuildingFloorNumber());
        location.setLivingArea(dto.getLivingArea());
        return locationRepository.save(location);
    }

}
