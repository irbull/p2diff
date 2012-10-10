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

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.example.p2diff.ArgumentProcessor.QueryType;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.query.CollectionResult;
import org.eclipse.equinox.p2.query.IQuery;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.IQueryable;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;

/**
 * A two way diff tool for p2 metadata repositories.
 * 
 * @author Ian Bull
 *
 */
public class P2Diff {

	private Set<IInstallableUnit> repositoryAContents;
	private Set<IInstallableUnit> repositoryBContents;
	private final IMetadataRepository repositoryA;
	private final IMetadataRepository repositoryB;
	private ArgumentProcessor processor;

	/**
	 * Factory method to create a P2Diff tool
	 * @param agent The provisioning agent to use to load the repositories
	 * @param repositoryALocation The location of the first repository
	 * @param repositoryBLocation The location of the second repository
	 * @return A P2Diff tool that can be used to compare the two repositories
	 * @throws ProvisionException Thrown if a repository cannot be read
	 * @throws OperationCanceledException
	 */
	public static P2Diff createP2Diff(IProvisioningAgent agent, ArgumentProcessor processor) throws ProvisionException, OperationCanceledException {
		IMetadataRepositoryManager manager = (IMetadataRepositoryManager) agent.getService(IMetadataRepositoryManager.SERVICE_NAME);
		IMetadataRepository repositoryA = manager.loadRepository(processor.getLocationA(), new NullProgressMonitor());
		IMetadataRepository repositoryB = manager.loadRepository(processor.getLocationB(), new NullProgressMonitor());
		return new P2Diff(repositoryA, createAndRunQuery(processor, repositoryA).toUnmodifiableSet(), repositoryB, createAndRunQuery(processor, repositoryB).toUnmodifiableSet(), processor);
	}
	
	private static IQueryResult<IInstallableUnit> createAndRunQuery(ArgumentProcessor processor, IQueryable<IInstallableUnit> queryable) {
		IQuery<IInstallableUnit> result = null;
		if (processor.getQueryMode() == QueryType.CATEGORIZED ) {
			result = createCompoundCategoryMemberQuery(queryable.query(getCategoryQuery(processor.getCategoryName()), null));
		} else if (processor.getQueryMode() == QueryType.GROUPS ) {
			result = QueryUtil.createIUGroupQuery();
		} else {
			result = QueryUtil.createIUAnyQuery();
		}
		if ( processor.isOnlyLatest() ) {
			result = QueryUtil.createLatestQuery(result);
		}
		return queryable.query(result, null);
	}
	
	private static IQuery<IInstallableUnit> createCompoundCategoryMemberQuery(IQueryResult<IInstallableUnit> categories) {
		Collection<IQuery<? extends IInstallableUnit>> queries = new ArrayList<IQuery<? extends IInstallableUnit>>();
		for (IInstallableUnit category : categories.toUnmodifiableSet()) {
			queries.add(QueryUtil.createIUCategoryMemberQuery(category));
		}
		return QueryUtil.createCompoundQuery(queries, false);
	}
	
	private static IQuery<IInstallableUnit> getCategoryQuery(String name) {
		if ( name == null ) {
			return QueryUtil.createIUCategoryQuery();
		}
		return QueryUtil.createPipeQuery(QueryUtil.createIUCategoryQuery(), QueryUtil.createMatchQuery("this.translatedProperties[$0] == $1", IInstallableUnit.PROP_NAME, name));
	}

	
	P2Diff(IMetadataRepository repositoryA, Set<IInstallableUnit> repositoryAContents, IMetadataRepository repositoryB, Set<IInstallableUnit> repositoryBContents, ArgumentProcessor processor) {
		this.repositoryA = repositoryA;
		this.repositoryAContents = repositoryAContents;
		this.repositoryB = repositoryB;
		this.repositoryBContents = repositoryBContents;
		this.processor = processor;
	}
	
	/**
	 * Returns the intersection of A and B
	 * @return
	 */
	public Collection<IInstallableUnit> getInersection() {
		HashSet<IInstallableUnit> result = new HashSet<IInstallableUnit>(repositoryAContents);
		result.addAll(repositoryBContents);
		return result;
	}
	
	/**
	 * Returns the elements in B that are not in A
	 * @return
	 */
	public IQueryResult<IInstallableUnit> getRelativeComplementA() {
		HashSet<IInstallableUnit> result = new HashSet<IInstallableUnit>(repositoryBContents);
		result.removeAll(repositoryAContents);
		return new CollectionResult<IInstallableUnit>(result);
	}
	
	/**
	 * Returns the elements in A that are not in B
	 * @return
	 */
	public IQueryResult<IInstallableUnit> getRelativeComplementB() {
		HashSet<IInstallableUnit> result = new HashSet<IInstallableUnit>(repositoryAContents);
		result.removeAll(repositoryBContents);
		return new CollectionResult<IInstallableUnit>(result);
	}

	/**
	 * Returns the IUs that we want to consider from repository A
	 */
	public IQueryResult<IInstallableUnit> getRepositoryA() {
		return new CollectionResult<IInstallableUnit>(repositoryAContents);
	}
	
	/**
	 * Returns the IUs that we want to consider from repository B
	 */
	public IQueryResult<IInstallableUnit> getRepositoryB() {
		return new CollectionResult<IInstallableUnit>(repositoryBContents);
	}

	/**
	 * Returns the location of repository A
	 */
	public String getRepositoryALocation() {
		return this.repositoryA.getLocation().toString();
	}

	/**
	 * Returns the location of repository B
	 */
	public String getRepositoryBLocation() {
		return this.repositoryB.getLocation().toString();
	}


}
