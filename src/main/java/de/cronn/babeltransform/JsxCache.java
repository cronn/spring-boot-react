package de.cronn.babeltransform;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import de.cronn.babeltransform.javascript.BabelTransformer;
import de.cronn.babeltransform.javascript.UglifyTransformer;

/**
 * Class for caching javascript assets in jsx transformed and minified form
 *
 * @author Hanno Fellmann, cronn GmbH
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class JsxCache
{
	@Autowired
	private BabelTransformer jsxTransformer;

	@Autowired
	private UglifyTransformer uglifier;
	
	private final ConcurrentHashMap<String, AssetEntry> jsCache = new ConcurrentHashMap<>();
	
	@Value("${babeltransform.diskCache:false}")
	private boolean diskCache;

	public static class AssetEntry
	{
		public final String content, etag;

		public final long lastModified;

		public AssetEntry(final String content, final long lastModified, final String etag)
		{
			super();
			this.content = content;
			this.lastModified = lastModified;

			this.etag = "\"" + etag + "\"";
		}
	}

	public AssetEntry get(final File file) throws IOException
	{
		final AssetEntry entry = jsCache.get(file.getPath());

		if (entry == null || (entry.lastModified < file.lastModified()))
		{
			return getNewEntry(file.getPath(), file);
		}
		else
		{
			return entry;
		}
	}

	private AssetEntry getNewEntry(final String fileName, final File file) throws IOException
	{
		String newContent = null;

		final String content = IOUtils.toString(new FileInputStream(file), Charset.defaultCharset());
		final String etag = UUID.nameUUIDFromBytes(content.getBytes()).toString();
		final String cachePrefix = "//" + etag + "\n";

		final File cacheFile = new File(file.getAbsolutePath() + ".cache");
		if(diskCache)
		{
			if(!file.getName().startsWith("test") && cacheFile.exists())
			{
				final String cacheContent = IOUtils.toString(new FileInputStream(cacheFile), "UTF-8");
				if(cacheContent.startsWith(cachePrefix))
				{
					newContent = cacheContent.substring(cachePrefix.length());
				}
			}
		}
		if(newContent == null)
		{
			newContent = jsxTransformer.transform(content);
			newContent = uglifier.transform(newContent);
			
			if(diskCache)
			{
				final FileOutputStream fos = new FileOutputStream(cacheFile);
				fos.write(cachePrefix.getBytes());
				fos.write(newContent.getBytes());
				fos.close();
			}
		}
		
		final AssetEntry entry = new AssetEntry(newContent, file.lastModified(), etag);
		jsCache.put(fileName, entry);
		return entry;
	}
}
