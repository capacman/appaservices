package com.globalmaksimum.appservices

import com.globalmaksimum.appservices.hector._
import me.prettyprint.hector.api.Keyspace
import me.prettyprint.hector.api.mutation.Mutator
import me.prettyprint.hector.api.factory.HFactory

class UserDao(val keyspace: Keyspace) extends Hector {
}