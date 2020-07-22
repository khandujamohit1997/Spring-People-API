package com.unthinkable.gsc.contact.impl;

import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.ContactGroup;
import com.google.api.services.people.v1.model.ListContactGroupsResponse;
import com.google.common.collect.Lists;
import com.unthinkable.gsc.contact.GroupEntryIterator;

import java.io.IOException;
import java.util.List;

public class DefaultGroupEntryIterator implements GroupEntryIterator {
    protected final String userEmail;
    private final PeopleService service;
    protected List<ContactGroup> resources = Lists.newArrayList();
    protected ListContactGroupsResponse listContactGroupsResponse;
    private final ContactRequestMethodBuilder requestMethodBuilder;
    private String nextPageToken;
    private boolean empty = false;

    public DefaultGroupEntryIterator(String userEmail, PeopleService service) {
        this.userEmail = userEmail;
        this.service = service;
        this.nextPageToken = null;
        this.requestMethodBuilder = new ContactRequestMethodBuilder();
    }

    @Override
    public GroupEntryIterator setMaxPageSize(int maxPageSize) {
        this.requestMethodBuilder.setMaxPageSize(maxPageSize);
        return this;
    }

    //Not Found
    @Override
    public GroupEntryIterator setShowDeleted(boolean showDeleted) {
        return this;
    }

    @Override
    public ListContactGroupsResponse getLatestFeed() {
        return this.listContactGroupsResponse;
    }

    @Override
    public boolean hasNext() {
        if (resources.size() > 0) {
            return true;
        }

        try {
            if (!empty) {
                this.listContactGroupsResponse = loadRequest(this.nextPageToken);
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (this.listContactGroupsResponse == null) {
            return false;
        }

        if (listContactGroupsResponse.getNextPageToken() != null) {
            this.nextPageToken = listContactGroupsResponse.getNextPageToken();
        } else {
            empty = true;
        }

        resources.addAll(listContactGroupsResponse.getContactGroups());

        return resources.size() > 0;
    }

    protected ListContactGroupsResponse loadRequest(String nextPageToken) throws IOException {
        return this.requestMethodBuilder.getContactGroups(this.service, nextPageToken);
    }

    @Override
    public ContactGroup next() {
        return resources.remove(0);
    }
}
