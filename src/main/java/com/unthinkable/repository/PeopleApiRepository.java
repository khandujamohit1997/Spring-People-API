package com.unthinkable.repository;

import com.unthinkable.model.PeopleAPIModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeopleApiRepository extends CrudRepository<PeopleAPIModel, String> {
}
