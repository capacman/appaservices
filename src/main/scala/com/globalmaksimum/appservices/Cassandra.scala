package com.globalmaksimum.appservices

import me.prettyprint.hector.api.Keyspace
import me.prettyprint.hector.api.factory.HFactory
import me.prettyprint.hector.api.mutation.Mutator
import me.prettyprint.cassandra.serializers.UUIDSerializer
import me.prettyprint.hector.api.Serializer
import me.prettyprint.cassandra.serializers.LongSerializer

import java.util.UUID

package object hector {
  type KeySerializer[K] = () => Serializer[K]
  type KeyExtractor[A, K] = A => K
  type ColumnFamilyExtractor[A] = A => String
  type ColumnExtractor[A, K] = A => (K, String, Mutator[K]) => Unit

  trait Hector {

    def keyspace: Keyspace

    def insert[A, Key](instance: A)(implicit keySerializer: KeySerializer[Key],
      keyExtractor: KeyExtractor[A, Key],
      columnFamilyExtractor: ColumnFamilyExtractor[A],
      columnExtractor: ColumnExtractor[A, Key]) {
      val mutator = HFactory.createMutator(keyspace, keySerializer())
      val key = keyExtractor(instance)
      val columnFamily = columnFamilyExtractor(instance)
      columnExtractor(instance)(key, columnFamily, mutator)

      mutator.execute()
    }

  }

  trait UUIDKeySerializer {
    implicit object UUIDKeySerializer extends KeySerializer[UUID] {
      def apply() = UUIDSerializer.get()
    }
  }

  trait LongKeySerialiser {
    implicit object LongKeySerialiser extends KeySerializer[java.lang.Long] {
      def apply() = LongSerializer.get()
    }
  }

  trait IdKeyExtractor {
    class IdKeyExtractor[A <: IDEntity[K], K]
      extends KeyExtractor[A, K] {
      def apply(value: A) = value.id
    }
  }

  trait UUIDIdKeyExtractor extends IdKeyExtractor {
    implicit def UUIDIdKeyExtractor[A <: IDEntity[UUID]] = new IdKeyExtractor[A, UUID]
  }

  trait LongIdKeyExtractor extends IdKeyExtractor {
    implicit def LongIdKeyExtractor[A <: IDEntity[java.lang.Long]] = new IdKeyExtractor[A, java.lang.Long]
  }

  trait TypeNameColumnFamilyExtractor {
    implicit object TypeNameColumnFamilyExtractor
      extends ColumnFamilyExtractor[AnyRef] {
      def apply(v1: AnyRef) = v1.getClass.getSimpleName.toLowerCase
    }
  }
  trait UserColumnExtractor {
    implicit def UserColumnExtractor[K] = new ColumnExtractor[User, K] {
      def apply(value: User) = {
        (key: K, columnFamily: String, mutator: Mutator[K]) =>
          mutator.addInsertion(key, columnFamily,
            HFactory.createStringColumn("name", value.name))
          mutator.addInsertion(key, columnFamily,
            HFactory.createStringColumn("surname", value.surname))
          mutator.addInsertion(key, columnFamily,
            HFactory.createStringColumn("username", value.username))
          ()
      }
    }
  }
}


