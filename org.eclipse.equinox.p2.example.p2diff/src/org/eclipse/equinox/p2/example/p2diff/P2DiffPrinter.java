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

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.equinox.p2.example.p2diff.ArgumentProcessor.DifferenceType;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.query.CollectionResult;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.QueryUtil;

/**
 * Pretty prints the differences between two repositories
 * 
 * @author Ian Bull
 *
 */
public class P2DiffPrinter {
	
	private P2Diff diff;
	private boolean ignoreCase;
	private DifferenceType differenceType;

	public P2DiffPrinter(P2Diff diff, boolean ignoreCase, DifferenceType differenceType) {
		this.diff = diff;
		this.ignoreCase = ignoreCase;
		this.differenceType = differenceType;
	}
	
	/**
	 * Writes the differences to the output stream provided.
	 * 
	 * @param out The output stream to write the diff results too
	 */
	public void printDiffs(OutputStream out) {
		IQueryResult<IInstallableUnit> relativeComplementB = diff.getRelativeComplementB();
		IQueryResult<IInstallableUnit> relativeComplementA = diff.getRelativeComplementA();
		IQueryResult<IInstallableUnit> repositoryA = diff.getRepositoryA();
		IQueryResult<IInstallableUnit> repositoryB = diff.getRepositoryB();
		
		int aCounter = 0;
		int bCounter = 0;
		for (IInstallableUnit iu : relativeComplementB.toUnmodifiableSet()) {
			IQueryResult<IInstallableUnit> query = getID(repositoryB, iu.getId(), this.ignoreCase);
			if ( query.isEmpty() || this.differenceType == DifferenceType.ALL ) {
				System.out.println("> " + iu.getId() + " [" + iu.getVersion() +"] ");
				aCounter++;
			} else {
				Set<IInstallableUnit> set = query.toSet();
				if (this.differenceType == DifferenceType.DEEP_COMPARE) {
					for (IInstallableUnit iu2 : set) {
						DeepIUCompare iuDiffer = new DeepIUCompare(iu, iu2);
						if (iuDiffer.hasDifferences()) {
							System.out.println("> " + iu.getId() + " [" + iu.getVersion() + "] ");
							aCounter++;
						}
						for (IUPart iuPart : iuDiffer.getRetativeComplementA()) {
							System.out.println("  > " + iuPart.toString());
						}
						for (IUPart iuPart : iuDiffer.getRetativeComplementB()) {
							System.out.println("  < " + iuPart.toString());
						}
					}
				}
			}
		}
		for (IInstallableUnit iu : relativeComplementA.toUnmodifiableSet()) {
			IQueryResult<IInstallableUnit> query = getID(repositoryA, iu.getId(), this.ignoreCase);
			if ( query.isEmpty() || this.differenceType == DifferenceType.ALL  ) {
				System.out.println("< " + iu.getId() + " [" + iu.getVersion() +"] ");
				bCounter++;
			} else {
				Set<IInstallableUnit> set = query.toSet();
				if (this.differenceType == DifferenceType.DEEP_COMPARE) {
					for (IInstallableUnit iu2 : set) {
						DeepIUCompare iuDiffer = new DeepIUCompare(iu, iu2);
						if (iuDiffer.hasDifferences()) {
							System.out.println("< " + iu.getId() + " [" + iu.getVersion() + "] ");
							bCounter++;
						}
						for (IUPart iuPart : iuDiffer.getRetativeComplementA()) {
							System.out.println("  < " + iuPart.toString());
						}
						for (IUPart iuPart : iuDiffer.getRetativeComplementB()) {
							System.out.println("  > " + iuPart.toString());
						}
					}
				}
			}
		}
		System.out.println("=== Summary ===");
		if ( !relativeComplementB.isEmpty() ) {
			System.out.println(diff.getRepositoryALocation() + " contains " + aCounter + " unique IUs");
		}
		if ( !relativeComplementA.isEmpty() ) {
			System.out.println(diff.getRepositoryBLocation() + " contains " + bCounter + " unique IUs");
		}
	}
	
	/**
	 * Returns all the IUs in the collections that match a particular ID
	 * @param ius The IUs to query
	 * @param id The ID to match
	 * @param ignoreCase A flag indicating whether case should be ignored
	 * @return
	 */
	private IQueryResult<IInstallableUnit> getID(IQueryResult<IInstallableUnit> ius, String id, boolean ignoreCase) {
		Collection<IInstallableUnit> result = new ArrayList<IInstallableUnit>();
		IQueryResult<IInstallableUnit> queryResult = ius.query(QueryUtil.createIUAnyQuery(), new NullProgressMonitor());
		Iterator<IInstallableUnit> iterator = queryResult.iterator();
		while(iterator.hasNext()) {
			IInstallableUnit next = iterator.next();
			if ( ignoreCase && next.getId().toLowerCase().equals(id.toLowerCase())) {
				result.add(next);
			} else if ( next.getId().equals(id)) {
				result.add(next);
			}
		}
		return new CollectionResult<IInstallableUnit>(result);
	}
}
