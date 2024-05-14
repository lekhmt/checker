package com.example.checker.configuration

import org.babyfish.jimmer.View
import org.babyfish.jimmer.sql.fetcher.ViewMetadata
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.query.KMutableRootQuery
import kotlin.reflect.KClass

fun <E : Any, V : View<E>> KSqlClient.findAll(
    viewType: KClass<V>,
    block: KMutableRootQuery<E>.() -> Unit = {},
): List<V> {
    val metadata = ViewMetadata.of(viewType.java)
    return createQuery(metadata.fetcher.javaClass.kotlin) {
        block()
        select(table.fetch(metadata.fetcher))
    }.execute().map(metadata.converter::apply)
}