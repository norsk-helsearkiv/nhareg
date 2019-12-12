package no.arkivverket.helsearkiv.nhareg.auth;

import org.jboss.security.CacheableManager;

import javax.annotation.Resource;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.security.Principal;

/**
 * Created by haraldk on 24.04.15.
 * WFLY-3221
 */
@WebListener
public class FlushCredentialsListener implements HttpSessionListener {

    @Resource(name = "java:jboss/jaas/secureDomain/authenticationMgr")
    private CacheableManager<?, Principal> authenticationManager;

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        Principal principal = (Principal) httpSessionEvent.getSession().getAttribute("principal");
        authenticationManager.flushCache();

        if (principal != null)
            authenticationManager.flushCache(principal);
    }
}