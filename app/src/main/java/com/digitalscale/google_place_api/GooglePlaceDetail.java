package com.digitalscale.google_place_api;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.digitalscale.constant.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
public class GooglePlaceDetail {

	static Context context;

	@SuppressWarnings("static-access")
	public GooglePlaceDetail(Context context) {
		this.context = context;
	}

	private static final String TAG = GooglePlaceApi.class.getSimpleName();

	private static final String PLACES_DETAIL_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_DETAILS = "/details";
	private static final String OUT_JSON = "/json";

	private static final String API_KEY = "AIzaSyCu2xmBMPLxf2AeWq3JBTdBnv0osVYsu_M";
	String placeId = "";

	public static HashMap<String, Object> getLatLngByPlaceId(String placeId) {
		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		HashMap<String, Object> locationMap = null;
		try {
			StringBuilder sb = new StringBuilder(PLACES_DETAIL_API_BASE + TYPE_DETAILS + OUT_JSON);
			sb.append("?key=" + API_KEY);
			sb.append("&placeid=" + placeId);

			System.out.println("GooglePlaceDetailApiURL : " + sb.toString());

			URL url = new URL(sb.toString());
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());

			// Load the results into a StringBuilder
			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}
		} catch (MalformedURLException e) {
			Log.e(TAG, "Error processing Places API URL", e);
			return locationMap;
		} catch (IOException e) {
			Log.e(TAG, "Error connecting to Places API", e);
			return locationMap;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		return parse(jsonResults.toString());
	}

	/** Receives a JSONObject and returns a list */
	public static HashMap<String, Object> parse(String response) {

		Double lat = Double.valueOf(0);
		Double lng = Double.valueOf(0);

		HashMap<String, Object> hm = new HashMap<String, Object>();

		try {
			JSONObject jObject = new JSONObject(response);
			lat = (Double) jObject.getJSONObject("result").getJSONObject("geometry").getJSONObject("location")
					.get("lat");
			lng = (Double) jObject.getJSONObject("result").getJSONObject("geometry").getJSONObject("location")
					.get("lng");

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		hm.put("LAT", Double.toString(lat));
		hm.put("LNG", Double.toString(lng));

		return hm;
	}

	public static void getLocationOfGooglePlace(String placeId) {

		new GetLocationOfGooglePlace().execute(placeId);

	}

	public static class GetLocationOfGooglePlace extends AsyncTask<String, Void, String> {

		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(GooglePlaceDetail.context);
			pd.setTitle("Please Wait...");
			pd.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String placeId = params[0];
			StringBuilder sb;

				sb = new StringBuilder(Constant.PLACES_DETAIL_API_BASE + Constant.TYPE_DETAILS
						+ Constant.OUT_JSON);
				sb.append("?key=" + Constant.API_KEY);
				sb.append("&placeid=" + placeId);
				
				System.out.println("GooglePlaceDetailApiURL : " + sb.toString());

				
			return Utils.getService(""+sb.toString()); /*jsonResults.toString();*/
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pd.dismiss();
			HashMap<String, Object> map = GooglePlaceDetail.parse(result);
			System.out.println("LAT : " + map.get("LAT"));
			System.out.println("LNG : " + map.get("LNG"));
		}

	}
}
