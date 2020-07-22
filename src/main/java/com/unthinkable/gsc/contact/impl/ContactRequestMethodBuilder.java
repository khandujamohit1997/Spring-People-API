package com.unthinkable.gsc.contact.impl;

import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.BatchGetContactGroupsResponse;
import com.google.api.services.people.v1.model.ContactGroup;
import com.google.api.services.people.v1.model.GetPeopleResponse;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.ListContactGroupsResponse;
import com.google.api.services.people.v1.model.Person;
import com.google.api.services.people.v1.model.Photo;
import com.google.api.services.people.v1.model.UpdateContactGroupRequest;
import com.google.api.services.people.v1.model.UpdateContactPhotoRequest;
import com.google.api.services.people.v1.model.UpdateContactPhotoResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ContactRequestMethodBuilder {

    private String personFields = "names,photos";
    private String resourceName = "people/me";
    private Integer maxPageSize = 100;
    private String sortOrder;

    public void setPersonFields(String personFields) {
        this.personFields = personFields;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public void setMaxPageSize(Integer maxPageSize) {
        this.maxPageSize = maxPageSize;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public ListConnectionsResponse getConnections(PeopleService peopleService,
                                                  String nextPageToken) throws IOException {
        return peopleService.people().connections()
            .list(resourceName)
            .setPageToken(nextPageToken)
            .setPageSize(maxPageSize)
            .setSortOrder(sortOrder)
            .setPersonFields(personFields)
            .execute();
    }

    public Person getPersons(PeopleService peopleService,
                             String resourceName) throws IOException {
        return peopleService.people()
            .get(resourceName)
            .setPersonFields(personFields)
            .execute();
    }

    public GetPeopleResponse getPersons(PeopleService peopleService,
                                        List<String> resourceNames) throws IOException {
        return peopleService.people()
            .getBatchGet()
            .setResourceNames(resourceNames)
            .setPersonFields(personFields)
            .execute();
    }

    public List<Photo> getContactPhoto(PeopleService peopleService, String resourceName) throws IOException {
        return peopleService.people()
            .get(resourceName)
            .setPersonFields(personFields)
            .execute()
            .getPhotos();
    }


    public Person updateContact(PeopleService peopleService, String resourceName, Person person) throws IOException {
        Person personDetails = peopleService
            .people()
            .get(resourceName)
            .setPersonFields("names")
            .execute();
        person.setEtag(personDetails.getEtag());

        return peopleService.people()
            .updateContact(resourceName, person)
            .setUpdatePersonFields(personFields)
            .execute();
    }

    public ListContactGroupsResponse getContactGroups(PeopleService peopleService,
                                                      String nextPageToken) throws IOException {
        return peopleService.contactGroups()
            .list()
            .setPageToken(nextPageToken)
            .setPageSize(maxPageSize)
            .execute();
    }


    public ContactGroup getContactGroup(PeopleService peopleService, String resourceName) throws IOException {
        return peopleService.contactGroups()
            .get(resourceName)
            .execute();
    }

    public BatchGetContactGroupsResponse getContactGroups(PeopleService peopleService, List<String> resourceName) throws IOException {
        return peopleService.contactGroups()
            .batchGet()
            .setResourceNames(resourceName)
            .execute();
    }

    public ContactGroup updateContactGroup(PeopleService peopleService, UpdateContactGroupRequest contactGroup, String resourceName) throws IOException {
        return peopleService.contactGroups().update(resourceName, contactGroup).execute();
    }

    public UpdateContactPhotoResponse updateContactPhoto(PeopleService peopleService, String resourceName, UpdateContactPhotoRequest decodedBase64String) throws IOException {
        return peopleService.people()
            .updateContactPhoto(resourceName, decodedBase64String).execute();
    }
}