package sootup.core.jimple.common.stmt;

/*-
 * #%Value
 * Soot - a J*va Optimization Framework
 * %%
 * Copyright (C) 1997-2020 Etienne Gagnon, Linghui Luo, Markus Schmidt and others
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #Value%
 */

import javax.annotation.Nonnull;
import sootup.core.jimple.basic.*;
import sootup.core.jimple.common.expr.AbstractInvokeExpr;
import sootup.core.jimple.common.expr.Expr;
import sootup.core.jimple.common.ref.ConcreteRef;
import sootup.core.jimple.common.ref.JArrayRef;
import sootup.core.jimple.common.ref.JFieldRef;
import sootup.core.jimple.common.ref.JInstanceFieldRef;
import sootup.core.jimple.visitor.StmtVisitor;
import sootup.core.util.printer.StmtPrinter;

/** Represents the assignment of one value to another */
public final class JAssignStmt extends AbstractDefinitionStmt implements FallsThroughStmt {

  @Nonnull final LValue leftOp;
  @Nonnull final Value rightOp;

  /**
   * Instantiates a new JAssignStmt.
   *
   * @param variable the variable on the left side of the assign statement.
   * @param rValue the value on the right side of the assign statement.
   */
  public JAssignStmt(
      @Nonnull LValue variable, @Nonnull Value rValue, @Nonnull StmtPositionInfo positionInfo) {
    super(positionInfo);
    leftOp = variable;
    rightOp = rValue;

    if (!validateValue(rValue)) {
      throw new RuntimeException(
          "Illegal Assignment statement. Make sure that right hand side ("
              + rValue
              + ") is a valid operand.");
    }
  }

  /**
   * returns true if rValue can be on the right side of the assign statement
   *
   * @param rValue the value on the right side of the assign statement.
   */
  private boolean validateValue(@Nonnull Value rValue) {
    // constant | local     |  *FieldRef | ArrayRef     | Expr ----> i.e. not IdentityRef
    return rValue instanceof Immediate || rValue instanceof ConcreteRef || rValue instanceof Expr;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.upb.sootup.jimple.common.stmt.AbstractStmt#containsInvokeExpr()
   */
  @Override
  public boolean containsInvokeExpr() {
    return getRightOp() instanceof AbstractInvokeExpr;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.upb.sootup.jimple.common.stmt.AbstractStmt#getInvokeExpr()
   */
  @Override
  public AbstractInvokeExpr getInvokeExpr() {
    if (!containsInvokeExpr()) {
      throw new RuntimeException("getInvokeExpr() called with no invokeExpr present!");
    }
    return (AbstractInvokeExpr) getRightOp();
  }

  /*
   * (non-Javadoc)
   *
   * @see de.upb.sootup.jimple.common.stmt.AbstractStmt#containsArrayRef()
   */
  /* added by Feng */
  @Override
  public boolean containsArrayRef() {
    return ((getLeftOp() instanceof JArrayRef) || (getRightOp() instanceof JArrayRef));
  }

  /*
   * (non-Javadoc)
   *
   * @see de.upb.sootup.jimple.common.stmt.AbstractStmt#getArrayRef()
   */
  // TODO [ms]: what is with assignments like: arr[0] = arr[6]? is that possible? if not ->
  // validator
  public JArrayRef getArrayRef() {
    if (getLeftOp() instanceof JArrayRef) {
      return (JArrayRef) getLeftOp();
    } else if (getRightOp() instanceof JArrayRef) {
      return (JArrayRef) getRightOp();
    } else {
      throw new RuntimeException("getArrayRef() called with no ArrayRef present!");
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see de.upb.sootup.jimple.common.stmt.AbstractStmt#containsFieldRef()
   */
  @Override
  public boolean containsFieldRef() {
    return ((getLeftOp() instanceof JFieldRef) || (getRightOp() instanceof JFieldRef));
  }

  /*
   * (non-Javadoc)
   *
   * @see de.upb.sootup.jimple.common.stmt.AbstractStmt#getFieldRef()
   */
  public JFieldRef getFieldRef() {
    // TODO: [MS] what if both Op's are a FieldRef? verify it in a verifier that this does not
    // happen or is it always handled via an intermediate Local?
    if (getLeftOp() instanceof JFieldRef) {
      return (JFieldRef) getLeftOp();
    } else if (getRightOp() instanceof JFieldRef) {
      return (JFieldRef) getRightOp();
    } else {
      throw new RuntimeException("getFieldRef() called with no JFieldRef present!");
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return getLeftOp() + " = " + getRightOp();
  }

  /*
   * (non-Javadoc)
   *
   * @see de.upb.sootup.jimple.common.stmt.Stmt#toString(de.upb.sootup.StmtPrinter)
   */
  @Override
  public void toString(@Nonnull StmtPrinter up) {
    getLeftOp().toString(up);
    up.literal(" = ");
    getRightOp().toString(up);
  }

  /*
   * (non-Javadoc)
   *
   * @see de.upb.sootup.jimple.common.stmt.AbstractStmt#accept(de.upb.sootup.jimple.visitor.Visitor)
   */
  @Override
  public void accept(@Nonnull StmtVisitor sw) {
    sw.caseAssignStmt(this);
  }

  @Override
  public boolean equivTo(Object o, @Nonnull JimpleComparator comparator) {
    return comparator.caseAssignStmt(this, o);
  }

  @Override
  public int equivHashCode() {
    return getLeftOp().equivHashCode() + 31 * getRightOp().equivHashCode();
  }

  @Nonnull
  @Override
  public LValue getLeftOp() {
    return leftOp;
  }

  @Nonnull
  @Override
  public Value getRightOp() {
    return rightOp;
  }

  @Override
  public boolean fallsThrough() {
    return true;
  }

  @Override
  public boolean branches() {
    return false;
  }

  @Nonnull
  @Override
  public JAssignStmt withNewDef(@Nonnull Local newLocal) {
    // "ReplaceDefVisitor"
    final Value leftOp = getLeftOp();
    LValue newVal;
    if (leftOp instanceof ConcreteRef) {
      if (leftOp instanceof JArrayRef) {
        newVal = ((JArrayRef) leftOp).withBase(newLocal);
      } else if (leftOp instanceof JInstanceFieldRef) {
        newVal = ((JInstanceFieldRef) leftOp).withBase(newLocal);
      } else {
        // JStaticFieldRef -> do nothing
        return this;
      }
    } else {
      // it's a Local
      newVal = newLocal;
    }
    return withVariable(newVal);
  }

  @Nonnull
  public JAssignStmt withVariable(@Nonnull LValue variable) {
    return new JAssignStmt(variable, getRightOp(), getPositionInfo());
  }

  @Nonnull
  public JAssignStmt withRValue(@Nonnull Value rValue) {
    return new JAssignStmt(getLeftOp(), rValue, getPositionInfo());
  }

  @Nonnull
  public JAssignStmt withPositionInfo(@Nonnull StmtPositionInfo positionInfo) {
    return new JAssignStmt(getLeftOp(), getRightOp(), positionInfo);
  }
}
