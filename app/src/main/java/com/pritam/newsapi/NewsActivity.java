package com.pritam.newsapi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class NewsActivity extends Activity {

    private String TAG = NewsActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;
    ArrayList<HashMap<String, String>> aList;

    ListView list;
    LazyAdapter adapter;
    String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        //getActionBar().hide();

        String id = getIntent().getStringExtra("id").toString();
        String sort = getIntent().getStringExtra("sort").toString().split("~")[0];

        ((AppConstant) this.getApplication()).setCompressImage(true);

        URL = getResources().getString(R.string.listurl)+id+"&sortBy="+sort+"&apiKey="+getResources().getString(R.string.apiKey);

        //Toast.makeText(this, URL.toString(), Toast.LENGTH_SHORT).show();

        lv = (ListView) findViewById(R.id.list);

        new GetList().execute();
    }

    @Override
    protected void onResume() {
        ((AppConstant) this.getApplication()).setCompressImage(true);
        super.onResume();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(NewsActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(URL);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null && jsonStr.length() > 0) {
                try {
                    JSONObject jsonObj =  new JSONObject(jsonStr);

                    JSONArray sources = jsonObj.getJSONArray("articles");

                    //System.out.println(sources.length());

                    aList = new ArrayList<HashMap<String,String>>();

                    for (int i = 0; i < sources.length(); i++)
                    {
                        JSONObject c = sources.getJSONObject(i);

                        HashMap<String, String> hm = new HashMap<String, String>();
                        // adding each child node to HashMap key => value
                        hm.put("author", c.getString("author").toString());
                        hm.put("title", c.getString("title").toString());
                        hm.put("description", c.getString("description").toString());
                        hm.put("url", c.getString("url").toString());
                        hm.put("urlToImage", c.getString("urlToImage").toString());
                        hm.put("publishedAt", c.getString("publishedAt").toString());
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


            if(!(aList != null && aList.size() > 0)) {
                Toast.makeText(NewsActivity.this, "No News are Available ", Toast.LENGTH_SHORT).show();
                onBackPressed();
            } else {
                list = (ListView) findViewById(R.id.list);

                // Getting adapter by passing xml data ArrayList
                adapter = new LazyAdapter(NewsActivity.this, aList);
                list.setAdapter(adapter);

                findViewById(R.id.list).setVisibility(View.VISIBLE);
                // findViewById(R.id.progressBar).setVisibility(View.GONE);

                // Click event for single list row
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        Intent in = new Intent(getApplicationContext(), NewsWebActivity.class);
                        in.putExtra("url", aList.get(position).get("url"));
                        startActivity(in);
                    }
                });
            }
        }

    }

}
