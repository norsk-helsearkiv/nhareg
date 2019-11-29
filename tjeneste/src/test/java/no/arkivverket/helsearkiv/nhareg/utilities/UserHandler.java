package no.arkivverket.helsearkiv.nhareg.utilities;

import java.util.concurrent.Callable;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RunAs;
import javax.ejb.Stateless;

import no.arkivverket.helsearkiv.nhareg.auth.Roller;

@Stateless
@RunAs(Roller.ROLE_BRUKER)
@PermitAll
public class UserHandler {
    public <V> V call(Callable<V> callable) throws Exception {
        return callable.call();
    }
}