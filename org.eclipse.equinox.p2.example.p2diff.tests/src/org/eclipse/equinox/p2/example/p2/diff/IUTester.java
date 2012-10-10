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
package org.eclipse.equinox.p2.example.p2.diff;

import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.MetadataFactory;
import org.eclipse.equinox.p2.metadata.MetadataFactory.InstallableUnitDescription;

public abstract class IUTester {

	protected IInstallableUnit createSimpleIU(String id) {
		InstallableUnitDescription installableUnitDescription = new MetadataFactory.InstallableUnitDescription();
		installableUnitDescription.setId(id);
		return MetadataFactory.createInstallableUnit(installableUnitDescription);
	}
	
}
