package com.develetter.develetter.jobposting.batch

import com.develetter.develetter.jobposting.entity.JobPosting
import com.develetter.develetter.jobposting.entity.QJobPosting
import com.querydsl.jpa.JPQLQuery
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.EntityTransaction
import org.springframework.batch.item.database.AbstractPagingItemReader
import org.springframework.dao.DataAccessResourceFailureException
import org.springframework.util.ClassUtils
import org.springframework.util.CollectionUtils
import java.util.concurrent.CopyOnWriteArrayList
import java.util.function.Function

class QuerydslPagingItemReader<T>(
    private val entityManagerFactory: EntityManagerFactory,
    pageSize: Int,
    private val transacted: Boolean = true,
    private val queryFunction: Function<JPAQueryFactory, JPAQuery<T>>
) : AbstractPagingItemReader<T>() {

    private val jpaPropertyMap: MutableMap<String, Any> = HashMap()
    private lateinit var entityManager: EntityManager
    private var lastId: Long? = null

    init {
        setName(ClassUtils.getShortName(QuerydslPagingItemReader::class.java))
        setPageSize(pageSize)
    }

    @Throws(Exception::class)
    override fun doOpen() {
        super.doOpen()
        entityManager = entityManagerFactory.createEntityManager(jpaPropertyMap)
        if (!::entityManager.isInitialized) {
            throw DataAccessResourceFailureException("Unable to obtain an EntityManager")
        }
    }

    override fun doReadPage() {
        val tx = getTxOrNull()

        val query: JPQLQuery<T> = createQuery()
            .where(lastId?.let { QJobPosting.jobPosting.id.gt(it) })
            .limit(pageSize.toLong())

        initResults()
        fetchQuery(query, tx)

        if (results.isNotEmpty()) {
            val lastEntity = results[results.size - 1]
            lastId = extractIdFromEntity(lastEntity)
        }
    }

    private fun extractIdFromEntity(entity: T): Long {
        return (entity as JobPosting).id!!
    }

    private fun getTxOrNull(): EntityTransaction? {
        return if (transacted) {
            val tx = entityManager.transaction
            tx.begin()
            entityManager.flush()
            entityManager.clear()
            tx
        } else {
            null
        }
    }

    private fun createQuery(): JPAQuery<T> {
        val queryFactory = JPAQueryFactory(entityManager)
        return queryFunction.apply(queryFactory)
    }

    private fun initResults() {
        if (CollectionUtils.isEmpty(results)) {
            results = CopyOnWriteArrayList()
        } else {
            results.clear()
        }
    }

    /**
     * where 의 조건은 id max/min 을 이용한 제한된 범위를 가지게 한다
     * @param query
     * @param tx
     */
    private fun fetchQuery(query: JPQLQuery<T>, tx: EntityTransaction?) {
        if (transacted) {
            results.addAll(query.fetch())
            tx?.commit()
        } else {
            val queryResult: List<T> = query.fetch()
            for (entity in queryResult) {
                entityManager.detach(entity)
                results.add(entity)
            }
        }
    }

    override fun jumpToItem(itemIndex: Int) {
        // Override jumpToItem if needed
    }

    @Throws(Exception::class)
    override fun doClose() {
        entityManager.close()
        super.doClose()
    }
}
