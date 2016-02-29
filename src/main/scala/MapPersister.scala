package minimal

import spark.jobserver.NamedObjectSupport
import spark.jobserver.NamedObject
import spark.jobserver.NamedObjectPersister


/**
 * wrapper for named objects of type Map[T,U]
 * Based on implemention of RddPersister
 */
case class NamedMap[T,U](m: Map[T,U]) extends NamedObject

/**
 * implementation of a NamedObjectPersister for DataFrame objects
 *
 */
class MapPersister[T,U] extends NamedObjectPersister[NamedMap[T,U]] {
  // Persist does not have a meaning for non-distributed collections
  override def persist(namedObj: NamedMap[T,U], name: String) {
    namedObj match {
      case NamedMap(m) => m
    }
  }

  // Unpersist does not have a meaning for non-distributed collections
  override def unpersist(namedObj: NamedMap[T,U]) {
    namedObj match {
      case NamedMap(m) => m
    }
  }

  /**
   * Replace the contents of the NamedMap with new data, can be useful for
   * extending or refreshing a dictionary.
   */
  override def refresh(namedMap: NamedMap[T,U]): NamedMap[T,U] = namedMap match {
    case NamedMap(m) => namedMap
  }

}