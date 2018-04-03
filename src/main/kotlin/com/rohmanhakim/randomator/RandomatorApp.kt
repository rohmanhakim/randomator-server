package com.rohmanhakim.randomator

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class RandomatorApp

fun main(args: Array<String>) {
  SpringApplication.run(RandomatorApp::class.java, *args)
}
