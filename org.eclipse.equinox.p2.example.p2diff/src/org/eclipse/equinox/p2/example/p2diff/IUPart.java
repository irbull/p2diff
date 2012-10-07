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

public class IUPart {
	private String id;
	private Object iuPart;
	
	public static IUPart createIUPart(Object iuPart, boolean ignoreFinalArgument, String... args) {
		StringBuilder id = new StringBuilder();
		for (int i = 0; i < args.length; i++) {
			if ( i == args.length-1 && ignoreFinalArgument) {
				break;
			}
			id.append(args[i]);
		}
		return new IUPart(id.toString(), iuPart);
	}
	
	@Override
	public String toString() {
		return iuPart.toString();
	}
	
	private IUPart(String id, Object iuPart) {
		this.id = id;
		this.iuPart = iuPart;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IUPart other = (IUPart) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


}