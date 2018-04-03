package com.rohmanhakim.randomator.repository

import com.rohmanhakim.randomator.model.entity.Dummy
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataJpaTest
class DummyRepositoryTest {

  @Autowired
  lateinit var entityManager: TestEntityManager

  @Autowired
  lateinit var dummyRepository: DummyRepository

  @Test
  fun `should create dummy in database`() {
    val dummy = Dummy().apply { data = "asd" }
    val saved = dummyRepository.save(dummy)
    Assert.assertEquals(dummy, saved)
  }

  @Test
  fun `should find all dummies in database`() {
    entityManager.persist(Dummy().apply { data = "asd" })
    entityManager.persist(Dummy().apply { data = "qwerty" })
    entityManager.persist(Dummy().apply { data = "zxcv" })

    val result = dummyRepository.findAll()
    Assert.assertEquals(3, result.count())
    Assert.assertEquals("asd", result.elementAt(0).data)
    Assert.assertEquals("qwerty", result.elementAt(1).data)
    Assert.assertEquals("zxcv", result.elementAt(2).data)
  }

  @Test
  fun `should find one dummy in database`() {
    val toInsert = entityManager.persist(Dummy().apply { data = "asd" })

    val result = dummyRepository.findOne(toInsert.id)
    Assert.assertEquals(toInsert.id, result.id)
  }

  @Test
  fun `should delete dummy in database`() {
    val toBeDeleted = entityManager.persist(Dummy().apply { data = "asd" })

    dummyRepository.delete(toBeDeleted)
    val result = dummyRepository.findOne(toBeDeleted.id)

    Assert.assertEquals(null, result)
  }

  @Test
  fun `should update dummy in database`() {
    val oldDummy = entityManager.persist(Dummy().apply { data = "asd" })

    dummyRepository.save(oldDummy.copy(data = "zxc"))
    val result = dummyRepository.findOne(oldDummy.id)

    Assert.assertEquals("zxc", result.data)
  }
}