package com.rohmanhakim.randomator.model.entity

import java.io.Serializable

interface Identifiable<out T : Serializable?> {
  val id: T
}