package chat.hola.com.app.dagger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by ankit on 20/2/18.
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityScoped {
}
