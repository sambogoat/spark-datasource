package com.scb.sabre.store.hazelcast

import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.sources.{RelationProvider, SchemaRelationProvider}
import org.apache.spark.sql.types.StructType

object HazelCastDataSource {

  val HC_MAP_NAME = "hazelcast.map.name"

  val HC_MAP_KEY_CLASS = "hazelcast.map.keyClass"

  val HC_MAP_VALUE_CLASS = "hazelcast.map.valueClass"
}

/**
  * TODO - Create a BaseRelation from RDD[String, Trade] (Hazelcast Map type)
  *
  * - See hoodie IncrementalRelation for some guidance
  */

class DefaultSource extends RelationProvider
  with SchemaRelationProvider {

  import HazelCastDataSource._

  override def createRelation(sqlContext: SQLContext, parameters: Map[String, String]) = {
    createRelation(sqlContext, parameters, null)
  }

  override def createRelation(sqlContext: SQLContext, parameters: Map[String, String], schema: StructType) = {

    val mapName = parameters.getOrElse(HC_MAP_NAME, sys.error(s"Boom! - $HC_MAP_NAME is required"))

    val keyCls = parameters.getOrElse(HC_MAP_KEY_CLASS, sys.error(s"Boom! - $HC_MAP_KEY_CLASS is required"))

    val valueCls = parameters.getOrElse(HC_MAP_VALUE_CLASS, sys.error(s"!Boom! - $HC_MAP_VALUE_CLASS is required"))

    // TODO - Pattern match on valueCls
    new HazelcastMapRelation(
      mapName,
      Class.forName(keyCls),
      Class.forName(valueCls).asSubclass(classOf[Product]))(sqlContext)
  }


}