package fr.psug.avro

import org.apache.avro.SchemaBuilder
import org.apache.avro.generic.GenericData.{Array, Record}
import org.apache.avro.generic.{GenericData, GenericRecord, GenericRecordBuilder}
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

    val transformer = AvroLens.defineWithSideEffect[String]("", _.toUpperCase)
    transformer(record)
    record.toString should be ("""[TOTO, TITI]""")
  }

  it should "handle nested array of simple values" in {
    val arraySchema = SchemaBuilder
      .array()
      .items()
      .stringType()

    val schema = SchemaBuilder
      .record("Person")
      .fields()
      .name("ingredients").`type`(arraySchema).noDefault()
      .endRecord()

    import scala.collection.JavaConverters._
    val record = new GenericRecordBuilder(schema)
      .set("ingredients", new Array[String](arraySchema, List("toto", "titi").asJava))
      .build()

    val transformer = AvroLens.defineWithSideEffect[String]("ingredients", _.toUpperCase)
    transformer(record)
    record.toString should be ("""{"ingredients": ["TOTO", "TITI"]}""")
  }

  it should "handle nested arrays of nested records values" in {
    val innerRecordSchema = SchemaBuilder
      .record("Item")
      .fields()
      .name("pos").`type`().stringType().noDefault()
      .endRecord()

    val arraySchema = SchemaBuilder
      .array()
      .items(innerRecordSchema)


    val schema = SchemaBuilder
      .record("Person")
      .fields()
      .name("ingredients").`type`(arraySchema).noDefault()
      .endRecord()

    import scala.collection.JavaConverters._
    val record = new GenericRecordBuilder(schema)
      .set("ingredients", new Array[Record](arraySchema,
        List(
          new GenericRecordBuilder(innerRecordSchema)
            .set("pos", "toto")
            .build(),
          new GenericRecordBuilder(innerRecordSchema)
            .set("pos", "titi")
            .build()
        ).asJava))
      .build()

    val transformer = AvroLens.defineWithSideEffect[String]("ingredients.pos", _.toUpperCase)
    transformer(record)
    record.toString should be ("""{"ingredients": [{"pos": "TOTO"}, {"pos": "TITI"}]}""")
  }

  it should "handle nested arrays of nested records values with JSON Path expression" in {
    val innerRecordSchema = SchemaBuilder
      .record("Item")
      .fields()
      .name("pos").`type`().stringType().noDefault()
      .endRecord()

    val arraySchema = SchemaBuilder
      .array()
      .items(innerRecordSchema)


    val schema = SchemaBuilder
      .record("Person")
      .fields()
      .name("ingredients").`type`(arraySchema).noDefault()
      .endRecord()

    import scala.collection.JavaConverters._
    val record = new GenericRecordBuilder(schema)
      .set("ingredients", new Array[Record](arraySchema,
        List(
          new GenericRecordBuilder(innerRecordSchema)
            .set("pos", "toto")
            .build(),
          new GenericRecordBuilder(innerRecordSchema)
            .set("pos", "titi")
            .build()
        ).asJava))
      .build()

    val transformer = AvroLens.defineWithSideEffect[String]("ingredients[].pos", _.toUpperCase)
    transformer(record)
    record.toString should be ("""{"ingredients": [{"pos": "TOTO"}, {"pos": "TITI"}]}""")
  }
}
