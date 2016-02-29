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

object Job2 extends SparkJob with NamedObjectSupport {

  // This is a temporary workaround. List of named objects is initialized again in `this`
  val _namedObjects = Job1.namedObjects

  override def validate(sc: SparkContext, config: Config): SparkJobValidation = {
    val namedObjectsCount = _namedObjects.getNames().toSet.size
    if (namedObjectsCount > 0) SparkJobValid else SparkJobInvalid(s"Missing named object [wc]")
  }

  override def runJob(sc: SparkContext, config: Config): Any = {

    val NamedRDD(dd, _ ,_) = _namedObjects.get[NamedRDD[String]]("rdd:words").get
    val NamedMap(result) = _namedObjects.get[NamedMap[String,Int]]("map:wc").get

    // Return the result of both named Objects
    Map("dd" -> dd.collect, "wc" -> result)

  }
}

