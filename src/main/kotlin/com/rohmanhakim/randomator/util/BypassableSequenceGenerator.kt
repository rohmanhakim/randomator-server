package com.rohmanhakim.randomator.util

import com.rohmanhakim.randomator.model.entity.Identifiable
import org.hibernate.HibernateException
import org.hibernate.engine.spi.SessionImplementor
import org.hibernate.id.enhanced.SequenceStyleGenerator
import java.io.Serializable

class BypassableSequenceGenerator : SequenceStyleGenerator() {
  companion object {
    const val QUALIFIED_NAME = "com.rohmanhakim.randomator.util.BypassableSequenceGenerator"
  }

  override fun generate(session: SessionImplementor?, obj: Any?): Serializable? {
    if (obj == null) throw HibernateException(NullPointerException())

    return if ((obj as Identifiable<*>?)?.id == null) {
      super.generate(session, obj)
    } else {
      obj.id
    }
  }
}