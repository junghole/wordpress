package com.wordpress.smjs2000.wordpress;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(MainActivity.this));
        getSupportActionBar().setTitle("smjs2000.wordpress.com");

        new GetPost().execute();
    }

    private class GetPost extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String url = "https://public-api.wordpress.com/rest/v1.1/sites/smjs2000.wordpress.com/posts/";
            String data = null;
            try {
                data = downloadUrl(url);
            } catch(Exception e) {
                Log.i("Task",e.toString());
            }

            return data;
        }

        @Override
        protected void onPostExecute(String s) {

            ArrayList<PostItem> postList = new ArrayList<>();
            if (s != null) {
                try {
                    JSONObject json = new JSONObject(s);
                    JSONArray jsonArray = json.getJSONArray("posts");
                    for (int i=0;i < jsonArray.length(); i++) {
                        PostItem postItem = new PostItem();
                        JSONObject post = jsonArray.getJSONObject(i);
                        postItem.setId(post.getString("ID"));
                        postItem.setTitle(post.getString("title"));
                        postItem.setAuthor(post.getJSONObject("author").getString("name"));
                        postItem.setDate(post.getString("date").substring(0, post.getString("date").indexOf("T")));

                        String content = post.getString("content");
                        String imgUrl = content.substring(content.indexOf("https://"), content.indexOf("\">"));
                        postItem.setImgUrl(imgUrl);

                        // parse paragraph string
                        /*content = content.substring(5);
                        content = content.substring(content.indexOf("<p>"));
                        content = content.replaceAll("<p>", "").replaceAll("</p>", "");
                        postItem.setContent(content);*/

                        postItem.setContent(post.getString("content"));

                        postItem.setPostUrl(post.getString("URL"));

                        postList.add(postItem);
                    }

                    ListView list = (ListView) findViewById(R.id.main_list);
                    PostAdapter adapter = new PostAdapter(MainActivity.this, postList);
                    list.setAdapter(adapter);

                } catch (Exception e) {
                    Log.i("Json", "fail");
                }
            } else Log.i("Fail", "null");
        }

        private String downloadUrl(String strUrl) throws IOException {
            String data = "";
            InputStream iStream = null;
            try {
                URL url = new URL(strUrl);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.connect();

                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb  = new StringBuffer();

                String line = "";
                while ((line = br.readLine())  != null){
                    sb.append(line);
                }

                data = sb.toString();

                br.close();

            } catch(Exception e) {
                Log.i("Downloading", e.toString());
            } finally {
                iStream.close();
            }

            return data;
        }
    }
}
