package com.unthinkable.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "contacts_details")
public class PeopleAPIModel {
    @Id
    @Column(name = "resource_name")
    private String resourceName;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "phone_numbers")
    private String phoneNumber;

    @Column(name = "photo_urls")
    private String photoUrl;
}
