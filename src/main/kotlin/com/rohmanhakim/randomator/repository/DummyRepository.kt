package com.rohmanhakim.randomator.repository

import com.rohmanhakim.randomator.model.entity.Dummy
import org.springframework.data.repository.CrudRepository

interface DummyRepository : CrudRepository<Dummy, Long>