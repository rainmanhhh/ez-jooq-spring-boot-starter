package ez.jooq

import org.jooq.*
import kotlin.Long

@Suppress("MemberVisibilityCanBePrivate")
data class PaginateHelper<R : Record>(
  val countSql: SelectJoinStep<Record1<Int>>,
  val fetchSql: SelectForUpdateStep<R>
) : Attachable {
  /**
   * run countSql and fetchSql to get total count and data list. example:
   * ```
   * val page = paginateHelper.exec {
   *   Page(count = it, list = fetchInto(MyPojo::class.java))
   * }
   * ```
   * @param fetchAction the action to fetch data list. `this` is [SelectForUpdateStep], `it` is the count
   */
  fun <Result> exec(fetchAction: SelectForUpdateStep<R>.(Long) -> Result): Result {
    return fetchSql.fetchAction(count())
  }

  /**
   * run countSql to get total count. example:
   * ```
   * val totalCount: Long = paginateHelper.count()
   * ```
   */
  fun count() = countSql.fetchOneInto(Long::class.java)!!

  /**
   * run fetchSql to get data list. example:
   * ```
   * val list: List<MyPojo> = paginateHelper.fetch { fetchInto(MyPojo::class.java) }
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
