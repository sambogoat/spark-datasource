
import org.apache.spark.sql.SparkSession
import com.scb.sabre.store._
import com.scb.sabre.store.hazelcast.HazelCastDataSource

case class Trade(id: String, price: Long)

object Main extends App {

  println("Main")

  val session = SparkSession.builder()
      .appName("main")
      .master("local[2]")
      .getOrCreate()

  import session.implicits._

  println(session.version)

  // TODO - what is the appropriate level of abstraction - store, hdfs, eod, intraday, store.parquet, store.hazelecastMap,...?

  /** Read HC */

  val df = session.read.hazelcast
      .option(HazelCastDataSource.HC_MAP_NAME, "test")
      .option(HazelCastDataSource.HC_MAP_KEY_CLASS, "java.lang.String")
      .option(HazelCastDataSource.HC_MAP_VALUE_CLASS, "Trade")
    .load("/path/to/dataset")

  println(df.schema)

  // TODO - Write HC
  //Seq("a", "b", "c").toDF.write.hazelcast

  // TODO - Read HDFS
  //session.read.hdfs

  // TODO _ Write HDFS
  //Seq("a", "b", "c").toDF.write.hdfs

}
