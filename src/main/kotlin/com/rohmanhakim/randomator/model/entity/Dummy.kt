package com.rohmanhakim.randomator.model.entity

import com.rohmanhakim.randomator.util.BypassableSequenceGenerator
import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "dummy")
data class Dummy(
        @Id
        @GenericGenerator(
                name = "bypassable-sequence-generator",
                strategy = BypassableSequenceGenerator.QUALIFIED_NAME)
        @GeneratedValue(
                generator = "bypassable-sequence-generator",
                strategy = GenerationType.SEQUENCE)
        @Column(name = "id", updatable = false, nullable = false)
        override val id: Long? = null,

        var data: String = "",

        val createdAt: Date = Date()

) : Identifiable<Long?>