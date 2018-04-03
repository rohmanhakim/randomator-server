package com.rohmanhakim.randomator.controller

import com.rohmanhakim.randomator.model.entity.Dummy
import com.rohmanhakim.randomator.model.response.BaseResponse
import com.rohmanhakim.randomator.service.DummyService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringRunner::class)
@WebMvcTest(DummyController::class)
open class DummyControllerTest {

  @Autowired
  lateinit var mockMvc: MockMvc

  @MockBean
  lateinit var dummyService: DummyService

  @Autowired
  lateinit var objectMapper: ObjectMapper


  @Test
  fun `post dummy should success`() {
    val dummy = Dummy().apply { data = "asd" }
    BDDMockito.given(dummyService.createDummy(dummy)).willReturn(dummy.copy(id = 123))

    mockMvc.perform(
            post("/dummy/new")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(objectMapper.writeValueAsBytes(dummy)))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(dummy.copy(id = 123))))
  }

  @Test
  fun `get all dummy should success`() {
    val dummy = Dummy(123).apply { data = "asd" }
    val dummy2 = Dummy(456).apply { data = "zxcv" }
    BDDMockito.given(dummyService.getAllDummy()).willReturn(listOf(dummy, dummy2))
    mockMvc.perform(
            get("/dummy/all"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(listOf(dummy, dummy2))))
  }

  @Test
  fun `get one dummy should success`() {
    val dummy = Dummy(123).apply { data = "asd" }
    BDDMockito.given(dummyService.getDummyById(123)).willReturn(dummy)
    mockMvc.perform(
            get("/dummy/123"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(dummy)))
  }

  @Test
  fun `get one dummy should returns not found`() {
    BDDMockito.given(dummyService.getDummyById(123)).willReturn(null)
    mockMvc.perform(
            get("/dummy/123"))
            .andExpect(status().isNotFound)
            .andExpect(content().json(objectMapper.writeValueAsString(BaseResponse(404, "not found"))))
  }

  @Test
  fun `delete dummy should success`() {
    mockMvc.perform(
            delete("/dummy/123"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(BaseResponse(200, "success"))))
  }

  @Test
  fun `patch dummy should success`() {
    val toUpdate = Dummy(123).apply { data = "qwerty" }

    BDDMockito.given(dummyService.updateDummy(123, toUpdate)).willReturn(toUpdate)

    mockMvc.perform(
            patch("/dummy/123")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(objectMapper.writeValueAsBytes(toUpdate)))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(BaseResponse(200, "success"))))
  }

  @Test
  fun `patch dummy should success when service returns null`() {
    val toUpdate = Dummy(123).apply { data = "qwerty" }

    BDDMockito.given(dummyService.updateDummy(123, toUpdate)).willReturn(null)

    mockMvc.perform(
            patch("/dummy/123")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(objectMapper.writeValueAsBytes(toUpdate)))
            .andExpect(status().isNotFound)
            .andExpect(content().json(objectMapper.writeValueAsString(BaseResponse(404, "not found"))))
  }
}