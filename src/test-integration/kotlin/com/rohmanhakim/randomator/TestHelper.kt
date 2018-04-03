package com.rohmanhakim.randomator

fun createURLWithPort(uri: String, port: Int): String {
  return "http://localhost:" + port + uri
}