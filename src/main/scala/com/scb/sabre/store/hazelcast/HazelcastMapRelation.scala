package com.scb.sabre.store.hazelcast

import com.hazelcast.spark.connector.conf.SerializableConf
import com.hazelcast.spark.connector.rdd.HazelcastRDD
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Encoders, Row, SQLContext}
import org.apache.spark.sql.catalyst.ScalaReflection
import org.apache.spark.sql.sources.{BaseRelation, TableScan}
import org.apache.spark.sql.types.{StringType, StructField, StructType}

import scala.reflect.runtime.universe._

class HazelcastMapRelation[K <: Any, V <: Product] (mapName: String, keyClass: Class[K], valueClass: Class[V])
                                                   (@transient val sqlContext: SQLContext)
  extends BaseRelation with TableScan with Serializable {

  def schema = {

    val keySchema = {
      val m = runtimeMirror(keyClass.getClassLoader)
      val classSymbol = m.staticClass(keyClass.getName)
      val t = classSymbol.selfType
      ScalaReflection.schemaFor(t)
    }

    val valueSchema = {
      val m = runtimeMirror(valueClass.getClassLoader)
      val classSymbol = m.staticClass(valueClass.getName)
      val t = classSymbol.selfType
      ScalaReflection.schemaFor(t)
    }

    StructType(
      Seq(
        StructField("key", keySchema.dataType, keySchema.nullable),
        StructField("value", valueSchema.dataType, valueSchema.nullable)
      )
    )
  }

  override def buildScan() = {

    val hcRDD: RDD[(K, V)] = new HazelcastRDD[K, V](sqlContext.sparkContext, mapName, false, new SerializableConf(sqlContext.sparkContext))

    val rowRDD = hcRDD.map { r =>
      Row.fromTuple((r._1, r._2))
    }

    rowRDD
  }
}
