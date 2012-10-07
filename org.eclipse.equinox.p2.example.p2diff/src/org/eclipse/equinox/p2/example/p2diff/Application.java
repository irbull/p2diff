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

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.IProvisioningAgentProvider;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.osgi.framework.ServiceReference;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {
	
	public static final boolean IGNORE_VERSIONS = true; // ignores differences when only the version has changed
	public static final boolean IGNORE_CASE = true; // attempts to find IUs that have the same ID regardless of case
	public String locationA = "http://download.eclipse.org/technology/epp/packages/juno/SR1/";
	public String locationB = "http://download.eclipse.org/technology/epp/packages/juno/SR1a.315/";

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	public Object start(IApplicationContext context) throws Exception {
		//URI uriA = new URI("http://download.eclipse.org/technology/epp/packages/juno/SR1a.313/");
		URI uriA = new URI(locationA);
		URI uriB = new URI(locationB);
		IProvisioningAgent agent = setupAgent(null);
		P2Diff diff = P2Diff.createP2Diff(agent, uriA, uriB);
		new P2DiffPrinter(diff).printDiffs(System.out);
		return IApplication.EXIT_OK;
	}
	
	  private IProvisioningAgent setupAgent(final URI location) throws ProvisionException {
		    IProvisioningAgent result = null;
		    ServiceReference providerRef =  Activator.getContext().getServiceReference(IProvisioningAgentProvider.SERVICE_NAME);
		    if (providerRef == null) {
		      throw new RuntimeException("No provisioning agent provider is available"); //$NON-NLS-1$
		    }
		    IProvisioningAgentProvider provider = (IProvisioningAgentProvider) Activator.getContext().getService(providerRef);
		    if (provider == null) {
		      throw new RuntimeException("No provisioning agent provider is available"); //$NON-NLS-1$
		    }
		    // obtain agent for currently running system
		    result = provider.createAgent(location);
		    Activator.getContext().ungetService(providerRef);
		    return result;
		  }

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	public void stop() {
		// nothing to do
	}
}
