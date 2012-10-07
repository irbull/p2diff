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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.equinox.internal.p2.metadata.IRequiredCapability;
import org.eclipse.equinox.p2.metadata.IArtifactKey;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IProvidedCapability;
import org.eclipse.equinox.p2.metadata.IRequirement;
import org.eclipse.equinox.p2.metadata.ITouchpointData;
import org.eclipse.equinox.p2.metadata.ITouchpointInstruction;

public class IUDiffer {

	private Collection<IUPart> retativeComplementA = null;
	private Collection<IUPart> retativeComplementB = null;

	public IUDiffer(IInstallableUnit iuA, IInstallableUnit iuB, boolean ignoreVersionDifferences) {
		retativeComplementA = computeRelativeComplement(iuA, iuB, ignoreVersionDifferences);
		retativeComplementB = computeRelativeComplement(iuB, iuA, ignoreVersionDifferences);
	}
	
	public Collection<IUPart> getRetativeComplementA() {
		return this.retativeComplementA;
	}

	public Collection<IUPart> getRetativeComplementB() {
		return this.retativeComplementB;
	}
	
	private static Collection<IUPart> computeRelativeComplement(IInstallableUnit iuA, IInstallableUnit iuB, boolean ignoreVersionDifferences) {
		Collection<IUPart> result = new ArrayList<IUPart>();
		result.addAll(getPropertyDifferences(iuA.getProperties(), iuB.getProperties(), ignoreVersionDifferences));
		result.addAll(getRequirementDifferences(iuA.getRequirements(), iuB.getRequirements(), ignoreVersionDifferences));
		result.addAll(getProvidedCapabilityDifferences(iuA.getProvidedCapabilities(), iuB.getProvidedCapabilities(), ignoreVersionDifferences));
		result.addAll(getTouchpointDataDifferences(iuA.getTouchpointData(), iuB.getTouchpointData(), ignoreVersionDifferences));
		result.addAll(getArtifactDifferences(iuA.getArtifacts(), iuB.getArtifacts(), ignoreVersionDifferences));
		return result;
	}

	private static Collection<IUPart> getComplement(Set<IUPart> iu1, Set<IUPart> iu2) {
		Set<IUPart> s = new HashSet<IUPart>(iu1);
		s.removeAll(iu2);
		return s;
	}
	private static Collection<IUPart> getArtifactDifferences(
			Collection<IArtifactKey> artifacts,
			Collection<IArtifactKey> artifacts2,
			boolean ignoreVersionDifferences) {
		Set<IUPart> iu1 = new HashSet<IUPart>();
		Set<IUPart> iu2 = new HashSet<IUPart>();
		for (IArtifactKey iArtifactKey : artifacts) {
			iu1.add(IUPart.createIUPart(iArtifactKey, ignoreVersionDifferences, iArtifactKey.getClassifier(), iArtifactKey.getId(), iArtifactKey.getVersion().toString()));
		}
		for (IArtifactKey iArtifactKey : artifacts2) {
			iu2.add(IUPart.createIUPart(iArtifactKey, ignoreVersionDifferences, iArtifactKey.getClassifier(), iArtifactKey.getId(), iArtifactKey.getVersion().toString()));
		}
		return getComplement(iu1, iu2);
	}

	private static Collection<IUPart> getTouchpointDataDifferences(
			Collection<ITouchpointData> touchpointData,
			Collection<ITouchpointData> touchpointData2,
			boolean ignoreVersionDifferences) {
		Set<IUPart> iu1 = new HashSet<IUPart>();
		Set<IUPart> iu2 = new HashSet<IUPart>();
		for (ITouchpointData element : touchpointData) {
			iu1.add(IUPart.createIUPart(element, false, asString(element.getInstructions())));
		}
		for (ITouchpointData element : touchpointData2) {
			iu2.add(IUPart.createIUPart(element, false, asString(element.getInstructions())));
		}
		return getComplement(iu1, iu2);
	}

	private static String asString(
			Map<String, ITouchpointInstruction> instructions) {
		StringBuilder result = new StringBuilder();
		for (Entry<String, ITouchpointInstruction> item : instructions.entrySet()) {
			result.append(item.getKey() + ":" + item.getValue().getImportAttribute() + ":" + item.getValue().getBody());
		}
		return result.toString();
	}

	private static Collection<IUPart> getProvidedCapabilityDifferences(
			Collection<IProvidedCapability> providedCapabilities,
			Collection<IProvidedCapability> providedCapabilities2,
			boolean ignoreVersionDifferences) {
		Set<IUPart> iu1 = new HashSet<IUPart>();
		Set<IUPart> iu2 = new HashSet<IUPart>();
		for (IProvidedCapability capability : providedCapabilities) {
			iu1.add(IUPart.createIUPart(capability, ignoreVersionDifferences, capability.getName().toLowerCase(), capability.getNamespace(), capability.getVersion().toString()));
		}
		for (IProvidedCapability capability : providedCapabilities2) {
			iu2.add(IUPart.createIUPart(capability, ignoreVersionDifferences, capability.getName().toLowerCase(), capability.getNamespace(), capability.getVersion().toString()));
		}
		return getComplement(iu1, iu2);
	}

	private static Collection<IUPart> getRequirementDifferences(
			Collection<IRequirement> requirements,
			Collection<IRequirement> requirements2,
			boolean ignoreVersionDifferences) {
		Set<IUPart> iu1 = new HashSet<IUPart>();
		Set<IUPart> iu2 = new HashSet<IUPart>();
		for (IRequirement requirement : requirements) {
			iu1.add(createIUPart(requirement, ignoreVersionDifferences));
		}
		for (IRequirement requirement : requirements2) {
			iu2.add(createIUPart(requirement, ignoreVersionDifferences));
		}
		return getComplement(iu1, iu2);
	}
	
	private static IUPart createIUPart(IRequirement requirement, boolean ignoreVersionDifferences ) {
		if ( requirement instanceof IRequiredCapability ) {
			IRequiredCapability requiredCapability = (IRequiredCapability) requirement;
			return IUPart.createIUPart(requirement, ignoreVersionDifferences, requiredCapability.getNamespace(), requiredCapability.getName().toLowerCase(), requiredCapability.getRange().toString());
		} else {
			return IUPart.createIUPart(requirement, false, requirement.getMatches().toString());
		}
	}

	private static Collection<IUPart> getPropertyDifferences(Map<String, String> properties,
			Map<String, String> properties2, boolean ignoreVersionDifferences) {
		Set<IUPart> iu1 = new HashSet<IUPart>();
		Set<IUPart> iu2 = new HashSet<IUPart>();
		for (Entry<String, String> entry : properties.entrySet()) {
			iu1.add(IUPart.createIUPart(entry, false, entry.getKey(), entry.getValue()));
		}
		for (Entry<String, String> entry : properties2.entrySet()) {
			iu2.add(IUPart.createIUPart(entry, false, entry.getKey(), entry.getValue()));
		}
		return getComplement(iu1, iu2);
	}

	public boolean hasDifferences() {
		return this.retativeComplementA.size() > 0 && this.retativeComplementB.size() > 0;
	}
}
