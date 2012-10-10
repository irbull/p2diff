package org.eclipse.equinox.p2.example.p2diff.tests;

import org.eclipse.equinox.p2.example.p2diff.ArgumentProcessorTest;
import org.eclipse.equinox.p2.example.p2diff.DeepIUCompareTester;
import org.eclipse.equinox.p2.example.p2diff.IUPartTester;
import org.eclipse.equinox.p2.example.p2diff.P2DiffTester;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({ P2DiffTester.class, DeepIUCompareTester.class, IUPartTester.class, ArgumentProcessorTest.class })
public class AllTests {

}
