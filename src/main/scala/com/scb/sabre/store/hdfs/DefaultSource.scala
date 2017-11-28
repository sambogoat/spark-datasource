package com.scb.sabre.store.hdfs

import org.apache.spark.sql.{DataFrame, SQLContext, SaveMode}
import org.apache.spark.sql.sources.{CreatableRelationProvider, DataSourceRegister, RelationProvider, SchemaRelationProvider}
import org.apache.spark.sql.types.StructType

class DefaultSource extends RelationProvider
  with SchemaRelationProvider
  with CreatableRelationProvider
  with DataSourceRegister {

  override def createRelation(sqlContext: SQLContext, parameters: Map[String, String]) = ???

  override def createRelation(sqlContext: SQLContext, parameters: Map[String, String], schema: StructType) = ???

  override def createRelation(sqlContext: SQLContext, mode: SaveMode, parameters: Map[String, String], data: DataFrame) = ???

  override def shortName() = ???
}