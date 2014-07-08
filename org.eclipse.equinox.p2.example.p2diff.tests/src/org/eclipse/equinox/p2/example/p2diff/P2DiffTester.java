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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Set;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.example.p2diff.ArgumentProcessor.Mode;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.VersionedId;
import org.junit.Test;
import org.osgi.framework.Bundle;


public class P2DiffTester extends IUTester{

//	private IProvisioningAgent setupAgent(final URI location) throws ProvisionException {
//		  IProvisioningAgent result = null;
//		    ServiceReference providerRef = Activator.getContext().getServiceReference(IProvisioningAgentProvider.SERVICE_NAME);
//		    if (providerRef == null) {
//		      throw new RuntimeException("No provisioning agent provider is available"); //$NON-NLS-1$
//		    }
//		    IProvisioningAgentProvider provider = (IProvisioningAgentProvider) Activator.getContext().getService(providerRef);
//		    if (provider == null) {
//		      throw new RuntimeException("No provisioning agent provider is available"); //$NON-NLS-1$
//		    }
//		    // obtain agent for currently running system
//		    result = provider.createAgent(location);
//		    // When you're done, make sure you 'unget' the service.
//		    // Activator.getContext().ungetService(providerRef);
//		    return result;
//		}
//	
//	public IMetadataRepository loadRepository(URI repositoryLocation) throws ProvisionException {
//		IProvisioningAgent agent = setupAgent(repositoryLocation);
//		  IMetadataRepositoryManager manager = (IMetadataRepositoryManager) agent.getService(IMetadataRepositoryManager.SERVICE_NAME);
//		  IMetadataRepository repository = manager.loadRepository(repositoryLocation, new NullProgressMonitor());
//		return repository;
//		}
	
	@Test
	public void testLunaRvsLunaPrevious() throws Exception {
		
		Application app = new Application();
		IApplicationContext context = new IApplicationContext() {

			@Override
			public void applicationRunning() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Map getArguments() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getBrandingApplication() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Bundle getBrandingBundle() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getBrandingDescription() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getBrandingId() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getBrandingName() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getBrandingProperty(String arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setResult(Object arg0, IApplication arg1) {
				// TODO Auto-generated method stub
				
			}};
		app.start(context);
		IProvisioningAgent agent = app.setupAgent(null);

		String repoA = "http://download.eclipse.org/releases/luna/201405230900";
		String repoB = "http://download.eclipse.org/releases/luna/201406250900"; 
		URI uriA = new URI (repoA);
		URI uriB = new URI (repoB); 

		ArgumentProcessor argumentProcessor = ArgumentProcessor.createArgumentProcessor((new String[] {"-mode=ignoreVersions", repoA, repoB}));
		assertTrue(argumentProcessor.getMode() == Mode.IGNORE_VERSION);
		
//		IMetadataRepository metadataA = loadRepository(uriA);
//		IMetadataRepository metadataB = loadRepository(uriB);
//		P2Diff p2Diff = new P2Diff(metadataA,null,metadataB,null,argumentProcessor);
		
		P2Diff p2Diff = P2Diff.createP2Diff(agent, argumentProcessor);
		Set<IInstallableUnit> relativeComplementA = p2Diff.getRelativeComplementA().toSet();
		assertEquals(0, relativeComplementA.size());
		
		app.stop();
	}
	
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
