package com.theiterators.utils

import scala.reflect.runtime.universe._
import org.scalatest._
import com.theiterators.utils.FunctionCompositionSolver._

class ExampleSpec extends FlatSpec with Matchers {
  def intToLong(x: Int) : Long = x
  def longToString(x: Long) : String = x.toString
  def stringToDouble(x: String) : Double = x.toDouble
  def doubleToBoolean(x: Double) : Boolean = x > 0

  def stringToShort(x: String) : Short = 0
  def shortToChar(x: Short) : Char = 'a'
  def intToInt(x: Int) = x

  "A Function" should "have proper domain" in {
    val f = F(intToLong)
    (f.domain =:= typeOf[Int]) should be (true)
  }

  it should "have proper codomain" in {
    val f = F(intToLong)
    (f.codomain =:= typeOf[Long]) should be (true)
  }

  it should "respond to apply like typical function" in {
    val f = F(longToString)
    val ret = f(42L)
    ret should be ("42")
  }

  it should "have pretty String representation" in {
    val f = F(longToString)
    f.toString should be ("Long => String")
  }

  "FunctionCompositionSolver" should "find composition path" in {
    val list1 = List(F(intToLong), F(longToString), F(stringToDouble), F(doubleToBoolean), F(intToInt), F(stringToShort), F(shortToChar))
    val list2 = list1.reverse
    val list3 = List(F(longToString), F(stringToDouble), F(doubleToBoolean), F(intToLong), F(intToInt), F(stringToShort), F(shortToChar))

    val ret1 = solve[Int, Boolean](list1)
    val ret2 = solve[Int, Boolean](list2)
    val ret3 = solve[Int, Boolean](list3)

    ret1 should not be ('empty)
    ret2 should not be ('empty)
    ret3 should not be ('empty)

    (ret1.head.apply(42)) should be (true)
    (ret2.head.apply(-42)) should be (false)
    (ret3.head.apply(1)) should be (true)
  }

  it should "return Nothing if there's no path" in {
    val list = List(F(intToLong), F(longToString), F(stringToDouble))
    val ret = solve[Int, Boolean](list)
    ret should be ('empty)
  }

  it should "not got stuck in infinite loop if there's type cycle" in {
    val list = List(F(intToLong), F(longToString), F(stringToDouble), F(intToInt))
    val ret = solve[Int, Boolean](list)
    ret should be ('empty)
  }
}

object Runner extends App {
  (new ExampleSpec).execute()
}
