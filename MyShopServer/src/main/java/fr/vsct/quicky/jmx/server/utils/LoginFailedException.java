package fr.vsct.quicky.jmx.server.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Sylvain on 05/12/2014.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "login failed")
public class LoginFailedException extends RuntimeException {
}
