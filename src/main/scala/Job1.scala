package minimal

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.spark.sql
import org.apache.spark.storage.StorageLevel
import scala.util.Try
//import spark.jobserver.{SparkJob, SparkJobValidation, SparkJobValid, SparkJobInvalid}
import spark.jobserver._
import spark.jobserver.NamedObjectSupport
//import spark.jobserver.NamedDataFrame

/**
 * Minimal spark-jobserver job, based on wordcount example but 
 * with addition of NamedObjectSupport.
 * 
 * Set the config with the sentence to split or count:
 * input.string = "adsfasdf asdkf  safksf a sdfa"
 *
 * validate() returns SparkJobInvalid if there is no input.string
 */
object Job1 extends SparkJob with NamedObjectSupport {

  implicit def rddPersister[T] : NamedObjectPersister[NamedRDD[T]] = new RDDPersister[T]
  implicit val MapPersister = new MapPersister[String, Int]

  override def validate(sc: SparkContext, config: Config): SparkJobValidation = {
    Try(config.getString("input.string"))
      .map(x => SparkJobValid)
      .getOrElse(SparkJobInvalid("No input.string config param"))
  }

  override def runJob(sc: SparkContext, config: Config): Any = {
    val dd = sc.parallelize(config.getString("input.string").split(" ").toSeq)
    val result = dd.map((_, 1)).reduceByKey(_ + _).collect().toMap

    this.namedObjects.update("rdd:words", NamedRDD(dd, forceComputation = true, storageLevel = StorageLevel.MEMORY_ONLY))
    this.namedObjects.update("map:wc", NamedMap(result))

    this.namedObjects.getNames()

  }
}

