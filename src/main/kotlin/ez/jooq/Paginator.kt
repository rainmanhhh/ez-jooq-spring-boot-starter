package ez.jooq

import org.jooq.*
import org.jooq.impl.DSL
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import kotlin.Long

@Suppress("MemberVisibilityCanBePrivate", "unused")
/**
 * @param limitStep the sql to be paginated
 * @param pageNo 1-based page number
 * @param pageSize page size
 */
class Paginator<R : Record>(
  limitStep: SelectLimitStep<R>,
  val pageNo: Int,
  val pageSize: Int
) : Attachable {
  val countSql: SelectJoinStep<Record1<Int>> = DSL.selectCount().from(limitStep)
  val fetchSql: SelectForUpdateStep<R> =
    DSL.selectFrom(limitStep).limit(pageSize).offset((pageNo - 1) * pageSize)

  /**
   * run countSql and fetchSql to get total count and data list,
   * then convert the result with [fetchAction]. example:
   * ```
   * val page = paginateSqls.exec {
   *   fetchInto(MyPojo::class.java)
   * }
   * ```
   * @param fetchAction the action to fetch data list:
   *   `this` is [SelectForUpdateStep]; `it` is the count; result should be data item in Page list
   */
  fun <Item, Result : List<Item>> exec(fetchAction: SelectForUpdateStep<R>.() -> Result): Page<Item> =
    PageImpl(fetch(fetchAction), PageRequest.of(pageNo - 1, pageSize), count())

  /**
   * run countSql to get total count. example:
   * ```
   * val totalCount: Long = paginateSqls.count()
   * ```
   */
  fun count() = countSql.fetchOneInto(Long::class.java)!!

  /**
   * run fetchSql to get data list. example:
   * ```
   * val list: List<MyPojo> = paginateSqls.fetch { fetchInto(MyPojo::class.java) }
   * ```
   */
  fun <Result> fetch(fetchAction: SelectForUpdateStep<R>.() -> Result) = fetchSql.fetchAction()

  override fun attach(configuration: Configuration?) {
    countSql.attach(configuration)
    fetchSql.attach(configuration)
  }

  override fun detach() {
    attach(null)
  }

  override fun configuration(): Configuration? {
    return countSql.configuration()
  }
}
