package com.rohmanhakim.randomator.service

import com.rohmanhakim.randomator.model.entity.Dummy
import com.rohmanhakim.randomator.repository.DummyRepository
import org.junit.Assert
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito.*

class DummyServiceTest {

  val dummyRepository: DummyRepository = mock(DummyRepository::class.java)
  val dummyService: DummyService = DummyServiceImpl(dummyRepository)

  @Test
  fun `should create dummy`() {
    given(dummyRepository.save(any<Dummy>())).willReturn(Dummy().apply { data = "asd" })

    val result = dummyService.createDummy(Dummy().apply { data = "asd" })

    Assert.assertEquals("asd", result.data)
  }

  @Test
  fun `should get all dummies`() {
    val dummy = Dummy().apply { data = "qwerty" }
    val dummy2 = Dummy().apply { data = "asd" }
    val dummy3 = Dummy().apply { data = "zxcv" }

    given(dummyRepository.findAll()).willReturn(listOf(dummy, dummy2, dummy3))

    val result = dummyService.getAllDummy()

    Assert.assertEquals(3, result.size)
    Assert.assertEquals("qwerty", result[0].data)
    Assert.assertEquals("asd", result[1].data)
    Assert.assertEquals("zxcv", result[2].data)
  }

  @Test
  fun `should get one dummy`() {
    val dummy = Dummy(123L)
    given(dummyRepository.findOne(123L)).willReturn(dummy)

    val result = dummyService.getDummyById(dummy.id!!)

    Assert.assertEquals(dummy.data, result?.data)
  }

  @Test
  fun `should not found one dummy`() {
    val dummy = Dummy(123L)
    given(dummyRepository.findOne(123L)).willReturn(null)

    val result = dummyService.getDummyById(dummy.id!!)

    Assert.assertNull(result)
  }

  @Test
  fun `should delete dummy`() {
    dummyService.deleteDummy(123L)
    verify(dummyRepository).delete(ArgumentMatchers.argThat<Long> { it == 123L })
  }

  @Test
  fun `should update dummy if exist`() {
    val dummy = Dummy(123L).apply { data = "asd" }
    val newDummy = Dummy().apply { data = "zxcv" }

    given(dummyRepository.findOne(123L)).willReturn(dummy)
    given(dummyRepository.save(any(Dummy::class.java))).willReturn(Dummy().apply { data = "zxcv" })

    val result = dummyService.updateDummy(123L, newDummy)

    verify(dummyRepository).save(ArgumentMatchers.argThat<Dummy> {
      it.id == 123L && it.data == "zxcv"
    })
    Assert.assertEquals("zxcv", result?.data)
  }

  @Test
  fun `should not update dummy if not exist`() {
    val newDummy = Dummy(123L).apply { data = "asd" }

    given(dummyRepository.findOne(345L)).willReturn(null)

    val result = dummyService.updateDummy(345L, newDummy)

    Assert.assertEquals(null, result)
  }
}