@file:Suppress("unused")

package ez.jooq

import org.jooq.Attachable
import org.jooq.Configuration
import org.jooq.Record
import org.jooq.RecordMapper

typealias JooqConf = Configuration

fun <RECORD : Record?, POJO : Any> RECORD.mapBy(mapper: RecordMapper<RECORD, POJO>): POJO? {
  return mapper.map(this)
}

fun <A : Attachable?> (A & Any).attach(jooq: Jooq) =
  apply { attach(jooq.context().configuration()) }