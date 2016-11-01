package fr.psug.avro

import org.apache.avro.SchemaBuilder
import org.apache.avro.generic.GenericData.Array
import org.apache.avro.generic.{GenericData, GenericRecordBuilder}
import org.scalatest.{FlatSpec, Matchers}

class AvroLens$Test extends FlatSpec with Matchers {

  it should "handle nested record" in {
    val innerSchema = SchemaBuilder.
      record("Name")
      .fields()
      .name("name").`type`().stringType().noDefault().endRecord()

    val schema = SchemaBuilder
      .record("person")
      .fields()
      .name("inner").`type`(innerSchema).noDefault()
      .endRecord()

    val record = new GenericRecordBuilder(schema)
      .set("inner", new GenericRecordBuilder(innerSchema).set("name", "toto").build())
      .build()

    println(record.toString)
    val transformer = AvroLens.defineWithSideEffect[String]("inner.name", _.toUpperCase)
    transformer(record)
    record.toString should be ("""{"inner": {"name": "TOTO"}}""")
  }

  it should "handle simple arrays of values" in {
    val schema = SchemaBuilder
      .array()
        .items()
        .stringType()

    import scala.collection.JavaConverters._
    val record = new Array[String](schema, List("toto", "titi").asJava)

    println(record.toString)
    val transformer = AvroLens.defineWithSideEffect[String]("", _.toUpperCase)
    transformer(record)
    record.toString should be ("""[TOTO, TITI]""")
  }
}
