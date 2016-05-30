package de.cronn.demo.react.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import de.cronn.jsxtransformer.CachedJsxTransformer;
import de.cronn.jsxtransformer.CachedJsxTransformer.AssetEntry;

/**
 * Controller for providing compiled JSX files
 *
 * @author Hanno Fellmann, cronn GmbH
 */
@Controller
public class JsxController {
	@Autowired
	private ResourceLoader resourceLoader;

	private CachedJsxTransformer assetCache;

	@Value("${babeltransform.filePath}")
	private String jsxPath;

	@Value("${babeltransform.uglify:false}")
	private boolean uglify;

	@Value("${babeltransform.recompile:false}")
	private boolean recompile;

	@PostConstruct
	public void initAssetCache() throws IOException, URISyntaxException {
		assetCache = new CachedJsxTransformer(uglify, recompile);
	}

	@RequestMapping(value = "/jsx/{fileName:.+}")
	@ResponseBody
	public String getAsset(@PathVariable final String fileName, final WebRequest request) throws Exception {
		final AssetEntry assetEntry = assetCache.get(fileName, (key) -> IOUtils
				.toString(resourceLoader.getResource(jsxPath + key).getInputStream(), StandardCharsets.UTF_8));

		if (request.checkNotModified(assetEntry.etag)) {
			return null;
		}

		return assetEntry.content;
	}
}
