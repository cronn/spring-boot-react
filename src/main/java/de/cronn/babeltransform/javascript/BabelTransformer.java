package de.cronn.babeltransform.javascript;
import java.io.IOException;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

/**
 * Babel.js transformer
 *
 * @author Hanno Fellmann, cronn GmbH
 */
@Service
public class BabelTransformer extends JsTransformBase
{
	@Autowired
	private ResourceLoader resourceLoader;
	
	private final String presets = "'es2015', 'react'";
	// ------------------------------ FIELDS ------------------------------

	@PostConstruct
	public void init() throws IOException
	{
		init("babel.js", Arrays.asList(resourceLoader.getResource("classpath:static/js/lib").getFile()));
	}
	
	@Override
	public synchronized String transformInContext(final String jsx, final Context ctx)
	{
		final Function transform = (Function) exports.get("transform", topLevelScope);
		
		ctx.evaluateString(
			topLevelScope, "var babelArgs = { retainLines: true, presets: [" + presets + "] };", "args", 1, null);
		final Object args = topLevelScope.get("babelArgs");
		
		final NativeObject obj = (NativeObject) transform.call(ctx, topLevelScope, exports, new Object[] { jsx, args });
		return (String) obj.get("code");
	}
}
