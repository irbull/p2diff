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

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

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


	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	public Object start(IApplicationContext context) throws Exception {
		String[] args = (String[]) context.getArguments().get(IApplicationContext.APPLICATION_ARGS);
		ArgumentProcessor processor = ArgumentProcessor.createArgumentProcessor(args);
		if  (processor == null) {
			printHelpMessage();
			return IApplication.EXIT_OK;
		}
		IProvisioningAgent agent = setupAgent(null);
		P2Diff diff = P2Diff.createP2Diff(agent, processor);
		Set<PrintStream> outs = new HashSet<PrintStream>();
		outs.add(System.out);
		for (String location : processor.getOutputFiles()) {
			File file = new File(location);
			if (!file.exists()) {
				file.createNewFile();
			}
			System.err.println("file=" +location);
			FileOutputStream outStream = new FileOutputStream(file);
			PrintStream printStream = new PrintStream(outStream);
			outs.add(printStream);
		}
		new P2DiffPrinter(diff, processor.isIgnoreCase(), processor.getMode()).printDiffs(outs);
		for (PrintStream stream : outs) {
			if (stream != System.out) {
				stream.close();
			}
		}
		return IApplication.EXIT_OK;
	}
	
	private void printHelpMessage() {
		System.out.println("NAME");
		System.out.println("   p2diff -- Shows the differences between two p2 repositories");
		System.out.println("");
		System.out.println("USAGE");
		System.out.println("   p2diff [options] repository1 repository2");
		System.out.println("");
		System.out.println("DESCRIPTION");
		System.out.println("   Loads two p2 repositories and pretty prints the differences.  The");
		System.out.println("   tool can be configured for high level differences, or it can show");
		System.out.println("   more detailed information about how specific IUs have changed.");
		System.out.println("");
		System.out.println("   The following options are available:");
		System.out.println("   -h                   Displays this message");
		System.out.println("   -onlylatest          Only operate on the latest version of each IU");
		System.out.println("   -ignorecase          When comparing IUs, the IDs are compared in a case insenstive way");
		System.out.println("   -mode=all            Compare all IUs regardless of their ID");
		System.out.println("   -mode=ignoreVersions If an IU in each repository has the same ID, consider\n" +
				           "                        them equal, regardless of their version.");
		System.out.println("   -mode=deep           If an IU in each repository has the same ID, compare\n" + 
				           "                        the contents of the IU.");
		System.out.println("   -query=all           Operate on ALL the IUs in a repository");
		System.out.println("   -query=groups        Only operate on IUs that are marked as GROUPS");
		System.out.println("   -query=categorized   Only operate on IUs that have been explicitly categorized");
		System.out.println("   -category=<Category> Used in conjunction with -query=categorized. This will\n" +
				           "                        only print IUs in a specific category");
		System.out.println("   -outputfile=<location>   File location where to write a copy of the report (multiple\n" +
				           "                            settings of this parameter will copy to multiple files).");
		System.out.println();
		System.out.println("EXAMPLE USAGE");
		System.out.println(" Print the differences between the Juno and Indigo releases, but only show the items \n" +
		                   " in the Programming Languages category.");           
		System.out.println("");
		System.out.println(" ./p2diff -query=categorized -mode=ignoreVersions -category=Programming Languages -onlylatest http://download.eclipse.org/releases/juno http://download.eclipse.org/releases/indigo");
		System.out.println("");
		System.out.println("LICENSE");
		System.out.println("    p2Diff is licensed under the EPL. Copyright EclipseSource 2012.");
		System.out.println("    Maintained by Ian Bull.");
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
