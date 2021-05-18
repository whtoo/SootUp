package de.upb.swt.soot.core.jimple.visitor;

/*-
 * #%L
 * Soot - a J*va Optimization Framework
 * %%
 * Copyright (C) 1997-2020 Etienne Gagnon, Linghui Luo, Christian Brüggemann
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
 * #L%
 */

import de.upb.swt.soot.core.jimple.common.constant.*;

public interface ConstantVisitor extends Visitor {
  void caseBooleanConstant(BooleanConstant constant);

  void caseDoubleConstant(DoubleConstant constant);

  void caseFloatConstant(FloatConstant constant);

  void caseIntConstant(IntConstant constant);

  void caseLongConstant(LongConstant constant);

  void caseNullConstant(NullConstant constant);

  void caseStringConstant(StringConstant constant);

  void caseClassConstant(ClassConstant constant);

  void caseMethodHandle(MethodHandle handle);

  void caseMethodType(MethodType methodType);

  void defaultCase(Constant constant);
}
