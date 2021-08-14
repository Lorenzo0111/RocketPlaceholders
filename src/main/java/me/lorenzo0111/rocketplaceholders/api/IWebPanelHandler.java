package me.lorenzo0111.rocketplaceholders.api;

import me.lorenzo0111.rocketplaceholders.exceptions.InvalidResponseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * Interface for the WebEditor handler
 */
public interface IWebPanelHandler {
    /**
     * @return JSON with internal placeholders
     */
    @NotNull String generate();

    /**
     * @return Server response
     * @throws IOException if the request causes an error
     */
    @Nullable String save() throws IOException;

    /**
     * @param code Editor save code
     * @return A {@link WebEdit} instance
     * @throws InvalidResponseException if the response is invalid
     */
    @NotNull WebEdit load(String code) throws InvalidResponseException;
}
