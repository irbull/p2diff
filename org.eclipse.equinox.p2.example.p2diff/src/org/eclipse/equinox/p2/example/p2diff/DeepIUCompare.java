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

/**
 * Does a deep compare on two IUs.  A deep compare will return differences inside
 * two IUs (with the same ID), where the differences are more than just 'version' differences. 
 *
 * For example, assume we have two IUs A and B - Where A and B have the same ID but different versions
 * The DeepIUCompare will find the parts of these IUs that differ. However, if a part only differs by a version number,
 * that is, for example, if all the requirements are the same but the versions are different, this will not be reported.
 * 
 * This tool allows you to find elements that have been added or removed from an IU.  
 * 
 * @author Ian Bull
 *
 */
public class DeepIUCompare {

	private Collection<IUPart> retativeComplementA = null;
	private Collection<IUPart> retativeComplementB = null;

	/**
	 * Creates a new DeepIUCompare object. iuA and iuB must have the same ID.
	 */
	public DeepIUCompare(IInstallableUnit iuA, IInstallableUnit iuB) {
		if ( !iuA.getId().equals(iuB.getId()) ) {
			throw new IllegalArgumentException(iuA.getId() + " must equal " + iuB.getId());
		}
		retativeComplementA = computeRelativeComplement(iuA, iuB);
		retativeComplementB = computeRelativeComplement(iuB, iuA);
	}
	
	/**
	 * Returns the parts of iuB that are not in iuA.
	 */
	public Collection<IUPart> getRetativeComplementA() {
		return this.retativeComplementA;
	}
	
	/**
	 * Returns true if iuA and iuB has any differences. Returns false otherwise.
	 */
	public boolean hasDifferences() {
		return this.retativeComplementA.size() > 0 && this.retativeComplementB.size() > 0;
	}

	/**
	 * Returns the parts of iuA that are not in iuB.
	 */
	public Collection<IUPart> getRetativeComplementB() {
		return this.retativeComplementB;
	}
	
	private static Collection<IUPart> computeRelativeComplement(IInstallableUnit iuA, IInstallableUnit iuB) {
		Collection<IUPart> result = new ArrayList<IUPart>();
		result.addAll(getPropertyDifferences(iuA.getProperties(), iuB.getProperties()));
		result.addAll(getRequirementDifferences(iuA.getRequirements(), iuB.getRequirements()));
		result.addAll(getProvidedCapabilityDifferences(iuA.getProvidedCapabilities(), iuB.getProvidedCapabilities()));
		result.addAll(getTouchpointDataDifferences(iuA.getTouchpointData(), iuB.getTouchpointData()));
		result.addAll(getArtifactDifferences(iuA.getArtifacts(), iuB.getArtifacts()));
		return result;
	}

	private static Collection<IUPart> getComplement(Set<IUPart> iu1, Set<IUPart> iu2) {
		Set<IUPart> s = new HashSet<IUPart>(iu1);
		s.removeAll(iu2);
		return s;
	}
	
	private static Collection<IUPart> getArtifactDifferences(
			Collection<IArtifactKey> artifacts,
			Collection<IArtifactKey> artifacts2) {
		Set<IUPart> iu1 = new HashSet<IUPart>();
		Set<IUPart> iu2 = new HashSet<IUPart>();
		for (IArtifactKey iArtifactKey : artifacts) {
			iu1.add(IUPart.createIUPart(iArtifactKey, true, iArtifactKey.getClassifier(), iArtifactKey.getId(), iArtifactKey.getVersion().toString()));
		}
		for (IArtifactKey iArtifactKey : artifacts2) {
			iu2.add(IUPart.createIUPart(iArtifactKey, true, iArtifactKey.getClassifier(), iArtifactKey.getId(), iArtifactKey.getVersion().toString()));
		}
		return getComplement(iu1, iu2);
	}

	private static Collection<IUPart> getTouchpointDataDifferences(
			Collection<ITouchpointData> touchpointData,
			Collection<ITouchpointData> touchpointData2 ) {
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
			Collection<IProvidedCapability> providedCapabilities2) {
		Set<IUPart> iu1 = new HashSet<IUPart>();
		Set<IUPart> iu2 = new HashSet<IUPart>();
		for (IProvidedCapability capability : providedCapabilities) {
			iu1.add(IUPart.createIUPart(capability, true, capability.getName().toLowerCase(), capability.getNamespace(), capability.getVersion().toString()));
		}
		for (IProvidedCapability capability : providedCapabilities2) {
			iu2.add(IUPart.createIUPart(capability, true, capability.getName().toLowerCase(), capability.getNamespace(), capability.getVersion().toString()));
		}
		return getComplement(iu1, iu2);
	}

	private static Collection<IUPart> getRequirementDifferences(
			Collection<IRequirement> requirements,
			Collection<IRequirement> requirements2) {
		Set<IUPart> iu1 = new HashSet<IUPart>();
		Set<IUPart> iu2 = new HashSet<IUPart>();
		for (IRequirement requirement : requirements) {
			iu1.add(createIUPart(requirement, true));
		}
		for (IRequirement requirement : requirements2) {
			iu2.add(createIUPart(requirement, true));
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
			Map<String, String> properties2) {
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
}
