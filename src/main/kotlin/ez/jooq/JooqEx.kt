@file:Suppress("unused")

package ez.jooq

import org.jooq.*

typealias JooqConf = Configuration

fun <RECORD : Record?, POJO : Any> RECORD.mapBy(mapper: RecordMapper<RECORD, POJO>): POJO? {
  return mapper.map(this)
}

fun <A : Attachable?> (A & Any).attach(jooq: Jooq) =
  apply { attach(jooq.context().configuration()) }

/**
 * do pagination
 * @param pageNo 1-based page number
 * @param pageSize page size
 * @param finalAction the final action to execute after pagination.
 *   `this` is [SelectForUpdateStep], `it` is the count. example:
 *   ```
 *   {
 *     Page(count = it, list = fetchInto(MyPojo::class.java))
 *   }
 *   ```
 */
fun <R : Record, Result> SelectLimitStep<R>.paginate(
  pageNo: Int,
  pageSize: Int,
  finalAction: SelectForUpdateStep<R>.(Long) -> Result
): Result {
  val ctx = configuration() ?: throw RuntimeException("can't paginate on sql without jooq context")
  val count = ctx.dsl().selectCount().from(this).fetchOneInto(Long::class.java)!!
  return limit(pageSize).offset((pageNo - 1) * pageSize).finalAction(count)
}
