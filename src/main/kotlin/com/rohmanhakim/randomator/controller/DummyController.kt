package com.rohmanhakim.randomator.controller

import com.rohmanhakim.randomator.model.entity.Dummy
import com.rohmanhakim.randomator.model.response.BaseResponse
import com.rohmanhakim.randomator.service.DummyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/dummy")
class DummyController {

  @Autowired
  lateinit var dummyService: DummyService

  @GetMapping("/all")
  fun getAllDummy(): ResponseEntity<List<Dummy>> {
    val dummies = dummyService.getAllDummy()
    return ResponseEntity(dummies, HttpStatus.OK)
  }

  @GetMapping("/{id}")
  fun getDummyById(@PathVariable("id") id: Long): ResponseEntity<*> {
    val dummy = dummyService.getDummyById(id)
    return if (dummy != null) {
      ResponseEntity(dummy, HttpStatus.OK)
    } else {
      ResponseEntity(BaseResponse(HttpStatus.NOT_FOUND.value(), "not found"), HttpStatus.NOT_FOUND)
    }
  }

  @PostMapping("/new")
  fun createDummy(entity: RequestEntity<Dummy>): ResponseEntity<Dummy?> {
    val savedDummy = dummyService.createDummy(entity.body)
    return ResponseEntity(savedDummy, HttpStatus.OK)
  }

  @DeleteMapping("/{id}")
  fun deleteById(@PathVariable("id") id: Long): ResponseEntity<BaseResponse> {
    dummyService.deleteDummy(id)
    return ResponseEntity(BaseResponse(HttpStatus.OK.value(), "success"), HttpStatus.OK)
  }

  @PatchMapping("/{id}")
  fun updateDummy(@PathVariable("id") id: Long, entity: RequestEntity<Dummy>): ResponseEntity<BaseResponse> {
    val newDummy = entity.body
    return if (dummyService.updateDummy(id, newDummy) != null) {
      ResponseEntity(BaseResponse(HttpStatus.OK.value(), "success"), HttpStatus.OK)
    } else {
      ResponseEntity(BaseResponse(HttpStatus.NOT_FOUND.value(), "not found"), HttpStatus.NOT_FOUND)
    }
  }
}