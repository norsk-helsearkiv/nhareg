package no.arkivverket.helsearkiv.nhareg.util;

import no.arkivverket.helsearkiv.nhareg.auth.AuthSecurityInterceptor;
import no.arkivverket.helsearkiv.nhareg.tjeneste.DiagnosekodeTjeneste;
import no.arkivverket.helsearkiv.nhareg.tjeneste.PasientjournalTjeneste;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * A class extending {@link Application} and annotated with @ApplicationPath is the Java EE 6
 * "no XML" approach to activating JAX-RS.
 * 
 * <p>
 * Resources are served relative to the servlet path specified in the {@link ApplicationPath}
 * annotation.
 * </p>
 */
@ApplicationPath("/")
public class JaxRsActivator extends Application {
   /* class body intentionally left blank */
   /*@Override
   public Set<Class<?>> getClasses() {
       Set<Class<?>> c = new HashSet<Class<?>>();
       c.add(AuthSecurityInterceptor.class);
       c.add(PasientjournalTjeneste.class);
       c.add(DiagnosekodeTjeneste.class);
       return c;
   }*/
}
