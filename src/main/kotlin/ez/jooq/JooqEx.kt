@file:Suppress("unused")

package ez.jooq

import org.jooq.*
import org.jooq.impl.DSL

typealias JooqConf = Configuration

fun <RECORD : Record, POJO : Any> RECORD?.mapBy(mapper: RecordMapper<RECORD, POJO>): POJO? {
  return if (this == null) null else mapper.map(this)
}

fun <A : Attachable?> (A & Any).attach(jooq: Jooq) =
  apply { attach(jooq.context().configuration()) }

/**
 * create a [PaginateSqls]
 * @param pageNo 1-based page number
 * @param pageSize page size
 */
fun <R : Record> SelectLimitStep<R>.paginate(
  pageNo: Int,
  pageSize: Int
) = PaginateSqls(
  DSL.selectCount().from(this),
  limit(pageSize).offset((pageNo - 1) * pageSize)
).also {
  it.attach(configuration())
}

/**
 * do pagination
 * @param pageNo 1-based page number
 * @param pageSize page size
 * @param fetchAction the action to fetch pagination data.
 *   `this` is [SelectForUpdateStep], `it` is the count. example:
 *   ```
 *   val page = jooq.selectFrom(t).where(condition).paginate(1, 10) {
 *     Page(count = it, list = fetchInto(MyPojo::class.java))
 *   }
 *   ```
 */
fun <R : Record, Result> SelectLimitStep<R>.paginate(
  pageNo: Int,
  pageSize: Int,
  fetchAction: SelectForUpdateStep<R>.(Long) -> Result
) = paginate(pageNo, pageSize).exec(fetchAction)

/**
 * [UpdatableRecord.insert] or [UpdatableRecord.store] will not return generated fields.
 * use this method to insert and return the generated fields. example:
 * ```
 * jooq.newRecord(t).apply {
 *   // change some fields
 * }.insertReturning().fetchOneInto(MyPojo::class.java)
 * ```
 */
fun <R : UpdatableRecord<R>> UpdatableRecord<R>.insertReturning() =
  configuration()!!.dsl().insertInto(table).set(this).returning()