package com.theiterators.utils

import scala.reflect.runtime.universe._

object FunctionCompositionSolver {
  class F[+T: TypeTag, +R: TypeTag](function: T => R) {

    def apply(x: Any) : R = function(x.asInstanceOf[T])

    val domain = typeOf[T]
    val codomain = typeOf[R]

    override def toString = domain.toString + " => " + codomain.toString
  }

  object F {
    def apply[T: TypeTag, R: TypeTag](function: T => R) = new F(function)
  }

  def solve[T: TypeTag, R: TypeTag](list: List[F[Any,Any]]) : Option[F[T, R]] = {
    // helper aliases
    type Function = F[Any, Any]
    type Path = List[Function]
    type PathsList = List[Path]

    def findPath(current: PathsList, possible: List[Function]) : Path = {
      def extend(path: Path, possible: List[Function]) : PathsList = {
        val feasible = possible.filter((y: Function) => path.head.codomain =:= y.domain)
        val unique = feasible.filter((y: Function) => !path.exists((z: Function) => z.domain =:= y.domain && z.codomain =:= y.codomain))
        unique.map((x: Function) => x :: path)
      }

      if (current.isEmpty)
        return List[F[Any,Any]]()

      val extended = current.map((x: Path) => extend(x, possible)).flatten

      val matching = extended.filter((p: Path) => p.head.codomain =:= typeOf[R])

      if (matching.isEmpty)
        findPath(extended, possible)
      else
        matching.head.reverse
    }

    val initial = list.filter(f => f.domain =:= typeOf[T]).map((x: Function) => List[Function](x))
    val path = findPath(initial, list)
    if (path.isEmpty) {
      None
    } else {
      Some(F((x: Any) => path.foldLeft(x)((r, c) => c.apply(r))).asInstanceOf[F[T, R]])
    }
  }
}
