package de.neuwirthinformatik.Alexander.TU.Render;

import java.nio.file.Path;

import lombok.Value;

@Value
public class DownloadedContent {
	Path path;
		String uri;
}
