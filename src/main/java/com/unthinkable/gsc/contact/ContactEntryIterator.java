package com.unthinkable.gsc.contact;

import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Person;

public interface ContactEntryIterator {

    ContactEntryIterator setMaxPageSize(int maxPageSize);

    ContactEntryIterator setSortOrder(String sortOrder);

    ContactEntryIterator setPersonFields(String personFields);

    ContactEntryIterator setResourceName(String resourceName);

    ContactEntryIterator setShowDeleted(boolean showDeleted);

    ListConnectionsResponse getLatestFeed();

    boolean hasNext();

    Person next();
}
