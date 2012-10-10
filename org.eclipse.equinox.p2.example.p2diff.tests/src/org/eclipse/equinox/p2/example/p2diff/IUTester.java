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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.MetadataFactory;
import org.eclipse.equinox.p2.metadata.VersionedId;
import org.eclipse.equinox.p2.metadata.MetadataFactory.InstallableUnitDescription;
import org.eclipse.equinox.p2.metadata.Version;

public abstract class IUTester {

	protected IInstallableUnit createSimpleIU(String id) {
		InstallableUnitDescription installableUnitDescription = new MetadataFactory.InstallableUnitDescription();
		installableUnitDescription.setId(id);
		return MetadataFactory.createInstallableUnit(installableUnitDescription);
	} 
	
	protected IInstallableUnit createSimpleIU(VersionedId versionedId) {
		InstallableUnitDescription installableUnitDescription = new MetadataFactory.InstallableUnitDescription();
		installableUnitDescription.setId(versionedId.getId());
		installableUnitDescription.setVersion(versionedId.getVersion());
		return MetadataFactory.createInstallableUnit(installableUnitDescription);
	}
	
	protected IInstallableUnit createSimpleIU(String id, Version version) {
		InstallableUnitDescription installableUnitDescription = new MetadataFactory.InstallableUnitDescription();
		installableUnitDescription.setId(id);
		installableUnitDescription.setVersion(version);
		return MetadataFactory.createInstallableUnit(installableUnitDescription);
	}
	
	
	protected Set<IInstallableUnit> createIUs(VersionedId... versionedIds) {
		Set<IInstallableUnit> results = new HashSet<IInstallableUnit>();
		for (VersionedId versionedId : versionedIds) {
			results.add(createSimpleIU(versionedId));
		}
		return results;
	}
	
}
