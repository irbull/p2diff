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

import static org.junit.Assert.*;

import org.eclipse.equinox.p2.example.p2diff.DeepIUCompare;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.MetadataFactory;
import org.eclipse.equinox.p2.metadata.MetadataFactory.InstallableUnitDescription;
import org.junit.Test;

public class DeepIUCompareTester extends IUTester {
	
	@Test
	public void testIAE() {
		try {
			new DeepIUCompare(createSimpleIU("foo"), createSimpleIU("bar"));
			fail("Should have thrown an IAE.");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testNoIAE() {
		try {
			new DeepIUCompare(createSimpleIU("org.foo"), createSimpleIU(new String("org.foo")));
			// expected
		} catch (IllegalArgumentException e) {
			fail("Should NOT have thrown an IAE.");
		}
	}

	
}
