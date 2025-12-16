package org.dpnam28.foodcouriers.utils;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.UUID;

public class MultipartRequest extends Request<JSONObject> {

    private static final String LINE_END = "\r\n";
    private static final String TWO_HYPHENS = "--";

    private final Response.Listener<JSONObject> listener;
    private final Map<String, String> stringParts;
    private final Map<String, DataPart> fileParts;
    private final String boundary = "apiclient-" + UUID.randomUUID();

    public MultipartRequest(
            int method,
            String url,
            Map<String, String> stringParts,
            Map<String, DataPart> fileParts,
            Response.Listener<JSONObject> listener,
            Response.ErrorListener errorListener
    ) {
        super(method, url, errorListener);
        this.listener = listener;
        this.stringParts = stringParts;
        this.fileParts = fileParts;
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data;boundary=" + boundary;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            if (stringParts != null) {
                for (Map.Entry<String, String> entry : stringParts.entrySet()) {
                    writeTextPart(bos, entry.getKey(), entry.getValue());
                }
            }
            if (fileParts != null) {
                for (Map.Entry<String, DataPart> entry : fileParts.entrySet()) {
                    writeFilePart(bos, entry.getKey(), entry.getValue());
                }
            }
            bos.write((TWO_HYPHENS + boundary + TWO_HYPHENS + LINE_END).getBytes());
        } catch (IOException e) {
            throw new AuthFailureError("Cannot build multipart request", e);
        }
        return bos.toByteArray();
    }

    private void writeTextPart(ByteArrayOutputStream bos, String name, String value) throws IOException {
        if (value == null) {
            return;
        }
        bos.write((TWO_HYPHENS + boundary + LINE_END).getBytes());
        bos.write(("Content-Disposition: form-data; name=\"" + name + "\"" + LINE_END).getBytes());
        bos.write(("Content-Type: text/plain; charset=UTF-8" + LINE_END).getBytes());
        bos.write(LINE_END.getBytes());
        bos.write(value.getBytes("UTF-8"));
        bos.write(LINE_END.getBytes());
    }

    private void writeFilePart(ByteArrayOutputStream bos, String name, DataPart data) throws IOException {
        if (data == null || data.getContent() == null) {
            return;
        }
        bos.write((TWO_HYPHENS + boundary + LINE_END).getBytes());
        bos.write(("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + data.getFileName() + "\"" + LINE_END).getBytes());
        bos.write(("Content-Type: " + data.getType() + LINE_END).getBytes());
        bos.write(LINE_END.getBytes());
        bos.write(data.getContent());
        bos.write(LINE_END.getBytes());
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JSONException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        listener.onResponse(response);
    }

    public static class DataPart {
        private final String fileName;
        private final byte[] content;
        private final String type;

        public DataPart(String fileName, byte[] content, @Nullable String type) {
            this.fileName = fileName;
            this.content = content;
            this.type = type == null ? "application/octet-stream" : type;
        }

        public String getFileName() {
            return fileName;
        }

        public byte[] getContent() {
            return content;
        }

        public String getType() {
            return type;
        }
    }
}
