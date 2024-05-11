package ez.jooq

import org.jooq.*

@Suppress("unused")
class Jooq(private val dsl: () -> DSLContext) {
  constructor(conf: JooqConf) : this(conf::dsl)

  fun context() = dsl()

  fun <R : Record> newRecord(t: Table<R>) = dsl().newRecord(t)

  fun <R : Record> selectFrom(t: Table<R>): JooqWhereStep<R> = JooqWhereStep(dsl().selectFrom(t))

  fun <R : Record> insertInto(t: Table<R>): InsertSetStep<R> = dsl().insertInto(t)

  fun <R : Record> update(t: Table<R>): UpdateSetFirstStep<R> = dsl().update(t)

  fun <R : Record> deleteFrom(t: Table<R>): DeleteUsingStep<R> = dsl().deleteFrom(t)

  fun tx(transactional: (jooq: Jooq) -> Unit) =
    dsl().transaction(TransactionalRunnable {
      val j = Jooq(it)
      transactional(j)
    })

  fun <T> txResult(transactional: (jooq: Jooq) -> T): T =
    dsl().transactionResult(TransactionalCallable {
      val j = Jooq(it)
      transactional(j)
    })
}
