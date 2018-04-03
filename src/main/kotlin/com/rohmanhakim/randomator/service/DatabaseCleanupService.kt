package com.rohmanhakim.randomator.service

import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.Table
import javax.persistence.metamodel.Metamodel
import kotlin.reflect.full.findAnnotation

@Service
class DatabaseCleanupService
@Autowired constructor(private val entityManager: EntityManager) : InitializingBean {

  lateinit var tableNames: List<String>

  /**
   * Uses the JPA metamodel to find all managed types then try to get the [Table] annotation's from each (if present) to discover the table name.
   * If the [Table] annotation is not defined then we skip that entity
   */
  override fun afterPropertiesSet() {
    val metaModel: Metamodel = entityManager.metamodel
    tableNames = metaModel.managedTypes
            .filter {
              it.javaType.kotlin.findAnnotation<Table>() != null
            }
            .map {
              val tableAnnotation: Table? = it.javaType.kotlin.findAnnotation()
              tableAnnotation?.name ?: throw IllegalStateException("should never get here")
            }
  }

  /**
   * Utility method that truncates all identified tables
   */
  @Transactional
  fun truncate() {
    entityManager.flush()
    entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate()
    tableNames.forEach { tableName ->
      entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate()
    }
    entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate()
  }

}