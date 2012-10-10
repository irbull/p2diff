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

import static org.junit.Assert.*;

import java.net.URI;

import org.eclipse.equinox.p2.example.p2diff.ArgumentProcessor;
import org.eclipse.equinox.p2.example.p2diff.ArgumentProcessor.Mode;
import org.eclipse.equinox.p2.example.p2diff.ArgumentProcessor.QueryType;
import org.junit.Test;

/**
 * Tests the argument processor
 * 
 * @author Ian Bull
 *
 */
public class ArgumentProcessorTest {

	private String[] createArgs(String args) {
		return args.split(" ");
	}

	@Test
	public void testArgumentProcessorNull() throws Exception {
		ArgumentProcessor argumentProcessor = ArgumentProcessor.createArgumentProcessor(createArgs(""));
		assertNull(argumentProcessor);
	}

	@Test
	public void testArgumentProcessorNull2() throws Exception {
		ArgumentProcessor argumentProcessor = ArgumentProcessor.createArgumentProcessor(createArgs("http://foo"));
		assertNull(argumentProcessor);
	}
	
	@Test
	public void testArgumentProcessorNull3() throws Exception {
		ArgumentProcessor argumentProcessor = ArgumentProcessor.createArgumentProcessor(createArgs("-foo -bar"));
		assertNull(argumentProcessor);
	}

	@Test
	public void testArgumentProcessor1() throws Exception {
		ArgumentProcessor argumentProcessor = ArgumentProcessor.createArgumentProcessor(createArgs("http://foo http://bar"));
		assertNotNull(argumentProcessor);
		assertEquals(new URI("http://foo"), argumentProcessor.getLocationA());
		assertEquals(new URI("http://bar"), argumentProcessor.getLocationB());
	}
	
	@Test
	public void testArgumentProcessor2() throws Exception {
		ArgumentProcessor argumentProcessor = ArgumentProcessor.createArgumentProcessor(createArgs("-ignoreCase http://foo http://bar"));
		assertNotNull(argumentProcessor);
		assertTrue(argumentProcessor.isIgnoreCase());
		assertEquals(new URI("http://foo"), argumentProcessor.getLocationA());
		assertEquals(new URI("http://bar"), argumentProcessor.getLocationB());
	}
	
	@Test
	public void testArgumentProcessor3() throws Exception {
		ArgumentProcessor argumentProcessor = ArgumentProcessor.createArgumentProcessor(createArgs("http://foo http://bar"));
		assertNotNull(argumentProcessor);
		assertFalse(argumentProcessor.isIgnoreCase());
		assertEquals(new URI("http://foo"), argumentProcessor.getLocationA());
		assertEquals(new URI("http://bar"), argumentProcessor.getLocationB());
	}
	
	@Test
	public void testArgumentProcessor4() throws Exception {
		ArgumentProcessor argumentProcessor = ArgumentProcessor.createArgumentProcessor(createArgs("-onlyLatest http://foo http://bar"));
		assertNotNull(argumentProcessor);
		assertTrue(argumentProcessor.isOnlyLatest());
		assertFalse(argumentProcessor.isIgnoreCase());
		assertEquals(new URI("http://foo"), argumentProcessor.getLocationA());
		assertEquals(new URI("http://bar"), argumentProcessor.getLocationB());
	}
	
	@Test
	public void testArgumentProcessorCategoryName1() throws Exception {
		ArgumentProcessor argumentProcessor = ArgumentProcessor.createArgumentProcessor(createArgs("-category=Programming Languages -onlyLatest http://foo http://bar"));
		assertNotNull(argumentProcessor);
		assertTrue(argumentProcessor.isOnlyLatest());
		assertFalse(argumentProcessor.isIgnoreCase());
		assertEquals("Programming Languages", argumentProcessor.getCategoryName());
		assertEquals(new URI("http://foo"), argumentProcessor.getLocationA());
		assertEquals(new URI("http://bar"), argumentProcessor.getLocationB());
	}
	
	@Test
	public void testArgumentProcessorCategoryName2() throws Exception {
		ArgumentProcessor argumentProcessor = ArgumentProcessor.createArgumentProcessor(createArgs("-category=Programming  -onlyLatest http://foo http://bar"));
		assertNotNull(argumentProcessor);
		assertTrue(argumentProcessor.isOnlyLatest());
		assertFalse(argumentProcessor.isIgnoreCase());
		assertEquals("Programming", argumentProcessor.getCategoryName());
		assertEquals(new URI("http://foo"), argumentProcessor.getLocationA());
		assertEquals(new URI("http://bar"), argumentProcessor.getLocationB());
	}
	
