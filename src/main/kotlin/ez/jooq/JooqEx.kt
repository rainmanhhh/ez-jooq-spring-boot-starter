package ez.jooq

import org.jooq.*
import org.jooq.impl.DSL
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId

typealias JooqConf = Configuration

fun <RECORD : Record, POJO : Any> RECORD.mapBy(mapper: RecordMapper<RECORD, POJO>): POJO? {
  return mapper.map(this)
}

class Jooq(private val conf: JooqConf) {
  private val dsl get() = DSL.using(conf)

  fun <R : Record> selectFrom(t: Table<R>): JooqWhereStep<R> {
    return JooqWhereStep(dsl.selectFrom(t))
  }

  fun <R : Record> insertInto(t: Table<R>): InsertSetStep<R> {
    return dsl.insertInto(t)
  }

  fun <R : Record> update(t: Table<R>): UpdateSetFirstStep<R> {
    return dsl.update(t)
  }

  fun <R : Record> deleteFrom(t: Table<R>): DeleteUsingStep<R> {
    return dsl.deleteFrom(t)
  }
}

class JooqWhereStep<R : Record>(
  private val step: SelectWhereStep<R>
) {
  /**
   * @param conditions null conditions will be ignored
   */
  fun where(vararg conditions: Condition?): SelectConditionStep<R> {
    return step.where(conditions.filterNotNull())
  }
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
