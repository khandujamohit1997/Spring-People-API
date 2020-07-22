package com.unthinkable.gsc.service.impl;

import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.BatchGetContactGroupsResponse;
import com.google.api.services.people.v1.model.ContactGroup;
import com.google.api.services.people.v1.model.ContactGroupResponse;
import com.google.api.services.people.v1.model.Empty;
import com.google.api.services.people.v1.model.GetPeopleResponse;
import com.google.api.services.people.v1.model.Name;
import com.google.api.services.people.v1.model.Person;
import com.google.api.services.people.v1.model.PersonResponse;
import com.unthinkable.gsc.contact.ContactEntryIterator;
import com.unthinkable.gsc.contact.GroupEntryIterator;
import com.unthinkable.gsc.contact.impl.DefaultGoogleContactService;
import com.unthinkable.gsc.service.GscPeopleService;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class ContactMangeServiceBean {

    public static Logger logger = getLogger(ContactMangeServiceBean.class);
    private final String userEmail = "Mohit.Khanduja@unthinkable.com";

    public DefaultGoogleContactService getPeopleService(String userEmail) {
        PeopleService peopleService = null;
        try {
            peopleService = new GscPeopleService().getPeopleService();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DefaultGoogleContactService(userEmail, peopleService);
    }

    public List<String> getFeed() {
        ContactEntryIterator contactEntryIterator = getPeopleService(this.userEmail).getContactIterator();
        List<String> nameList = new ArrayList<>();
        while (contactEntryIterator.hasNext()) {
            Person person = contactEntryIterator.next();
            List<Name> names = person.getNames();
            if ((names != null) && names.size() > 0) {
                nameList.add(person.getNames().get(0).getDisplayName());
            } else {
                logger.info("No Name Found");
            }
        }
        return nameList;
    }

    public int getEntriesCount() {
        int count = 0;
        try {
            count = getPeopleService(this.userEmail).getTotalContactNumber();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    public String getEntry(String resourceName) {
        Person person = null;
        try {
            person = getPeopleService(this.userEmail).getContacts(resourceName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (person != null) {
            List<Name> names = person.getNames();
            if ((names != null) && names.size() > 0) {
                return person.getNames().get(0).getDisplayName();
            } else {
                return "No Name Found";
            }
        }
        return "No Person Found";
    }

    public List<String> getEntryBatch(List<String> resourceNames) {
        GetPeopleResponse peopleResponse = null;
        try {
            peopleResponse = getPeopleService(this.userEmail).getContacts(resourceNames);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> nameList = null;
        if (peopleResponse != null) {
            List<PersonResponse> personResponseList = peopleResponse.getResponses();
            nameList = new ArrayList<>();
            for (PersonResponse personResponse : personResponseList) {
                Person person = personResponse.getPerson();
                if (person != null) {
                    List<Name> names = person.getNames();
                    if ((names != null) && names.size() > 0) {
                        nameList.add(person.getNames().get(0).getDisplayName());
                    } else {
                        nameList.add("No Name Found");
                    }
                }
            }
        }
        return nameList;
    }

    public byte[] getEntryPhoto(String resourceName) {
        byte[] photoBytes = null;
        try {
            photoBytes = getPeopleService(this.userEmail).getContactPhoto(resourceName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return photoBytes;
    }

    public Person updateContactPhoto(String resourceName) {
        byte[] photoBytes = null;
        try {
            photoBytes = getPeopleService(this.userEmail).getContactPhoto(resourceName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return getPeopleService(this.userEmail).saveContactPhoto(resourceName, photoBytes).getPerson();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String createContact(Person person) {
        try {
            getPeopleService(this.userEmail).createContact(person);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Success";
    }

    public Person updateContact(Person person, String resourceName) {
        try {
            person = getPeopleService(this.userEmail).updateContact(person, resourceName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return person;
    }

    public Empty removeContact(String resourceName) {
        Empty empty = null;
        try {
            empty = getPeopleService(this.userEmail).removeContact(resourceName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return empty;
    }

    public List<String> getContactGroupBatch() {
        GroupEntryIterator groupEntryIterator = getPeopleService(this.userEmail).getGroupIterator();
        List<String> groupAndResourceList = new ArrayList<>();
        while (groupEntryIterator.hasNext()) {
            ContactGroup group = groupEntryIterator.next();
            groupAndResourceList.add(group.getName());
        }
        return groupAndResourceList;
    }

    public int getContactGroupMembersCount(String resourceName) {
        int count = 0;
        try {
            count = getPeopleService(this.userEmail).getContactGroupMemberCount(resourceName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return count;
    }

    public ContactGroup getContactGroup(String resourceName) {
        ContactGroup contactGroup = null;
        try {
            contactGroup = getPeopleService(this.userEmail).getContactGroup(resourceName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contactGroup;
    }

    public List<ContactGroupResponse> getContactGroupBatch(List<String> resourceName) {
        BatchGetContactGroupsResponse groupsResponse = null;
        try {
            groupsResponse = getPeopleService(this.userEmail).getContactGroups(resourceName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (groupsResponse != null) {
            return groupsResponse.getResponses();
        }
        return null;
    }

    public ContactGroup createContactGroup(ContactGroup contactGroup) {
        ContactGroup group = null;
        try {
            group = getPeopleService(this.userEmail).createGroup(contactGroup);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return group;
    }

    public ContactGroup updateContactGroup(ContactGroup contactGroup, String resourceName) {
        ContactGroup group = null;
        try {
            group = getPeopleService(this.userEmail).renameContactGroup(contactGroup, resourceName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return group;
    }

    public Empty removeContactGroup(String resourceName) {
        Empty empty = null;
        try {
            empty = getPeopleService(this.userEmail).removeContactGroup(resourceName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return empty;
    }
}