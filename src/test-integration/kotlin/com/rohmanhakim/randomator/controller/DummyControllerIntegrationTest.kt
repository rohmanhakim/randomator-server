package com.rohmanhakim.randomator.controller

import com.rohmanhakim.randomator.createURLWithPort
import com.rohmanhakim.randomator.model.entity.Dummy
import com.rohmanhakim.randomator.model.response.BaseResponse
import com.rohmanhakim.randomator.repository.DummyRepository
import com.rohmanhakim.randomator.service.DatabaseCleanupService
import org.apache.http.client.HttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.client.RestTemplate

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class DummyControllerIntegrationTest {

  @LocalServerPort
  val port: Int? = null

  @Autowired
  lateinit var dummyRepository: DummyRepository

  @Autowired
  lateinit var databaseCleanupService: DatabaseCleanupService

  val httpClient: HttpClient = HttpClientBuilder.create().build()
  final val testRestTemplate: TestRestTemplate = TestRestTemplate()
  val restTemplate: RestTemplate = testRestTemplate.restTemplate.apply {
    requestFactory = HttpComponentsClientHttpRequestFactory(httpClient)
  }

  @After
  fun tearDown() {
    databaseCleanupService.truncate()
  }

  @Test
  fun `post new dummy should success`() {
    val dummy = Dummy().apply { data = "asd" }

    val requestEntity = HttpEntity<Dummy>(dummy)

    val response = restTemplate.exchange(
            createURLWithPort("/dummy/new", port!!),
            HttpMethod.POST,
            requestEntity,
            Dummy::class.java)

    Assert.assertEquals(HttpStatus.OK, response.statusCode)
    Assert.assertEquals("asd", response.body.data)
  }

  @Test
  fun `get all dummies should success`() {

    dummyRepository.save(Dummy().apply { data = "asd" })
    dummyRepository.save(Dummy().apply { data = "zxcv" })
    dummyRepository.save(Dummy().apply { data = "qwerty" })

    val response = restTemplate.getForEntity(createURLWithPort("/dummy/all", port!!), List::class.java)

    Assert.assertEquals(HttpStatus.OK, response.statusCode)
    Assert.assertEquals(3, response.body.size)
    Assert.assertEquals("asd", (response.body[0] as LinkedHashMap<*, *>)["data"])
    Assert.assertEquals("zxcv", (response.body[1] as LinkedHashMap<*, *>)["data"])
    Assert.assertEquals("qwerty", (response.body[2] as LinkedHashMap<*, *>)["data"])
  }

  @Test
  fun `get one dummy should success`() {
    dummyRepository.save(Dummy(1).apply { data = "bnm" })
    dummyRepository.save(Dummy(2).apply { data = "fgh" })

    val response = restTemplate.getForEntity(createURLWithPort("/dummy/1", port!!), Dummy::class.java)

    Assert.assertEquals(HttpStatus.OK, response.statusCode)
    Assert.assertEquals("bnm", response.body.data)
  }

  @Test
  fun `delete dummy should success`() {
    dummyRepository.save(Dummy(1).apply { data = "ert" })
    dummyRepository.save(Dummy(2).apply { data = "jkl" })

    val requestEntity = HttpEntity<Dummy>(null)

    val deleteResponse = restTemplate.exchange(
            createURLWithPort("/dummy/1", port!!),
            HttpMethod.DELETE,
            requestEntity,
            BaseResponse::class.java)

    Assert.assertEquals(HttpStatus.OK, deleteResponse.statusCode)

    val getResponse = restTemplate.getForEntity(createURLWithPort("/dummy/1", port!!), Any::class.java)

    Assert.assertEquals(HttpStatus.NOT_FOUND, getResponse.statusCode)
  }

  @Test
  fun `patch dummy should success`() {
    dummyRepository.save(Dummy(1).apply { data = "mnb" })
    dummyRepository.save(Dummy(2).apply { data = "lkj" })

    val requestEntity = HttpEntity<Dummy>(Dummy().apply { data = "qwerty" })

    val response = restTemplate.exchange(
            createURLWithPort("/dummy/1", port!!),
            HttpMethod.PATCH,
            requestEntity,
            BaseResponse::class.java
    )

    Assert.assertEquals(HttpStatus.OK, response.statusCode)

    val getResponse = restTemplate.getForEntity(createURLWithPort("/dummy/1", port!!), LinkedHashMap::class.java)

    Assert.assertEquals(HttpStatus.OK, getResponse.statusCode)
    Assert.assertEquals("qwerty", getResponse.body["data"])
  }

  @Test
  fun `patch dummy should not found`() {
    dummyRepository.save(Dummy(1).apply { data = "mnb" })
    dummyRepository.save(Dummy(2).apply { data = "lkj" })

    val requestEntity = HttpEntity<Dummy>(Dummy().apply { data = "qwerty" })

    val response = restTemplate.exchange(
            createURLWithPort("/dummy/3", port!!),
            HttpMethod.PATCH,
            requestEntity,
            BaseResponse::class.java
    )

    Assert.assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
  }
}