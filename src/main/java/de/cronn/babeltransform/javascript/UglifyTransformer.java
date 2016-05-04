package de.cronn.babeltransform.javascript;

import java.io.IOException;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

/**
 * Uglify.js transformer
 *
 * @author Hanno Fellmann, cronn GmbH
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UglifyTransformer extends JsTransformBase
{
	@Autowired
	private ResourceLoader resourceLoader;
	
	@Value("${babeltransform.uglify:false}")
	boolean uglify;
	
	// ------------------------------ FIELDS ------------------------------
	@PostConstruct
	public void init() throws IOException
	{
		init("uglify-js.js", Arrays.asList(resourceLoader.getResource("classpath:static/js/lib/uglify").getFile()));
	}

	@Override
	public synchronized String transformInContext(final String jsx, Context ctx)
	{
		if(uglify)
		{
			Function transform = (Function) exports.get("convenience", topLevelScope);
			return (String) transform.call(ctx, topLevelScope, exports, new String[] { jsx });
		}
		return jsx;
	}
}
