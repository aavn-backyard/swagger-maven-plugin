package com.github.kongchen.swagger.docgen.mavenplugin;

import org.apache.maven.plugins.annotations.Parameter;

public class AdditionalDependency {
	@Parameter
	private String additionalClassPath;
	
	@Parameter
	private IncludeJarsOnFolder includeJarsOnFolder;
	
	public String getAdditionalClassPath() {
		return additionalClassPath;
	}
	public void setAdditionalClassPath(String additionalClassPath) {
		this.additionalClassPath = additionalClassPath;
	}
	public IncludeJarsOnFolder getIncludeJarsOnFolder() {
		return includeJarsOnFolder;
	}
	public void setIncludeJarsOnFolder(IncludeJarsOnFolder includeJarsOnFolder) {
		this.includeJarsOnFolder = includeJarsOnFolder;
	}

	
}
