package com.unthinkable.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PeopleAPIModelAll {

    private List<String> resourceName;

    private List<String> displayNames;

    private List<String> phoneNumbers;

    private List<String> photoUrls;
}
