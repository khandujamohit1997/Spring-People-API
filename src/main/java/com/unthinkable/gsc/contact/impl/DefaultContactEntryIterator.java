package com.unthinkable.gsc.contact.impl;

import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Person;
import com.google.common.collect.Lists;
import com.unthinkable.gsc.contact.ContactEntryIterator;

import java.io.IOException;
import java.util.List;

public class DefaultContactEntryIterator implements ContactEntryIterator {
    protected final String userEmail;
    private final PeopleService service;
    protected List<Person> resources = Lists.newArrayList();
    protected ListConnectionsResponse listConnectionsResponse;
    private String nextPageToken;
    private boolean empty = false;
    private final ContactRequestMethodBuilder requestMethodBuilder;

    public DefaultContactEntryIterator(String userEmail, PeopleService service) {
        this.userEmail = userEmail;
        this.service = service;
        this.nextPageToken = null;
        this.requestMethodBuilder = new ContactRequestMethodBuilder();
    }

    @Override
    public ContactEntryIterator setMaxPageSize(int maxPageSize) {
        this.requestMethodBuilder.setMaxPageSize(maxPageSize);
        return this;
    }

    @Override
    public ContactEntryIterator setSortOrder(String sortOrder) {
        this.requestMethodBuilder.setSortOrder(sortOrder);
        return this;
    }

    @Override
    public ContactEntryIterator setPersonFields(String personFields) {
        this.requestMethodBuilder.setPersonFields(personFields);
        return this;
    }

    @Override
    public ContactEntryIterator setResourceName(String resourceName) {
        this.requestMethodBuilder.setResourceName(resourceName);
        return this;
    }

    @Override
    public ContactEntryIterator setShowDeleted(boolean showDeleted) {
        return this;
    }

    @Override
    public ListConnectionsResponse getLatestFeed() {
        return this.listConnectionsResponse;
    }

    @Override
    public boolean hasNext() {
        if (resources.size() > 0) {
            return true;
        }
        try {
            if (!empty) {
                this.listConnectionsResponse = loadRequest(this.nextPageToken);
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.listConnectionsResponse == null) {
            return false;
        }
        if (listConnectionsResponse.getNextPageToken() != null) {
            this.nextPageToken = listConnectionsResponse.getNextPageToken();
        } else {
            empty = true;
        }
        resources.addAll(listConnectionsResponse.getConnections());
        return resources.size() > 0;
    }
    
    @Override
    public Person next() {
        return resources.remove(0);
    }

    protected ListConnectionsResponse loadRequest(String nextPageToken) throws IOException {
        return this.requestMethodBuilder.getConnections(this.service, nextPageToken);
    }
}
