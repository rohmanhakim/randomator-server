package com.rohmanhakim.randomator.service

import com.rohmanhakim.randomator.model.entity.Dummy
import com.rohmanhakim.randomator.repository.DummyRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface DummyService {
  fun getDummyById(id: Long): Dummy?
  fun createDummy(dummy: Dummy): Dummy
  fun updateDummy(id: Long, newDummy: Dummy): Dummy?
  fun deleteDummy(id: Long)
  fun getAllDummy(): List<Dummy>
}

@Service
class DummyServiceImpl @Autowired constructor(val dummyRepository: DummyRepository) : DummyService {

  override fun getDummyById(id: Long): Dummy? {
    return dummyRepository.findOne(id)
  }

  override fun createDummy(dummy: Dummy): Dummy {
    return dummyRepository.save(dummy)
  }

  override fun updateDummy(id: Long, newDummy: Dummy): Dummy? {
    var toUpdate = dummyRepository.findOne(id)
    return if (toUpdate != null) {
      toUpdate = newDummy.copy(id = id)
      dummyRepository.save(toUpdate)
    } else {
      null
    }
  }

  override fun deleteDummy(id: Long) {
    return dummyRepository.delete(id)
  }

  override fun getAllDummy(): List<Dummy> {
    return dummyRepository.findAll().toList()
  }

}