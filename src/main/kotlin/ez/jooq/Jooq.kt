package ez.jooq

import org.jooq.*

@Suppress("unused")
class Jooq(private val dsl: () -> DSLContext) {
  constructor(conf: JooqConf) : this(conf::dsl)

  fun context() = dsl()

  fun <R : Record?> newRecord(t: Table<R>) = dsl().newRecord(t)

  fun <R : Record?> selectFrom(t: Table<R>): JooqWhereStep<R> {
    return JooqWhereStep(dsl().selectFrom(t))
  }

  fun <R : Record?> insertInto(t: Table<R>): InsertSetStep<R> {
    return dsl().insertInto(t)
  }

  fun <R : Record?> update(t: Table<R>): UpdateSetFirstStep<R> {
    return dsl().update(t)
  }

  fun <R : Record?> deleteFrom(t: Table<R>): DeleteUsingStep<R> {
    return dsl().deleteFrom(t)
  }

  fun tx(transactional: (jooq: Jooq) -> Unit) {
    dsl().transaction(TransactionalRunnable {
      val j = Jooq(it)
      transactional(j)
    })
  }
}