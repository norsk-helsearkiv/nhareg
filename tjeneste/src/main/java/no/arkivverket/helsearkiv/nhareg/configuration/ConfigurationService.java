package no.arkivverket.helsearkiv.nhareg.configuration;

import javax.inject.Inject;

import static no.arkivverket.helsearkiv.nhareg.configuration.ConfigurationDAO.*;

public class ConfigurationService implements ConfigurationServiceInterface {

    @Inject
    private ConfigurationDAO configDAO;

    @Override
    public boolean getLmrConfigured() {
        final String lmr = configDAO.getValue(CONFIG_LMR);

        return "True".equals(lmr);
    }

    @Override
    public int getFanearkidLength() {
        return configDAO.getInt(CONFIG_FANEARKID);
    }

    @Override
    public String getCentury() {
        return configDAO.getValue(CONFIG_CENTURY);
    }

}