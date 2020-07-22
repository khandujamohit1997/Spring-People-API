package com.unthinkable.service;

import com.google.api.client.util.DateTime;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Membership;
import com.google.api.services.people.v1.model.Name;
import com.google.api.services.people.v1.model.Person;
import com.google.api.services.people.v1.model.PhoneNumber;
import com.google.api.services.people.v1.model.Photo;
import com.unthinkable.GooglePeopleApiApplication;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.PeopleServiceScopes;
import com.unthinkable.model.PeopleAPIModel;
import com.unthinkable.model.PeopleAPIModelAll;
import com.unthinkable.repository.PeopleApiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;


@Service
public class PeopleAPIService {

    private static final String APPLICATION_NAME = "Google-API";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "token-personal";

    private static final List<String> SCOPES = Arrays.asList(PeopleServiceScopes.CONTACTS_READONLY, PeopleServiceScopes.CONTACTS);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    PeopleAPIModelAll peopleAPIModel = new PeopleAPIModelAll();

    @Autowired
    PeopleApiRepository peopleApiRepository;

    @Autowired
    FileDownloadService fileDownloadService;

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws Exception {

        InputStream in = GooglePeopleApiApplication.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
            HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
            .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
            .setAccessType("offline")
            .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public PeopleService getPeopleService() throws Exception {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        PeopleService service = new PeopleService.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME)
            .build();
        return service;
    }

    public List<String> getResourceNames(PeopleService peopleService) throws IOException {
        ListConnectionsResponse response = peopleService.people().connections()
            .list("people/me")
            .setPersonFields("names")
            .execute();

        List<Person> connections = response.getConnections();
        if (connections != null && connections.size() > 0) {

            List<String> resourceNameList = new ArrayList<>();

            for (Person person : connections) {
                String resourceName = person.getResourceName();
                if (resourceName != null) {
                    resourceNameList.add(resourceName);
                } else {
                    System.out.println("No resource available for the person");
                }
            }
            peopleAPIModel.setResourceName(resourceNameList);
            return resourceNameList;
        } else {
            System.out.println("No Connection Found");
            return null;
        }
    }


    public List<String> getDisplayNames(PeopleService peopleService) throws IOException {
        ListConnectionsResponse response = peopleService.people().connections()
            .list("people/me")
            .setPersonFields("names,birthdays")
            .execute();

        List<Person> connections = response.getConnections();
        if (connections != null && connections.size() > 0) {
            List<String> displayNames = new ArrayList<>();

            for (Person person : connections) {
                List<Name> names = person.getNames();
                if ((names != null) && names.size() > 0) {
                    displayNames.add(person.getNames().get(0).getDisplayName());
                    System.out.println(person.getNames().get(0).getMiddleName());
                    System.out.println(person.getNames().get(0).getHonorificPrefix());
                    System.out.println(person.getNames().get(0).getHonorificSuffix());
                    System.out.println(person.getNames().get(0).getGivenName());
                    System.out.println(person.getNames().get(0).getPhoneticHonorificPrefix());
                    System.out.println(person.getNames().get(0).getPhoneticHonorificSuffix());
                    System.out.println(person.getBirthdays());
                } else {
                    displayNames.add("No Name Found");
                }
            }
            peopleAPIModel.setDisplayNames(displayNames);
            return displayNames;
        } else {
            return null;
        }
    }

    public List<String> getPhoneNumbers(PeopleService peopleService) throws IOException {
        ListConnectionsResponse response = peopleService.people().connections()
            .list("people/me")
            .setPageSize(10)
            .setPersonFields("phoneNumbers")
            .execute();

        List<Person> connections = response.getConnections();

        if (connections != null && connections.size() > 0) {

            List<String> phoneNumberList = new ArrayList<>();

            for (Person person : connections) {

                List<PhoneNumber> phoneNumbers = person.getPhoneNumbers();

                if ((phoneNumbers != null) && phoneNumbers.size() > 0) {
                    phoneNumberList.add(person.getPhoneNumbers().get(0).getValue());
                } else {
                    phoneNumberList.add("No Phone Number Found");
                }
            }
            peopleAPIModel.setPhoneNumbers(phoneNumberList);
            return phoneNumberList;
        } else {
            return null;
        }

    }

