package com.unthinkable.gsc;

import com.google.api.services.people.v1.model.ContactGroup;
import com.google.api.services.people.v1.model.ContactGroupResponse;
import com.google.api.services.people.v1.model.Empty;
import com.google.api.services.people.v1.model.Person;
import com.unthinkable.gsc.service.impl.ContactMangeServiceBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/people/")
public class GscController {

    @Autowired
    ContactMangeServiceBean mangeServiceBean;

    @GetMapping("gsc/feed")
    public List<String> getGscFeed() {
        return mangeServiceBean.getFeed();
    }

    @GetMapping("gsc/entries/count")
    public int getGscEntriesCount() {
        return mangeServiceBean.getEntriesCount();
    }


    @GetMapping("gsc/entry")
    public String getGscEntry(@RequestParam("resourceName") String resourceName) {
        return mangeServiceBean.getEntry(resourceName);
    }

    @PostMapping("gsc/entry/batch")
    public List<String> getGscEntryBatch(@RequestBody List<String> resourceNames) {
        return mangeServiceBean.getEntryBatch(resourceNames);
    }

    @GetMapping("gsc/entry/photo")
    public byte[] getGscEntryPhoto(@RequestParam("resourceName") String resourceName) {
        return mangeServiceBean.getEntryPhoto(resourceName);
    }

    @GetMapping("gsc/entry/photo/update")
    public Person updateGscEntryPhoto(@RequestParam("resourceName") String resourceName) {
        return mangeServiceBean.updateContactPhoto(resourceName);
    }


    @PostMapping("gsc/create/contact")
    public String createGscContact(@RequestBody Person person) {
        return mangeServiceBean.createContact(person);
    }

    @PutMapping("gsc/update/contact")
    public Person updateGscContact(@RequestBody Person person, @RequestParam String resourceName) {
        return mangeServiceBean.updateContact(person, resourceName);
    }

    @DeleteMapping("gsc/delete/contact")
    public Empty deleteGscContact(@RequestParam String resourceName) {
        return mangeServiceBean.removeContact(resourceName);
    }

    @GetMapping("gsc/groups")
    public List<String> getGscContactGroup() {
        return mangeServiceBean.getContactGroupBatch();
    }

    @GetMapping("gsc/group/members/count")
    public int getGscContactGroupMembersCount(@RequestParam("resourceName") String resourceName) {
        return mangeServiceBean.getContactGroupMembersCount(resourceName);
    }

    @GetMapping("gsc/group")
    public ContactGroup getGscContactGroup(@RequestParam("resourceName") String resourceName) {
        return mangeServiceBean.getContactGroup(resourceName);
    }

    @PostMapping("gsc/group/batch")
    public List<ContactGroupResponse> getGscContactGroups(@RequestBody List<String> resourceName) {
        return mangeServiceBean.getContactGroupBatch(resourceName);
    }

    @PostMapping("gsc/create/group")
    public ContactGroup createGscGroup(@RequestBody ContactGroup contactGroup) {
        return mangeServiceBean.createContactGroup(contactGroup);
    }

    @PutMapping("gsc/update/group")
    public ContactGroup updateGscContactGroup(@RequestBody ContactGroup contactGroup, @RequestParam String resourceName) {
        return mangeServiceBean.updateContactGroup(contactGroup, resourceName);
    }

    @DeleteMapping("gsc/delete/group")
    public Empty deleteGscContactGroup(@RequestParam String resourceName) {
        return mangeServiceBean.removeContactGroup(resourceName);
    }
}

