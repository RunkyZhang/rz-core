package com.rz.core.http;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import com.rz.core.Assert;
import com.rz.core.RZHelper;

// CaseInsensitiveMap
public class HttpAgentImpl implements HttpAgent {
    private final static int TIMEOUT = 30 * 1000;
    private static HttpAgent httpAgent = new HttpAgentImpl();
    private List<String> baseUris;
    private int timeout;
    private List<Header> headers;
    private String contentCharset;
    private String contentMimeType;
    private String acceptContentType;
    private String currentBaseUri;

    public static HttpAgent getHttpAgent() {
        return HttpAgentImpl.httpAgent;
    }

    public HttpAgentImpl() {
        this(null, HttpAgentImpl.TIMEOUT, null, HttpDecompressionMethods.Both, "application/json", StandardCharsets.UTF_8.name(), "application/json;charset=utf-8");
    }

    public HttpAgentImpl(
            List<String> baseUris,
            int timeout,
            Map<String, String> headers,
            HttpDecompressionMethods httpDecompressionMethods,
            String contentMimeType,
            String contentCharset,
            String acceptContentType) {
        this.baseUris = new ArrayList<>();
        if (!RZHelper.isEmptyCollection(baseUris)) {
            for (String baseUri : baseUris) {
                if (false == StringUtils.isBlank(baseUri)) {
                    if (true == baseUri.endsWith("/")) {
                        this.baseUris.add(baseUri.substring(0, baseUri.length() - 1));
                    } else {
                        this.baseUris.add(baseUri);
                    }
                }
            }
        }
        this.setCurrentBaseUri();
        this.headers = new ArrayList<>();
        httpDecompressionMethods = null == httpDecompressionMethods ? HttpDecompressionMethods.None : httpDecompressionMethods;
        this.timeout = 0 >= timeout ? HttpAgentImpl.TIMEOUT : timeout;
        this.contentCharset = true == StringUtils.isBlank(contentCharset) ? "UTF-8" : contentCharset;
        this.contentMimeType = true == StringUtils.isBlank(contentMimeType) ? "application/json" : contentMimeType;
        this.acceptContentType = true == StringUtils.isBlank(acceptContentType) ? "application/json;charset=utf-8" : acceptContentType;

        if (!RZHelper.isEmptyCollection(headers)) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                if (false == StringUtils.equalsIgnoreCase("Accept-Encoding", header.getKey()) || HttpDecompressionMethods.None == httpDecompressionMethods) {
                    this.headers.add(new BasicHeader(header.getKey(), header.getValue()));
                }
            }
        }
        if (HttpDecompressionMethods.None != httpDecompressionMethods) {
            this.headers.add(new BasicHeader("Accept-Encoding", this.getAcceptEncoding(httpDecompressionMethods)));
        }
        this.headers.add(new BasicHeader("Connection", "Keep-Alive"));
        this.headers.add(new BasicHeader("Accept", this.acceptContentType));
    }

    public String get(String uri) throws IOException {
        return this.get(uri, null);
    }

    public String get(String uri, Map<String, String> headers) throws IOException {
        return this.sendRequest("Get", uri, "", headers);
    }

    public String delete(String uri) throws IOException {
        return this.delete(uri, null);
    }

    public String delete(String uri, Map<String, String> headers) throws IOException {
        return this.sendRequest("Delete", uri, "", headers);
    }

    @Override
    public String deleteEx(String uri, String body)  throws IOException{
        return this.deleteEx(uri, body, null);
    }

    @Override
    public String deleteEx(String uri, String body, Map<String, String> headers) throws IOException {
        return this.sendRequest("Delete", uri, body, headers);
    }
    
    public String post(String uri, String body) throws IOException {
        return this.post(uri, body, null);
    }

    public String post(String uri, String body, Map<String, String> headers) throws IOException {
        return this.sendRequest("Post", uri, body, headers);
    }

    public String put(String uri, String body) throws IOException {
        return this.put(uri, body, null);
    }

    public String put(String uri, String body, Map<String, String> headers) throws IOException {
        return this.sendRequest("Put", uri, body, headers);
    }

    private String sendRequest(String httpMethod, String uri, String body, Map<String, String> headers) throws IOException {
        String url = null;
        if (StringUtils.isBlank(this.currentBaseUri)) {
            Assert.isNotBlank(uri, "uri");
            url = uri;
        } else {
            if (StringUtils.isBlank(uri)) {
                url = this.currentBaseUri;
            } else {
                url = this.currentBaseUri + (uri.startsWith("/") ? uri : "/" + uri);
            }
        }

        HttpRequestBase httpRequest = this.buildHttpRequest(httpMethod, url, body, headers);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(this.timeout).setSocketTimeout(this.timeout).setConnectionRequestTimeout(this.timeout).build();
        
        try (CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
                CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpRequest)) {
            HttpEntity httpEntity = closeableHttpResponse.getEntity();
            String charset = this.getcharset(httpEntity, "UTF-8");
            return EntityUtils.toString(httpEntity, charset);
        }
    }

    private HttpRequestBase buildHttpRequest(String httpMethod, String url, String body, Map<String, String> headers) {
        HttpRequestBase httpRequest = null;
        if (StringUtils.equalsIgnoreCase("Get", httpMethod)) {
            HttpGet httpGet = new HttpGet(url);
            httpRequest = httpGet;
        } else if (StringUtils.equalsIgnoreCase("Delete", httpMethod)) {
            if(StringUtils.isBlank(body)){
                HttpDelete httpDelete = new HttpDelete(url);
                httpRequest = httpDelete;
            }else{
                HttpDeleteEx httpDeleteEx = new HttpDeleteEx(url);
                ContentType contentType = ContentType.create(this.contentMimeType, this.contentCharset);
                StringEntity stringEntity = new StringEntity(body, contentType);
                httpDeleteEx.setEntity(stringEntity);
                httpRequest = httpDeleteEx;
            }
        } else if (StringUtils.equalsIgnoreCase("Post", httpMethod)) {
            HttpPost httpPost = new HttpPost(url);
            ContentType contentType = ContentType.create(this.contentMimeType, this.contentCharset);
            StringEntity stringEntity = new StringEntity(body, contentType);
            httpPost.setEntity(stringEntity);
            httpRequest = httpPost;
        } else if (StringUtils.equalsIgnoreCase("Put", httpMethod)) {
            HttpPut httpPut = new HttpPut(url);
            ContentType contentType = ContentType.create(this.contentMimeType, this.contentCharset);
            StringEntity stringEntity = new StringEntity(body, contentType);
            httpPut.setEntity(stringEntity);
            httpRequest = httpPut;
        } else {
            throw new UnsupportedOperationException(httpMethod);
        }
        httpRequest.setHeaders(this.mergeHeaders(headers));
        httpRequest.setProtocolVersion(HttpVersion.HTTP_1_1);
        return httpRequest;
    }

    private String getAcceptEncoding(HttpDecompressionMethods httpDecompressionMethods) {
        if (HttpDecompressionMethods.GZip == httpDecompressionMethods || HttpDecompressionMethods.Deflate == httpDecompressionMethods) {
            return httpDecompressionMethods.name().toLowerCase();
        } else if (HttpDecompressionMethods.Both == httpDecompressionMethods) {
            return HttpDecompressionMethods.GZip.name().toLowerCase() + "," + HttpDecompressionMethods.Deflate.name().toLowerCase();
        } else {
            return "";
        }
    }

    private void setCurrentBaseUri() {
        if (RZHelper.isEmptyCollection(this.baseUris)) {
            this.currentBaseUri = "";
        } else {
            this.currentBaseUri = this.baseUris.get(0);
        }
    }

    private Header[] mergeHeaders(Map<String, String> headers) {
        List<Header> finalHeaders = new ArrayList<>(this.headers);

        if (!RZHelper.isEmptyCollection(headers)) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                finalHeaders.add(new BasicHeader(header.getKey(), header.getValue()));
            }
        }

        if (RZHelper.isEmptyCollection(finalHeaders)) {
            return new Header[] {};
        } else {
            return finalHeaders.toArray(new Header[finalHeaders.size()]);
        }
    }

    private String getcharset(HttpEntity httpEntity, String defaultCharset) {
        String charset = null;
        if (null != httpEntity.getContentType()) {
            HeaderElement[] headerElements = httpEntity.getContentType().getElements();
            if (0 < headerElements.length) {
                NameValuePair nameValuePair = headerElements[0].getParameterByName("charset");
                if (null != nameValuePair) {
                    charset = nameValuePair.getValue();
                }
            }
        }
        return null == charset ? defaultCharset : charset;
    }

}
