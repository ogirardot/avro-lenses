package fr.psug.avro

import org.apache.avro.Schema
import org.apache.avro.generic._

import scala.reflect.ClassTag


object AvroLens {

  /**
    * Creates a 'Lens' that will mutate in-place your GenericRecord
    *
    * @param path      the path that will be transformed
    * @param transform the lambda that will be applied to mutate the target value
    * @tparam A type of the value expected
    * @return nothing - everything is modified using the dark (but effective) side of the force
    */
  def defineWithSideEffect[A: ClassTag](path: String,
                                        transform: A => A): (GenericContainer => Unit) = {
    def matchAndReplace(container: GenericContainer,
                        paths: Seq[String],
                        parent: Option[(String, GenericRecord)]) {
      container match {
        case array: GenericArray[_] =>
          if (!array.isEmpty) {
            array.get(0) match {
              case in: A if paths.size == 1 || paths.isEmpty =>
                val casted = array.asInstanceOf[GenericArray[A]]
                for (i <- 0 until array.size())
                  casted.set(i, transform(casted.get(i)))

              case in: GenericRecord =>
                val casted = array.asInstanceOf[GenericArray[IndexedRecord]]
                for (i <- 0 until array.size())
                  matchAndReplace(casted.get(i), paths, None)
            }
          }

        case rec: GenericRecord =>
          rec.get(paths.head) match {
            case target: A if paths.size == 1 =>
              rec.put(paths.head, transform(target))

            case other: GenericContainer =>
              matchAndReplace(other, paths.tail, Some((paths.head, rec)))
          }

        case value: A =>
          val (fieldName, record) = parent.get
          record.put(fieldName, transform(value))

        case _ =>
          throw new IllegalStateException(s"Found unexpected type while going through $path at position $path next was $paths")
      }
    }
    (record: GenericContainer) => {
      val subPaths = path.replace("[]", "").split('.')
      matchAndReplace(record, subPaths, None)
    }
  }


  /**
    * Creates a 'Lens' that will mutate in-place your GenericRecord with addition Schema / Path checking
    *
    * @param path      the path that will be transformed
    * @param transform the lambda that will be applied to mutate the target value
    * @param schema    the schema that will be used to check the path against
    * @tparam A type of the value expected
    * @return nothing - everything is modified using the dark (but effective) side of the force

    */
  def defineWithSideEffectAndSchema[A](path: String,
                                       transform: A => A,
                                       schema: Schema)(implicit tag: ClassTag[A]): (GenericContainer => Unit) = {
    checkPath(path, schema)
    (record: GenericContainer) => {
      GenericData.get().validate(schema, record)
      defineWithSideEffect(path, transform)(tag)(record)
    }
  }

  def checkPath(path: String, schema: Schema) = {
    val subPaths = path.replace("[]", "").split('.')
    subPaths.foldLeft(schema) { (current, name) =>
      Option(current.getField(name)) match {
        case Some(field) =>
          field.schema()

        case None =>
          throw new IllegalStateException(s"Path '$path' does not exist according to schema ${schema.toString(true)}")
      }

    }
  }
}