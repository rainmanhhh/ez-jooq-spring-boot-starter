package ez.jooq

import org.jooq.Condition
import org.jooq.Record
import org.jooq.SelectConditionStep
import org.jooq.SelectWhereStep

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