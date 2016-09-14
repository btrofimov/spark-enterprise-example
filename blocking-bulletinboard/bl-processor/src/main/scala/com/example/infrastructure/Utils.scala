package com.example.infrastructure

import org.apache.spark.streaming.{Duration => SparkDuration}

import scala.concurrent.duration.Duration


object Utils {

  implicit def scalaDurationToSparkDuration(time: Duration): SparkDuration =
    new SparkDuration(time.toMillis)

}