	@Test
	public void testArgumentProcessorCategoryName3() throws Exception {
		ArgumentProcessor argumentProcessor = ArgumentProcessor.createArgumentProcessor(createArgs("-category=Programming bar foo bar -onlyLatest http://foo http://bar"));
		assertNotNull(argumentProcessor);
		assertTrue(argumentProcessor.isOnlyLatest());
		assertFalse(argumentProcessor.isIgnoreCase());
		assertEquals("Programming bar foo bar", argumentProcessor.getCategoryName());
		assertEquals(new URI("http://foo"), argumentProcessor.getLocationA());
		assertEquals(new URI("http://bar"), argumentProcessor.getLocationB());
	}
	
	@Test
	public void testArgumentProcessorMode1() throws Exception {
		ArgumentProcessor argumentProcessor = ArgumentProcessor.createArgumentProcessor(createArgs("http://foo http://bar"));
		assertTrue(argumentProcessor.getMode() == Mode.ALL);
		assertEquals(new URI("http://foo"), argumentProcessor.getLocationA());
		assertEquals(new URI("http://bar"), argumentProcessor.getLocationB());
	}
	
	@Test
	public void testArgumentProcessorMode2() throws Exception {
		ArgumentProcessor argumentProcessor = ArgumentProcessor.createArgumentProcessor(createArgs("-mOdE=AlL http://foo http://bar"));
		assertTrue(argumentProcessor.getMode() == Mode.ALL);
		assertEquals(new URI("http://foo"), argumentProcessor.getLocationA());
		assertEquals(new URI("http://bar"), argumentProcessor.getLocationB());
	}
	
	@Test
	public void testArgumentProcessorMode3() throws Exception {
		ArgumentProcessor argumentProcessor = ArgumentProcessor.createArgumentProcessor(createArgs("-mOdE=ignoreVersions http://foo http://bar"));
		assertTrue(argumentProcessor.getMode() == Mode.IGNORE_VERSION);
		assertEquals(new URI("http://foo"), argumentProcessor.getLocationA());
		assertEquals(new URI("http://bar"), argumentProcessor.getLocationB());
	}
	
	@Test
	public void testArgumentProcessorMode4() throws Exception {
		ArgumentProcessor argumentProcessor = ArgumentProcessor.createArgumentProcessor(createArgs("-mOdE=deep http://foo http://bar"));
		assertTrue(argumentProcessor.getMode() == Mode.DEEP_COMPARE);
		assertEquals(new URI("http://foo"), argumentProcessor.getLocationA());
		assertEquals(new URI("http://bar"), argumentProcessor.getLocationB());
	}
	
	@Test
	public void testArgumentProcessorQuery1() throws Exception {
		ArgumentProcessor argumentProcessor = ArgumentProcessor.createArgumentProcessor(createArgs("-mOdE=deep http://foo http://bar"));
		assertTrue(argumentProcessor.getQueryMode() == QueryType.ALL);
		assertEquals(new URI("http://foo"), argumentProcessor.getLocationA());
		assertEquals(new URI("http://bar"), argumentProcessor.getLocationB());
	}
	
	@Test
	public void testArgumentProcessorQuery2() throws Exception {
		ArgumentProcessor argumentProcessor = ArgumentProcessor.createArgumentProcessor(createArgs("-query=all -mOdE=deep http://foo http://bar"));
		assertTrue(argumentProcessor.getQueryMode() == QueryType.ALL);
		assertEquals(new URI("http://foo"), argumentProcessor.getLocationA());
		assertEquals(new URI("http://bar"), argumentProcessor.getLocationB());
	}
	
	@Test
	public void testArgumentProcessorQuery3() throws Exception {
		ArgumentProcessor argumentProcessor = ArgumentProcessor.createArgumentProcessor(createArgs("-query=groups -mOdE=deep http://foo http://bar"));
		assertTrue(argumentProcessor.getQueryMode() == QueryType.GROUPS);
		assertEquals(new URI("http://foo"), argumentProcessor.getLocationA());
		assertEquals(new URI("http://bar"), argumentProcessor.getLocationB());
	}
	
	@Test
	public void testArgumentProcessorQuery4() throws Exception {
		ArgumentProcessor argumentProcessor = ArgumentProcessor.createArgumentProcessor(createArgs("-query=categorized -mOdE=deep http://foo http://bar"));
		assertTrue(argumentProcessor.getQueryMode() == QueryType.CATEGORIZED);
		assertEquals(new URI("http://foo"), argumentProcessor.getLocationA());
		assertEquals(new URI("http://bar"), argumentProcessor.getLocationB());
	}
}
