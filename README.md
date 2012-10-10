##NAME
p2diff -- Shows the differences between two p2 repositories

##BUILDING
<pre>
[$] cd org.eclipse.equinox.p2.example.p2diff.releng  
[$] mvn clean install
</pre>

   The artifacts will be built in
   org.eclipse.equinox.p2.example.p2diff.packaging/target/products/org.eclipse.equinox.p2.example.p2diff.product/

##USAGE
   p2diff [options] repository1 repository2

##DESCRIPTION
Loads two p2 repositories and pretty prints the differences.
The tool can be configured for high level differences, or it can show  more detailed information about how specific IUs have changed.

<pre>
The following options are available: 

    -h                   Displays this message  
    -onlylatest          Only operate on the latest version of each IU  
    -ignorecase          When comparing IUs, the IDs are compared in a case insenstive way  
    -mode=all            Compare all IUs regardless of their ID  
    -mode=ignoreVersions If an IU in each repository has the same ID, consider  
                         them equal, regardless of their version.  
    -mode=deep           If an IU in each repository has the same ID, compare  
                         the contents of the IU.  
    -query=all           Operate on ALL the IUs in a repository  
    -query=groups        Only operate on IUs that are marked as GROUPS  
    -query=categorized   Only operate on IUs that have been explicitly categorized  
    -category=Category  Used in conjunction with -query=categorized. This will  
                         only print IUs in a specific category 
</pre>

##EXAMPLE USAGE
 Print the differences between the Juno and Indigo releases, but only show the items 
 in the Programming Languages category.

<pre>
 ./p2diff -query=categorized -mode=ignoreVersions -category=Programming Languages -onlylatest http://download.eclipse.org/releases/juno http://download.eclipse.org/releases/indigo
</pre>

##LICENSE
    p2Diff is licensed under the EPL. Copyright EclipseSource 2012.
    Maintained by Ian Bull.