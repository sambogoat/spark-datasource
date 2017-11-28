package com.scb.sabre

import org.apache.spark.sql.{DataFrameReader, DataFrameWriter}

package object store {

  implicit class StoreDataFrameWriter[T](dfw: DataFrameWriter[T]) {

    def hazelcast: DataFrameWriter[T] = {
      dfw.format("com.scb.sabre.store.hazelcast")
    }

    def hdfs: DataFrameWriter[T] = {
      dfw.format("com.scb.sabre.store.hdfs")
    }

  }

  implicit class StoreDataFrameReader(dfr: DataFrameReader) {

    def hazelcast: DataFrameReader = {
      println("hazelcast")
      dfr.format("com.scb.sabre.store.hazelcast")
    }

    def hdfs: DataFrameReader = {
      println("hdfs")
      dfr.format("com.scb.sabre.store.hdfs")
    }

  }

}
