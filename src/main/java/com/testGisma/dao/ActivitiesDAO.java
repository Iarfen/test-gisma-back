package com.testGisma.dao;

import org.springframework.stereotype.Repository;

import com.testGisma.model.Activity;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.repository.CrudRepository;

@Repository
@Configurable
public interface ActivitiesDAO extends CrudRepository<Activity, Long>{}

