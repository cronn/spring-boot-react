package de.cronn.babeltransform;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import de.cronn.babeltransform.JsxCache.AssetEntry;

/**
 * Controller for providing compiled JSX files
 *
 * @author Hanno Fellmann, cronn GmbH
 */
@Controller
public class JsxController {
	@Autowired
	private ResourceLoader resourceLoader;
	
	@Autowired
	private JsxCache assetCache;
	
	@Value("${babeltransform.filePath}")
	private String jsxPath;
	
	@RequestMapping(value="/jsx/{fileName:.+}")
	@ResponseBody
	public String getAsset(@PathVariable final String fileName, final WebRequest request) throws IOException
	{
		final AssetEntry assetEntry = assetCache.get(resourceLoader.getResource(jsxPath + fileName).getFile());
		
		if(request.checkNotModified(assetEntry.etag))
		{
			return null;
		}
		
		return assetEntry.content;
	}
}
