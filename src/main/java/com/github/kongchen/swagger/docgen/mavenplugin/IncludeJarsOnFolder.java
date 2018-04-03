package com.github.kongchen.swagger.docgen.mavenplugin;

import java.util.List;

import org.apache.maven.plugins.annotations.Parameter;

public class IncludeJarsOnFolder {
	@Parameter
	private List<String> folders;

	public List<String> getFolders() {
		return folders;
	}

	public void setFolders(List<String> folders) {
		this.folders = folders;
	}
	
}
