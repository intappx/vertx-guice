import com.google.common.base.Preconditions;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

import javax.inject.Inject;

/**
 * Uncompiled version of the Verticle which uses constructor
 * injection to obtain reference to Vertx instance.
 */
public class UncompiledVerticleWithDependency extends AbstractVerticle {
    @Inject
    public UncompiledVerticleWithDependency(Vertx vertx)
    {
        Preconditions.checkNotNull(vertx);
    }
}
