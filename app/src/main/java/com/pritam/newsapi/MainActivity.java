package com.pritam.newsapi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;


import android.app.ProgressDialog;
import android.os.AsyncTask;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pritam.newsapi.utility.AppConstant;
import com.pritam.newsapi.utility.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

// Get Detail imformation of API used ~ https://newsapi.org/

public class MainActivity extends Activity {

    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;
    ArrayList<HashMap<String, String>> aList;

    ListView list;
    LazyAdapter adapter;

    // URL to get contacts JSON
    private String url;
   // static public Typeface t1,t2,t3,t4,t5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Font path
        //Typeface t1= Typeface.createFromAsset(this.getResources().getAssets(), "assets/fonts/future.ttf");
       /* t2= Typeface.createFromAsset(getAssets(), "font/DroidSans.ttf");
        t3= Typeface.createFromAsset(getAssets(), "font/DroidSans-Bold.ttf");
        t4= Typeface.createFromAsset(getAssets(), "font/DroidSansMono.ttf");
        t5= Typeface.createFromAsset(getAssets(), "font/smart watch.ttf");*/

        TextView title = (TextView) findViewById(R.id.createby);
        //title.setTypeface(t1);
        ((TextView) findViewById(R.id.createby)).setVisibility(View.GONE);

        ((AppConstant) this.getApplication()).setCompressImage(false);

        url = getResources().getString(R.string.srcurl);

        lv = (ListView) findViewById(R.id.list);

        new GetNewChannelList().execute();
    }

    @Override
    protected void onResume() {
        ((AppConstant) this.getApplication()).setCompressImage(false);
        super.onResume();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetNewChannelList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null && jsonStr.length() > 0) {
                try {
                    JSONObject jsonObj =  new JSONObject(jsonStr);

                    JSONArray sources = jsonObj.getJSONArray("sources");

                    //System.out.println(sources.length());

                    aList = new ArrayList<HashMap<String,String>>();

                    for (int i = 0; i < sources.length(); i++)
                    {
                        JSONObject c = sources.getJSONObject(i);

                        HashMap<String, String> hm = new HashMap<String, String>();
                        // adding each child node to HashMap key => value
                        hm.put("id", c.getString("id").toString());
                        hm.put("name", c.getString("name").toString());
                        hm.put("description", c.getString("description").toString());
                        hm.put("url", c.getString("url").toString());
                        hm.put("category", c.getString("category").toString().toUpperCase());
                        hm.put("language", c.getString("language").toString());
                        hm.put("country", c.getString("country").toString());
                        JSONObject urlsToLogos = c.getJSONObject("urlsToLogos");
                        hm.put("urlsToLogo_s", urlsToLogos.getString("small").toString());
                        hm.put("urlsToLogo_m", urlsToLogos.getString("medium").toString());
                        hm.put("urlsToLogo_l", urlsToLogos.getString("large").toString());

                        String sort = "";
                        JSONArray sortBysAvailable = c.getJSONArray("sortBysAvailable");
                        sort =  sortBysAvailable.toString().replace("[", "").replace("]", "").replace("\"", "").replace(",", "~");
                        hm.put("sortBysAvailable", sort);
                        aList.add(hm);
                    }

                    System.out.println(aList.toString());

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */

            list=(ListView)findViewById(R.id.list);

            // Getting adapter by passing xml data ArrayList
            adapter=new LazyAdapter(MainActivity.this, aList);
            list.setAdapter(adapter);

            findViewById(R.id.list).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.createby)).setVisibility(View.VISIBLE);
           // findViewById(R.id.progressBar).setVisibility(View.GONE);

            // Click event for single list row
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    Intent in = new Intent(getApplicationContext(), NewsActivity.class);
                        in.putExtra("id", aList.get(position).get("id"));
                    in.putExtra("sort", aList.get(position).get("sortBysAvailable"));
                    startActivity(in);
                }
            });
        }

    }
}
