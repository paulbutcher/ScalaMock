package org.scalamock.util

class MacroAdapter[C <: MacroAdapter.Context](val ctx2: C) {
  import ctx2.universe._

  def freshName(prefix: String) = ctx2.fresh(prefix)
  def freshTerm(prefix: String): TermName = TermName(freshName(prefix))
  def internalTypeRef(pre: Type, sym: Symbol, args: List[Type]) = internal.typeRef(pre, sym, args)
  def internalSuperType(thistpe: Type, supertpe: Type): Type = internal.superType(thistpe, supertpe)
  def internalThisType(thistpe: Symbol) = internal.thisType(thistpe)
  def internalTypeDef(p: Symbol): TypeDef = internal.typeDef(p)
}

object MacroAdapter {
  type Context = scala.reflect.macros.blackbox.Context
}
