package ez.jooq

import org.jooq.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId

typealias JooqConf = Configuration

fun <RECORD : Record, POJO : Any> RECORD.mapBy(mapper: RecordMapper<RECORD, POJO>): POJO? {
  return mapper.map(this)
}

/**
 * 分转成元（默认保留两位小数，四舍六入五成双）
 */
fun Long.asFenToYuan(scale: Int = 2): BigDecimal {
  return (BigDecimal(this).divide(BigDecimal(100), scale, RoundingMode.HALF_EVEN))
}

fun LocalDateTime.toOffsetDateTime(zoneId: ZoneId = ZoneId.systemDefault()): OffsetDateTime {
  return atZone(zoneId).toOffsetDateTime()
}
