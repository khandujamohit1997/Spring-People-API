package com.unthinkable.gsc.contact;

import java.util.Collection;
import java.util.Set;

public interface Contact {
    String getDuId();

    Boolean hasPicture();

    Collection<String[]> getNameAndEmails();

    String getGoogleId();

    String getMasterId();

    Set<String> getGroups();

    String getHash();

    String getOwner();

    /**
     * @return If the contact is removed
     */
    boolean isDeleted();

    /**
     * @return The xml content of the contact entry
     */
    String getEntry();

    /**
     * @return The updated element value of the contact entry
     */
    Long getLastModified();

    String getEtag();

    /**
     * Time when photo was updated last time.
     *
     * @return time in long.
     */
    Long getPhotoUpdatedOn();

    /**
     * Etag of the photo.
     *
     * @return etag
     */
    String getPhotoEtag();

    /**
     * Etag of the source photo of which given photo is a copy.
     *
     * @return source photo etag.
     */
    String getSourcePhotoEtag();
}

