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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.VersionedId;
import org.junit.Test;


public class P2DiffTester extends IUTester{
	
	@Test
	public void testGetRelativeComplementA() {
		Set<IInstallableUnit> A = createIUs(new VersionedId("foo", "1.0.0"), new VersionedId("bar", "1.0.0"), new VersionedId("baz", "1.0.0"));
		Set<IInstallableUnit> B = createIUs(new VersionedId("foo", "1.0.0"), new VersionedId("blah", "1.0.0"), new VersionedId("baz", "1.0.0"));
		P2Diff p2Diff = new P2Diff(null, A, null, B, new ArgumentProcessor());
		Set<IInstallableUnit> relativeComplementA = p2Diff.getRelativeComplementA().toSet();
		assertEquals(1, relativeComplementA.size());
		assertTrue(relativeComplementA.contains(createSimpleIU(new VersionedId("blah", "1.0.0"))));
	}

	@Test
	public void testGetRelativeComplementB() {
		Set<IInstallableUnit> A = createIUs(new VersionedId("foo", "1.0.0"), new VersionedId("bar", "1.0.0"), new VersionedId("baz", "1.0.0"));
		Set<IInstallableUnit> B = createIUs(new VersionedId("foo", "1.0.0"), new VersionedId("blah", "1.0.0"), new VersionedId("baz", "1.0.0"));
		P2Diff p2Diff = new P2Diff(null, A, null, B, new ArgumentProcessor());
		Set<IInstallableUnit> relativeComplementB = p2Diff.getRelativeComplementB().toSet();
		assertEquals(1, relativeComplementB.size());
		assertTrue(relativeComplementB.contains(createSimpleIU(new VersionedId("bar", "1.0.0"))));
	}
}
