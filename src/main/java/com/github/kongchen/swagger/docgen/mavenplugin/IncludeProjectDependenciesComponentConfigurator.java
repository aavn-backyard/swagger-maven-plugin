package com.github.kongchen.swagger.docgen.mavenplugin;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.classworlds.ClassRealm;
import org.codehaus.plexus.component.configurator.AbstractComponentConfigurator;
import org.codehaus.plexus.component.configurator.ComponentConfigurationException;
import org.codehaus.plexus.component.configurator.ConfigurationListener;
import org.codehaus.plexus.component.configurator.converters.composite.ObjectWithFieldsConverter;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator;
import org.codehaus.plexus.configuration.PlexusConfiguration;

/**
 * A custom ComponentConfigurator which adds the project's runtime classpath elements
 * to the
 *
 * @author Brian Jackson
 * @plexus.component role="org.codehaus.plexus.component.configurator.ComponentConfigurator"
 * role-hint="include-project-dependencies"
 * @plexus.requirement role="org.codehaus.plexus.component.configurator.converters.lookup.ConverterLookup"
 * role-hint="default"
 * @since Aug 1, 2008 3:04:17 PM
 */
public class IncludeProjectDependenciesComponentConfigurator extends AbstractComponentConfigurator {
	
    @Override
    public void configureComponent(Object component, PlexusConfiguration configuration,
                                   ExpressionEvaluator expressionEvaluator, ClassRealm containerRealm,
                                   ConfigurationListener listener)
            throws ComponentConfigurationException {
        addProjectDependenciesToClassRealm(expressionEvaluator, containerRealm);
        ObjectWithFieldsConverter converter = new ObjectWithFieldsConverter();
        converter.processConfiguration(converterLookup, component, containerRealm.getClassLoader(), configuration,
                expressionEvaluator, listener);
        
        ApiDocumentMojo apiMojo = (ApiDocumentMojo) component;
        if(apiMojo.getAdditionalDependency() != null) {
        	updateProjectDependenciesToClassRealm(containerRealm, apiMojo.getAdditionalDependency());
        }
    }

    private void addProjectDependenciesToClassRealm(ExpressionEvaluator expressionEvaluator, ClassRealm containerRealm) throws ComponentConfigurationException {
        List<String> compileClasspathElements;
        try {
            compileClasspathElements = (List<String>) expressionEvaluator.evaluate("${project.compileClasspathElements}");
        } catch (ExpressionEvaluationException e) {
            throw new ComponentConfigurationException("There was a problem evaluating: ${project.compileClasspathElements}", e);
        }

        // Add the project dependencies to the ClassRealm
        final URL[] urls = buildURLs(compileClasspathElements);
        for (URL url : urls) {
            containerRealm.addConstituent(url);
        }
    }
    
    private void updateProjectDependenciesToClassRealm(ClassRealm containerRealm, AdditionalDependency additionalDependency) throws ComponentConfigurationException {
    	String additionalClassPath = additionalDependency.getAdditionalClassPath();
    	List<String> additionalJarsFolders = additionalDependency.getIncludeJarsOnFolder() != null ? additionalDependency.getIncludeJarsOnFolder().getFolders() : null;
    	List<String> compileClasspathElements = new ArrayList<String>();
    	if(StringUtils.isNotEmpty(additionalClassPath)) {
    		compileClasspathElements.addAll(Arrays.asList(StringUtils.split(additionalDependency.getAdditionalClassPath(), ",")));
    	}
    	
    	if(additionalJarsFolders != null && additionalJarsFolders.size() > 0) {
    		for ( int i = 0; i < additionalJarsFolders.size(); i++) {
    			if(StringUtils.isNotEmpty(additionalJarsFolders.get(i))) {
    				System.out.println("Include jar of:" + additionalJarsFolders.get(i));
		    		File folder = new File(additionalJarsFolders.get(i));
		    		if(folder.isDirectory()) {
		    			File[] listOfFiles = folder.listFiles();
		    			for (int j = 0; j < listOfFiles.length; j++) {
		    			    if (listOfFiles[j].isFile() && listOfFiles[j].getAbsolutePath().endsWith("jar")) {
		    			    	compileClasspathElements.add(listOfFiles[j].getAbsolutePath());
		    			     } 
		    			}
		    		} else {
		    			System.out.println("[WARN] Can not load additional jars because " + additionalJarsFolders.indexOf(i) + " is not a valid folder.");
		    		}
    			}
    		}
    	}

        // Add the project dependencies to the ClassRealm
    	if(compileClasspathElements != null) {
	        final URL[] urls = buildURLs(compileClasspathElements);
	        for (URL url : urls) {
	            containerRealm.addConstituent(url);
	        }
    	}
    }
    private URL[] buildURLs(List<String> runtimeClasspathElements) throws ComponentConfigurationException {
        // Add the projects classes and dependencies
        List<URL> urls = new ArrayList<URL>(runtimeClasspathElements.size());
        for (String element : runtimeClasspathElements) {
            try {
                final URL url = new File(element).toURI().toURL();
                urls.add(url);
            } catch (MalformedURLException e) {
                throw new ComponentConfigurationException("Unable to access project dependency: " + element, e);
            }
        }

        // Add the plugin's dependencies (so Trove stuff works if Trove isn't on
        return urls.toArray(new URL[urls.size()]);
    }

}
