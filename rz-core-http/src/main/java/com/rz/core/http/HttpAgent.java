package com.rz.core.http;

import java.io.IOException;
import java.util.Map;

public interface HttpAgent {
    String get(String uri) throws IOException;

    String get(String uri, Map<String, String> headers) throws IOException;

    String delete(String uri) throws IOException;

    String delete(String uri, Map<String, String> headers) throws IOException;

    String deleteEx(String uri, String body) throws IOException;

    String deleteEx(String uri, String body, Map<String, String> headers) throws IOException;

    String post(String uri, String body) throws IOException;

    String post(String uri, String body, Map<String, String> headers) throws IOException;

    String put(String uri, String body) throws IOException;

    String put(String uri, String body, Map<String, String> headers) throws IOException;
}
