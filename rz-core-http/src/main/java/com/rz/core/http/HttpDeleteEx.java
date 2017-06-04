package com.rz.core.http;

import java.net.URI;

//import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

//@NotThreadSafe
public class HttpDeleteEx extends HttpEntityEnclosingRequestBase {

    public final static String METHOD_NAME = "DELETE";

    public HttpDeleteEx() {
        super();
    }

    public HttpDeleteEx(final URI uri) {
        super();
        setURI(uri);
    }

    public HttpDeleteEx(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }
}