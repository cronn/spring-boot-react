package de.cronn.babeltransform.javascript;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.commonjs.module.Require;
import org.mozilla.javascript.commonjs.module.RequireBuilder;
import org.mozilla.javascript.commonjs.module.provider.SoftCachingModuleScriptProvider;
import org.mozilla.javascript.commonjs.module.provider.UrlModuleSourceProvider;

/**
 * Helper class to execute JavaScript tasks in a defined JavaScript thread context
 *
 * @author Hanno Fellmann, cronn GmbH
 */
public abstract class JsTransformBase
{
	protected Scriptable exports;

	protected ScriptableObject topLevelScope;

	private List<File> modulePaths;

	private String transformJs;

	protected void init(final String transformJs, final List<File> modulePaths)
	{
		this.transformJs = Objects.requireNonNull(transformJs);
		this.modulePaths = Objects.requireNonNull(modulePaths);
	}

	/**
	 * Helper function to create module path for require
	 * 
	 * @param modulePaths requested module paths
	 * @return list of URIs to module paths
	 */
	private SoftCachingModuleScriptProvider buildModulePaths(final List<File> modulePaths)
	{
		final List<URI> uris = new ArrayList<URI>(modulePaths.size());
		for (final File path : modulePaths)
		{
			final URI uri = path.toURI();
			uris.add(uri);
		}
		return new SoftCachingModuleScriptProvider(new UrlModuleSourceProvider(uris, null));
	}

	private void init(final Context ctx)
	{
		final RequireBuilder builder = new RequireBuilder();
		builder.setModuleScriptProvider(buildModulePaths(modulePaths));

		topLevelScope = ctx.initStandardObjects();
		final Require require = builder.createRequire(ctx, topLevelScope);

		exports = require.requireMain(ctx, transformJs);
	}

	public String transform(final String input)
	{
		final Context ctx = Context.enter();
		// Will otherwise crash on huge libraries like babel.js
		ctx.setOptimizationLevel(-1);
		try
		{
			init(ctx);
			return transformInContext(input, ctx);
		}
		finally
		{
			Context.exit();
		}
	}

	protected abstract String transformInContext(String input, Context ctx);
}
