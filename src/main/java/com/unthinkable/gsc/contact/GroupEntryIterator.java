package com.unthinkable.gsc.contact;

import com.google.api.services.people.v1.model.ContactGroup;
import com.google.api.services.people.v1.model.ListContactGroupsResponse;

public interface GroupEntryIterator {
    GroupEntryIterator setMaxPageSize(int maxPageSize);

    GroupEntryIterator setShowDeleted(boolean showDeleted);

    ListContactGroupsResponse getLatestFeed();

    boolean hasNext();

    ContactGroup next();
}
