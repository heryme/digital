package com.digitalscale.services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by Ankur on 11-12-2015.
 */
public class MultipartRequest extends Request<JSONObject> {

    private static final String TAG = MultipartRequest.class.getSimpleName();

    MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
    private final Response.Listener listener;
    private final Map<String, Object> params;
    private HttpEntity entity;

    public MultipartRequest(String url, Map<String, Object> params, Response.Listener<JSONObject> listner, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.listener = listner;
        this.params = params;
        buildEntity();
    }

    private void buildEntity() {

        for (String key : params.keySet()) {

            if (params.get(key) instanceof File) {
                File file = (File) params.get(key);
                Log.d(TAG, "Key Data = " + key + " File=" + file + ", " + file.getAbsolutePath());
                entityBuilder.addBinaryBody(key, file);

            } else {
                entityBuilder.addPart(key, new StringBody(String.valueOf(params.get(key)), ContentType.TEXT_PLAIN));
                Log.d(TAG, "Key File  = " + key + "    Value : " + params.get(key));
            }
        }
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        entity = entityBuilder.build();
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    public String getBodyContentType() {
        Log.d(TAG, "Content-Type >> " + entity.getContentType().getValue());
        return entity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            entity.writeTo(bos);
            Log.d(TAG, "BOS ISZE = " + bos.size());
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream bos, building the multipart request.");
        }

        return bos.toByteArray();
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        listener.onResponse(response);
    }

    private ContentBody compressImage(final String imgPath, final String key) {

        try {
            Bitmap bmp = BitmapFactory.decodeFile(imgPath);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, bos);
            return new ByteArrayBody(bos.toByteArray(), key.trim() + ".jpg");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private FileInputStream inputStreamData(String path) {
        FileInputStream in = null;
        byte[] data = new byte[(int) new File(path).length()];
        String filecontent = null;
        try {
            new FileInputStream(path).read(data);
            filecontent = new String(data);
        } catch (Exception e) {

        }
        return in;
    }
}
