package no.arkivverket.helsearkiv.nhareg.utilities;

import no.arkivverket.helsearkiv.nhareg.common.Roller;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RunAs;
import javax.ejb.Stateless;
import java.util.concurrent.Callable;

@Stateless
@RunAs(Roller.ROLE_ADMIN)
@PermitAll
public class AdminHandler {
    public <V> V call(Callable<V> callable) throws Exception {
        return callable.call();
    }
}
