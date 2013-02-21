package com.globalmaksimum.appservices

import org.scalatest.FlatSpec
import org.scalatest.BeforeAndAfter
import me.prettyprint.hector.api.Cluster
import me.prettyprint.hector.api.factory.HFactory
import com.globalmaksimum.appservices.hector.LongIdKeyExtractor
import com.globalmaksimum.appservices.hector.LongKeySerialiser
import com.globalmaksimum.appservices.hector.TypeNameColumnFamilyExtractor
import com.globalmaksimum.appservices.hector.UserColumnExtractor

class DaoTests extends FlatSpec with BeforeAndAfter with LongIdKeyExtractor with LongKeySerialiser
  with TypeNameColumnFamilyExtractor with UserColumnExtractor {
  val myCluster: Cluster = HFactory.getOrCreateCluster("Test Cluster", "localhost:9160");
  val keySpace = HFactory.createKeyspace("appservices", myCluster)

  before {
    println("before")
  }

  after {
    println("after")
  }

  "testing " should "be easy " in {
    val userDao = new UserDao(keySpace)
    userDao.insert(User(1l, "anil", "halil", "capacman")) 

  }
  it should "repeat " in {

  }
}