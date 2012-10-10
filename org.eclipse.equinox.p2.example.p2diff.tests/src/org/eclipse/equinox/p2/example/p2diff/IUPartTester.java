/*******************************************************************************
 *  Copyright (c) 2012 EclipseSource
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 * 
 *  Contributors:
 *     EclipseSource - initial API and implementation
 *******************************************************************************/
package org.eclipse.equinox.p2.example.p2diff;

import org.eclipse.equinox.p2.example.p2diff.IUPart;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the IUPart Data Structure
 * 
 * @author Ian Bull
 */
public class IUPartTester {
	
	@Test
	public void testIgnoreFinalArg() {
		IUPart iuPart1 = IUPart.createIUPart(new Object(), true, "foo", "bar", "baz");
		IUPart iuPart2 = IUPart.createIUPart(new Object(), true, "foo", "bar", "missing");
		assertTrue(iuPart1.equals(iuPart2));
		assertTrue(iuPart1.hashCode() == iuPart2.hashCode());
	}
	
	@Test
	public void testIgnoreFinalArg2() {
		IUPart iuPart1 = IUPart.createIUPart(new Object(), false, "foo", "bar", "baz");
		IUPart iuPart2 = IUPart.createIUPart(new Object(), false, "foo", "bar", "missing");
		assertFalse(iuPart1.equals(iuPart2));
	}
	
	@Test
	public void testUseFinalArg() {
		IUPart iuPart1 = IUPart.createIUPart(new Object(), false, "foo", "bar", "baz");
		IUPart iuPart2 = IUPart.createIUPart(new Object(), false, "foo", "bar", "baz");
		assertTrue(iuPart1.equals(iuPart2));
		assertTrue(iuPart1.hashCode() == iuPart2.hashCode());
	}
	
	@Test
	public void testUseFinalArg2() {
		IUPart iuPart1 = IUPart.createIUPart(new Object(), true, "foo", "bar", "baz");
		IUPart iuPart2 = IUPart.createIUPart(new Object(), true, "foo", "bar", "baz");
		assertTrue(iuPart1.equals(iuPart2));
		assertTrue(iuPart1.hashCode() == iuPart2.hashCode());
	}
}
