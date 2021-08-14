package me.lorenzo0111.rocketplaceholders.api;

import me.lorenzo0111.rocketplaceholders.creator.Placeholder;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * A web edit created from the server
 */
public class WebEdit implements Serializable {
    private List<String> remove;
    private Map<String,String> rename;
    private List<Placeholder> edited;

    /**
     * @return All placeholders identifier to remove
     */
    public List<String> getRemove() {
        return remove;
    }

    /**
     * @return A map with old and new identifiers
     */
    public Map<String, String> getRename() {
        return rename;
    }

    /**
     * @return All new placeholders to load
     */
    public List<Placeholder> getEdited() {
        return edited;
    }
}
