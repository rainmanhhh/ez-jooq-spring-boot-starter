package ez.jooq

import org.jooq.Configuration
import org.jooq.Record
import org.jooq.RecordMapper

typealias JooqConf = Configuration

@Suppress("unused")
fun <RECORD : Record, POJO : Any> RECORD.mapBy(mapper: RecordMapper<RECORD, POJO>): POJO? {
  return mapper.map(this)
}
