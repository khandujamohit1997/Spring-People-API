package com.unthinkable.gsc.contact.impl;

import com.google.api.client.util.Base64;
import com.google.api.client.util.IOUtils;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.BatchGetContactGroupsResponse;
import com.google.api.services.people.v1.model.ContactGroup;
import com.google.api.services.people.v1.model.CreateContactGroupRequest;
import com.google.api.services.people.v1.model.Empty;
import com.google.api.services.people.v1.model.GetPeopleResponse;
import com.google.api.services.people.v1.model.Person;
import com.google.api.services.people.v1.model.Photo;
import com.google.api.services.people.v1.model.UpdateContactGroupRequest;
import com.google.api.services.people.v1.model.UpdateContactPhotoRequest;
import com.google.api.services.people.v1.model.UpdateContactPhotoResponse;
import com.unthinkable.gsc.contact.ContactEntryIterator;
import com.unthinkable.gsc.contact.GroupEntryIterator;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class DefaultGoogleContactService {
    private final String userEmail;
    private final PeopleService peopleService;

    public DefaultGoogleContactService(String userEmail, PeopleService peopleService) {
        this.userEmail = userEmail;
        this.peopleService = peopleService;
    }

    public ContactEntryIterator getContactIterator() {
        return new DefaultContactEntryIterator(this.userEmail, this.peopleService);
    }

    public int getTotalContactNumber() throws IOException {
        DefaultContactEntryIterator entryIterator = new DefaultContactEntryIterator(userEmail, peopleService);
        entryIterator.setMaxPageSize(0);
        entryIterator.hasNext();
        return entryIterator.getLatestFeed().getTotalPeople();
    }

    public Person getContacts(String resourceName) throws IOException {
        return new ContactRequestMethodBuilder().getPersons(this.peopleService, resourceName);
    }

    public GetPeopleResponse getContacts(List<String> resourceNames) throws IOException {
        return new ContactRequestMethodBuilder().getPersons(this.peopleService, resourceNames);
    }

    public void createContact(Person person) throws IOException {
        this.peopleService.people().createContact(person).execute();
    }

    public Person updateContact(Person person, String resourceName) throws IOException {
        return new ContactRequestMethodBuilder().updateContact(this.peopleService, resourceName, person);
    }

    public Empty removeContact(String resourceName) throws IOException {
        return peopleService.people().deleteContact(resourceName).execute();
    }

    public GroupEntryIterator getGroupIterator() {
        return new DefaultGroupEntryIterator(this.userEmail, this.peopleService);
    }

    public int getContactGroupMemberCount(String resourceName) throws IOException {
        return getContactGroup(resourceName).getMemberCount();
    }

    public ContactGroup getContactGroup(String resourceName) throws IOException {
        return new ContactRequestMethodBuilder().getContactGroup(this.peopleService, resourceName);
    }

    public BatchGetContactGroupsResponse getContactGroups(List<String> resourceName) throws IOException {
        return new ContactRequestMethodBuilder().getContactGroups(this.peopleService, resourceName);
    }

    public ContactGroup createGroup(ContactGroup contactGroup) throws IOException {
        return this.peopleService.contactGroups().create(new CreateContactGroupRequest().setContactGroup(contactGroup)).execute();
    }

    public ContactGroup renameContactGroup(ContactGroup contactGroup, String resourceName) throws IOException {
        contactGroup.setEtag(new ContactRequestMethodBuilder().getContactGroup(peopleService, resourceName).getEtag());
        return new ContactRequestMethodBuilder().updateContactGroup(this.peopleService, new UpdateContactGroupRequest().setContactGroup(contactGroup), resourceName);
    }

    public Empty removeContactGroup(String resourceName) throws IOException {
        return peopleService.contactGroups().delete(resourceName).execute();
    }

    public UpdateContactPhotoResponse saveContactPhoto(String resourceName, byte[] photoBytes) throws IOException {
        String encodedBase64String = Base64.encodeBase64String(photoBytes);
        return new ContactRequestMethodBuilder().updateContactPhoto(this.peopleService, resourceName, new UpdateContactPhotoRequest().setPhotoBytes(encodedBase64String));
    }

    public byte[] getContactPhoto(String resourceName) throws IOException {
        List<Photo> photoList = new ContactRequestMethodBuilder().getContactPhoto(this.peopleService, resourceName);
        if (photoList != null) {
            for (Photo photo : photoList) {
                if (photo.getMetadata().getPrimary()) {
                   URL url = new URL(photo.getUrl());
                   InputStream in = url.openStream();
                   return in.readAllBytes();
                }
            }
        }
        return null;
    }
}