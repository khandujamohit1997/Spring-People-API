package com.unthinkable.gsc.contact.impl;

import com.google.api.client.util.DateTime;
import com.google.api.services.people.v1.model.ContactGroupMembership;
import com.google.api.services.people.v1.model.Membership;
import com.google.api.services.people.v1.model.Person;
import com.google.api.services.people.v1.model.Photo;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ContactHelper {
    public static Set<String> getGroupIds(Person person) {
        List<Membership> membershipList = person.getMemberships();
        return membershipList.stream()
            .map(Membership::getContactGroupMembership)
            .map(ContactGroupMembership::getContactGroupResourceName)
            .collect(Collectors.toSet());
    }

    public static boolean getDeleted(Person person) {
        return person.getMetadata().getDeleted();
    }

    public static DateTime getUpdated(Person person) {
        return DateTime.parseRfc3339(person.getMetadata().getSources().get(0).getUpdateTime());
    }

    public static String getContactId(Person person) {
        return person.getResourceName();
    }

    public static String getContactEtag(Person person) {
        return person.getEtag();
    }

    public static String getPhotoEtag(Person person) {
        return person.getPhotos().get(0).getMetadata().getSource().getEtag();
    }

    public static Collection<String[]> getEmailAndName() {
        return null;
    }

    public static Person stringToJson(String entry, Class<Person> personClass) {
        return null;
    }

    public static void getByteArrayOutputStream(Photo photo) {
    }
}
