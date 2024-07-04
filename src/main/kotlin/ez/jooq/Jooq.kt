package ez.jooq

import org.jooq.*

@Suppress("unused", "MemberVisibilityCanBePrivate")
class Jooq(
  /**
   * dsl context
   */
  val context: () -> DSLContext
) {
  constructor(conf: JooqConf) : this(conf::dsl)

  fun <R : Record> newRecord(t: Table<R>) = context().newRecord(t)

  fun <R : Record> selectFrom(t: Table<R>): JooqWhereStep<R> =
    JooqWhereStep(context().selectFrom(t))

  fun <R : Record> insertInto(t: Table<R>): InsertSetStep<R> = context().insertInto(t)

  fun <R : Record> update(t: Table<R>): UpdateSetFirstStep<R> = context().update(t)

  fun <R : Record> deleteFrom(t: Table<R>): DeleteUsingStep<R> = context().deleteFrom(t)

  fun tx(transactional: (jooq: Jooq) -> Unit) =
    context().transaction(TransactionalRunnable {
      val j = Jooq(it)
      transactional(j)
    })

  fun <T> txResult(transactional: (jooq: Jooq) -> T): T =
    context().transactionResult(TransactionalCallable {
      val j = Jooq(it)
      transactional(j)
    })
}
