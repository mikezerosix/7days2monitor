package org.mikezerosix.handlers;

import java.util.Map;
import java.util.Set;

/** When handler is added to chain all keys are added to settings from getSettingsKeys()
 *  When settings re saved each handler's  in the chain  updateSettings( ) is called with all the settings
 *
 */
public interface ConfigurableHandler {
    public Set<String> getSettingsKeys();
    public void updateSettings(Map<String, String> settings) ;
}