    public List<String> getPhotoUrls(PeopleService peopleService, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, InterruptedException, ExecutionException, URISyntaxException {

        ListConnectionsResponse response = peopleService.people().connections()
            .list("people/me")
            .setPersonFields("photos")
            .setPageSize(1)
            .execute();

        List<Person> connections = response.getConnections();

        if (connections != null && connections.size() > 0) {

            List<String> photoUrlList = new ArrayList<>();

            for (Person person : connections) {
                List<Photo> photoUrls = person.getPhotos();
                if ((photoUrls != null) && photoUrls.size() > 0) {
                    photoUrlList.add(photoUrls.get(0).getUrl());
                    //FileDownloadService.downloadFileWithResume(photoUrls.get(0).getUrl(),"photo");
                } else {
                    photoUrlList.add("No Url Found");
                }
            }
            peopleAPIModel.setPhotoUrls(photoUrlList);
            return photoUrlList;
        } else {
            return null;
        }
    }

    public Person createContact(PeopleService peopleService, Person person) throws IOException {
        return peopleService.people().createContact(person).execute();
    }

    public void deleteContact(PeopleService peopleService, String resourceName) throws IOException {
        System.out.println(resourceName);
        peopleService.people().deleteContact(resourceName).execute();
    }

    public void updateContact(PeopleService peopleService, String resourceName, Person person) throws IOException {
        Person personDetails = peopleService
            .people()
            .get(resourceName)
            .setPersonFields("userDefined")
            .execute();
        person.setEtag(personDetails.getEtag());

        peopleService.people()
            .updateContact(resourceName, person)
            .setUpdatePersonFields("userDefined")
            .execute();
    }

    public String getDisplayName(PeopleService peopleService, String resourceName) throws IOException {
        Person person = peopleService.people().get(resourceName).setPersonFields("names").execute();
        return person.getNames().get(0).getDisplayName();
    }

    public Optional<PeopleAPIModel> getContactDetailsResource(String resourceName) {
        return peopleApiRepository.findById(resourceName);
    }

    public PeopleAPIModel getContactDetailsGoogle(PeopleService peopleService, String resourceName) throws IOException {
        Person person = peopleService.people()
            .get(resourceName)
            .setPersonFields("names,phoneNumbers,photos")
            .execute();

        if (person != null) {
            PeopleAPIModel peopleAPIModel = new PeopleAPIModel(resourceName,
                person.getNames().get(0).getDisplayName(),
                person.getPhoneNumbers().get(0).getValue(),
                person.getPhotos().get(0).getUrl());

            peopleApiRepository.save(peopleAPIModel);

            return peopleAPIModel;
        } else
            return null;
    }

    public List<String> getDisplayNamesPagination(PeopleService peopleService, String pageToken, int pageSize) throws IOException {
        ListConnectionsResponse response = peopleService.people().connections()
            .list("people/me")
            .setPersonFields("names")
            .setPageSize(pageSize)
            .setPageToken(pageToken)
            .setSortOrder("FIRST_NAME_ASCENDING")
            .execute();

        List<Person> connections = response.getConnections();
        if (connections != null && connections.size() > 0) {
            List<String> displayNames = new ArrayList<>();

            for (Person person : connections) {
                List<Name> names = person.getNames();

                if ((names != null) && names.size() > 0) {
                    displayNames.add(person.getNames().get(0).getDisplayName());
                } else {
                    displayNames.add("No Name Found");
                }
            }
            peopleAPIModel.setDisplayNames(displayNames);
            displayNames.add(response.getNextPageToken());
            return displayNames;
        } else {
            return null;
        }
    }

    public List<Map<String, Object>> getAllContactDetails(PeopleService peopleService) throws IOException {
        ListConnectionsResponse response = peopleService.people().connections()
            .list("people/me")
            .setPersonFields("names,memberships,metadata")
            .execute();

        List<Person> connections = response.getConnections();
        if (connections != null && connections.size() > 0) {

            List<Map<String, Object>> contactDetailList = new ArrayList<>();
            Map<String, Object> contactDetails = new HashMap<>();
            List<String> listForAll;

            for (Person person : connections) {
                contactDetails.put("resource", person.getResourceName());
                contactDetails.put("deleted", person.getMetadata().getDeleted());

                listForAll = person.getMetadata().getPreviousResourceNames();
                if (listForAll != null)
                    for (String p : listForAll) {
                        contactDetails.put("previousRN" + p, p);
                    }

                listForAll = person.getMetadata().getLinkedPeopleResourceNames();
                if (listForAll != null)
                    for (String p : listForAll) {
                        contactDetails.put("previousLPRN" + p, p);
                    }

                contactDetails.put("eTag", person.getMetadata().getSources().get(0).getEtag());
                contactDetails.put("id", person.getMetadata().getSources().get(0).getId());
                contactDetails.put("type", person.getMetadata().getSources().get(0).getType());
                contactDetails.put("updatedTime", DateTime.parseRfc3339(person.getMetadata().getSources().get(0).getUpdateTime()).getValue());

                if (person.getMetadata().getSources().get(0).getProfileMetadata() != null) {
                    contactDetails.put("userType", person.getMetadata().getSources().get(0).getProfileMetadata().getUserTypes().get(0));
                }

                if (person.getMemberships() != null)
                    for (Membership m : person.getMemberships()) {
                        if (m.getContactGroupMembership() != null) {
                            contactDetails.put("contactGroupMembership" + m.getContactGroupMembership(), m.getContactGroupMembership());
                        }
                        if (m.getDomainMembership() != null) {
                            contactDetails.put("domainGroupMembership" + m.getDomainMembership(), m.getDomainMembership());
                        }
                    }

                contactDetailList.add(contactDetails);
            }
            return contactDetailList;
        } else {
            System.out.println("No Connection Found");
            return null;
        }
    }

}

