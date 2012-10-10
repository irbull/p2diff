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
import java.net.URISyntaxException;


public class ArgumentProcessor {
	public enum QueryType { ALL, GROUPS, CATEGORIZED };
	public enum Mode {ALL, IGNORE_VERSION, DEEP_COMPARE};
	
	private URI locationA;
	private URI locationB;
	private Mode mode = Mode.ALL;
	private QueryType queryMode = QueryType.ALL;
	private boolean ignoreCase = false;
	private boolean onlyLatest = false;
	
	public static ArgumentProcessor createArgumentProcessor(String[] args) throws URISyntaxException {
		if ( args.length < 2) {
			printHelpMessage();
			return null;
		}
		if ( args[args.length-1].startsWith("-") || args[args.length-2].startsWith("-")) {
			printHelpMessage();
			return null;
		}
		String locationA = args[args.length-2];
		String locationB = args[args.length-1];
		ArgumentProcessor argumentProcessor = new ArgumentProcessor(new URI(locationA), new URI(locationB));
		int optionLength = args.length-2;
		for ( int i = 0; i < optionLength; i++) {
			String arg = args[i].toLowerCase();
			if ( arg.equals("-h")) {
				printHelpMessage();
				return null;
			} else if (arg.equals("-query=all")) {
				argumentProcessor.queryMode = QueryType.ALL;
			} else if (arg.equals("-query=groups")) {
				argumentProcessor.queryMode = QueryType.GROUPS;
			} else if (arg.equals("-query=categorized")) {
				argumentProcessor.queryMode = QueryType.CATEGORIZED;
			} else if (arg.equals("-mode=all")) {
				argumentProcessor.mode = Mode.ALL;
			} else if (arg.equals("-mode=ignoreversions") ) {
				argumentProcessor.mode = Mode.IGNORE_VERSION;
			} else if (arg.equals("-mode=deep")) {
				argumentProcessor.mode = Mode.DEEP_COMPARE;
			} else if (arg.equals("-ignorecase")) {
				argumentProcessor.ignoreCase = true;
			} else if (arg.equals("-onlylatest")) {
				argumentProcessor.onlyLatest = true;
			}
		}
		return argumentProcessor;
	}
	
	private static void printHelpMessage() {
		
	}

	private ArgumentProcessor(URI locationA, URI locationB) {
		this.locationA = locationA;
		this.locationB = locationB;
	}
	
	public URI getLocationA() {
		return locationA;
	}

	public URI getLocationB() {
		return locationB;
	}

	public Mode getMode() {
		return mode;
	}

	public QueryType getQueryMode() {
		return queryMode;
	}

	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	public boolean isOnlyLatest() {
		return onlyLatest;
	}

	public String getCategoryName() {
		return null;
	}
}
