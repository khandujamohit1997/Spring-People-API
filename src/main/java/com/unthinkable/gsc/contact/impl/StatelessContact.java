package com.unthinkable.gsc.contact.impl;

import com.google.api.services.people.v1.model.Biography;
import com.google.api.services.people.v1.model.Birthday;
import com.google.api.services.people.v1.model.EmailAddress;
import com.google.api.services.people.v1.model.Name;
import com.google.api.services.people.v1.model.Person;
import com.google.common.collect.Sets;
import com.unthinkable.gsc.contact.Contact;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class StatelessContact implements Contact {
    private Set<String> groups = Sets.newHashSet();
    private String owner;
    private String id;
    private String masterId;
    private String duId;
    private long updated;
    private Person contactEntry;
    private String eTag;
    private boolean deleted;
    private boolean hasPhoto;
    private String photoEtag;
    private String sourcePhotoEtag;
    private Long photoUpdatedOn;
    private Collection<String[]> emails;

    public StatelessContact() {
       this.contactEntry =  new Person();
    }

    public StatelessContact(Person person) {
        this.contactEntry = copyExtensions(person);
        //Deleted boolean Not Found
        this.groups = ContactHelper.getGroupIds(person);
        this.id = ContactHelper.getContactId(person);
        this.owner = null;
        this.updated = ContactHelper.getUpdated(person).getValue();
        this.deleted = ContactHelper.getDeleted(person);
        //User-Defined Fields
        this.masterId = null;
        this.duId = null;
        this.eTag = ContactHelper.getContactEtag(person);
        this.emails = ContactHelper.getEmailAndName();
        this.hasPhoto = person.getPhotos().size() > 0;
        this.photoEtag = null; //Equals to this.eTag
    }

    public StatelessContact(Contact input) {
        this.owner = input.getOwner();
        this.id = input.getGoogleId();
        this.eTag = input.getEtag() == null ? "*" : input.getEtag();
        this.groups = input.getGroups();
        this.masterId = input.getMasterId();
        this.duId = input.getDuId();
        //Working on it
        this.contactEntry = ContactHelper.stringToJson(input.getEntry(), Person.class);
        this.hasPhoto = (input.hasPicture() == null) ? false : input.hasPicture();
        this.photoEtag = input.getPhotoEtag();
        this.sourcePhotoEtag = input.getSourcePhotoEtag();
        this.photoUpdatedOn = input.getPhotoUpdatedOn();
    }

    //Pre-Defined Fields
    private Person copyExtensions(Person person) {
        return null;
    }

    @Override
    public String getDuId() {
        return null;
    }

    @Override
    public Boolean hasPicture() {
        return null;
    }

    @Override
    public Collection<String[]> getNameAndEmails() {
        return null;
    }

    @Override
    public String getGoogleId() {
        return null;
    }

    @Override
    public String getMasterId() {
        return null;
    }

    @Override
    public Set<String> getGroups() {
        return null;
    }

    @Override
    public String getHash() {
        return null;
    }

    @Override
    public String getOwner() {
        return null;
    }

    @Override
    public boolean isDeleted() {
        return false;
    }

    @Override
    public String getEntry() {
        return null;
    }

    @Override
    public Long getLastModified() {
        return null;
    }

    @Override
    public String getEtag() {
        return null;
    }

    @Override
    public Long getPhotoUpdatedOn() {
        return null;
    }

    @Override
    public String getPhotoEtag() {
        return null;
    }

    @Override
    public String getSourcePhotoEtag() {
        return null;
    }

    @Override
    public String toString() {
        final Person contact = this.contactEntry;
        StringBuilder builder = new StringBuilder();

        if(contact.getNames() != null) {
            List<Name> nameList = contact.getNames();
            nameList.forEach(name -> {
                //Title
                addAttribute(builder, "title", name.getDisplayName());

                //Name //Yomi
                startElement(builder, "name");
                addAttribute(builder, name.getMiddleName());
                addAttribute(builder, name.getFamilyName());
                addAttribute(builder, name.getDisplayName());
                addAttribute(builder, name.getGivenName());
                addAttribute(builder, name.getHonorificPrefix());
                addAttribute(builder, name.getHonorificSuffix());
                endElement(builder);
            });
         }

        // Birthday P : 12/19/1997 # C : 1997-12-19
        if (contact.getBirthdays() != null) {
            List<Birthday> birthdayList = contact.getBirthdays();
            birthdayList.forEach(birthday -> {
                if (birthday.getText().endsWith("/0000")) {
                    birthday.setText(birthday.getText().replace("/0000", "--"));
                }
                addAttribute(builder, birthday.getText());
            });
        }

        // Content
        if (contact.getBiographies() != null) {
            List<Biography> biographyList = contact.getBiographies();
            biographyList.forEach(biography -> {
                if (biography.getContentType().equals("TEXT_PLAIN")) {
                    startElement(builder, "content");
                    addAttribute(builder, biography.getValue(), "value");
                    endElement(builder);
                }
            });
        }

        // EmailAddresses //Rel
        if (contact.getEmailAddresses() != null) {
            List<EmailAddress> emailAddressList= contact.getEmailAddresses();
            emailAddressList.forEach(emailAddress -> {
                StringBuilder inner = new StringBuilder();
                startElement(inner, "email");
                addAttribute(inner, emailAddress.getType());
                addAttribute(inner, emailAddress.getDisplayName());
                addAttribute(inner, emailAddress.getMetadata().getPrimary());
                endElement(inner);
            });
        }
        return null;
    }

    protected void startElement(StringBuilder builder, String element) {
        builder.append("{").append(element).append(" ");
    }

    protected void endElement(StringBuilder builder) {
        builder.append("}");
    }

    protected void addAttribute(StringBuilder builder, Object attr, String name) {
        if (attr == null) {
            return;
        }
        if (!String.valueOf(attr).isEmpty()) {
            builder.append(name).append("=").append(attr).append(" ");
        }
    }

    protected void addAttribute(StringBuilder builder, Object attr) {
        if (attr == null) {
            return;
        }
        if (!String.valueOf(attr).isEmpty()) {
            builder.append(attr).append("\n");
        }
    }
}