package com.trackmyterm

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TrackMyTermApplication

fun main(args: Array<String>) {
	runApplication<TrackMyTermApplication>(*args)
}
