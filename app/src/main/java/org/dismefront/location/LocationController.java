package org.dismefront.location;

import org.dismefront.location.geography.City;
import org.dismefront.location.geography.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/locations")
@PreAuthorize("hasRole('ROLE_STANDARD_USER')")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping("/cities")
    public ResponseEntity<List<City>> getCities() {
        return ResponseEntity.ok(locationService.getAllCities());
    }

    @GetMapping("/countries")
    public ResponseEntity<List<Country>> getCountries() {
        return ResponseEntity.ok(locationService.getAllCountries());
    }

}
