package com.unthinkable.controller;

import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.Person;
import com.unthinkable.model.PeopleAPIModel;
import com.unthinkable.service.PeopleAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/people/")
public class PeopleAPI {

    @Autowired
    PeopleAPI peopleAPI;

    @Autowired
    PeopleAPIService peopleAPIService;

    @Autowired(required = false)
    PeopleService peopleService;

    static String pageToken = null;

    public void getPeopleService() throws Exception {
        peopleService = peopleAPIService.getPeopleService();
    }

    @GetMapping("resources")
    public List<String> getResourceName() throws Exception {
        if (peopleService == null) {
            peopleAPI.getPeopleService();
        }
        return peopleAPIService.getResourceNames(peopleService);
    }

    @GetMapping("displayNames")
    public List<String> getDisplayNames() throws Exception {
        if (peopleService == null) {
            peopleAPI.getPeopleService();
        }
        return peopleAPIService.getDisplayNames(peopleService);
    }

    @GetMapping("phoneNumbers")
    public List<String> getPhoneNumbers() throws Exception {
        if (peopleService == null) {
            peopleAPI.getPeopleService();
        }
        return peopleAPIService.getPhoneNumbers(peopleService);
    }

    @GetMapping("photos")
    public List<String> getPhotoUrls(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        if (peopleService == null) {
            peopleAPI.getPeopleService();
        }
        return peopleAPIService.getPhotoUrls(peopleService, httpServletRequest, httpServletResponse);
    }

    @PostMapping("createContact")
    public Person createContact(@RequestBody Person person) throws Exception {
        if (peopleService == null) {
            peopleAPI.getPeopleService();
        }
        return peopleAPIService.createContact(peopleService, person);
    }

    @DeleteMapping("deleteContact")
    public void deleteContact(@RequestParam String resourceName) throws Exception {
        if (peopleService == null) {
            peopleAPI.getPeopleService();
        }
        peopleAPIService.deleteContact(peopleService, resourceName);
    }

    @PutMapping("updateContact")
    public void updateContact(@RequestParam String resourceName, @RequestBody Person person) throws Exception {
        if (peopleService == null) {
            peopleAPI.getPeopleService();
        }
        peopleAPIService.updateContact(peopleService, resourceName, person);
    }


    @GetMapping("displayNameResource")
    public String getDisplayName(@RequestParam String resourceName) throws Exception {
        if (peopleService == null) {
            peopleAPI.getPeopleService();
        }
        return peopleAPIService.getDisplayName(peopleService, resourceName);
    }

    @GetMapping("contactDetailsResourcePostgres")
    public Optional<PeopleAPIModel> getContactDetailsResource(@RequestParam String resourceName) throws Exception {
        return peopleAPIService.getContactDetailsResource(resourceName);
    }

    @GetMapping("contactDetailsResourceGoogle")
    public PeopleAPIModel getContactDetailsResourceGoogle(@RequestParam String resourceName) throws Exception {
        if (peopleService == null) {
            peopleAPI.getPeopleService();
        }
        return peopleAPIService.getContactDetailsGoogle(peopleService, resourceName);
    }

    @GetMapping("displayNamesPagination")
    public List<String> getDisplayNamesPagination(
        @RequestParam(name = "pageToken", defaultValue = "", required = false) String pageToken,
        @RequestParam(name = "pageSize", defaultValue = "5", required = false) int pageSize) throws Exception {
        if (peopleService == null) {
            peopleAPI.getPeopleService();
        }
        List<String> displayNames;
        displayNames = peopleAPIService.getDisplayNamesPagination(peopleService, PeopleAPI.pageToken, pageSize);
        PeopleAPI.pageToken = displayNames.get(displayNames.size() - 1);
        displayNames.remove(displayNames.size() - 1);
        return displayNames;
    }

    @GetMapping("contacts/details")
    public List<Map<String, Object>> getAllContactDetails() throws Exception {
        if (peopleService == null) {
            peopleAPI.getPeopleService();
        }
        return peopleAPIService.getAllContactDetails(peopleService);
    }
}
