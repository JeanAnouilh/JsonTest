package ch.ethz.nervousnet.jsontest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView tvConnectionJson;
    Button btnSetUserInfo;
    Button btnGetUserInfo;
    Button btnGetUserAssignment;
    Button btnReturnUserAssignment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvConnectionJson = (TextView) findViewById(R.id.tv_connection_json);
        btnSetUserInfo = (Button) findViewById(R.id.btn_set_user_info);
        btnGetUserInfo = (Button) findViewById(R.id.btn_get_user_info);
        btnGetUserAssignment = (Button) findViewById(R.id.btn_get_user_assignment);
        btnReturnUserAssignment = (Button) findViewById(R.id.btn_return_user_assignment);
        btnSetUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SetUserInfo().execute();
            }
        });
        btnGetUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetUserInfo().execute();
            }
        });
        btnGetUserAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetUserAssignment().execute();
            }
        });
        btnReturnUserAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ReturnUserAssignment().execute();
            }
        });
    }

    private class SetUserInfo extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String userInfo = "test";

            try {
                // Construct the URL for the OpenWeatherMap query
                URL url = new URL("http://192.168.0.220:9200/projects/nervousnetTwo/user");

                // creates JSON user File
                JSONObject jObject = new JSONObject();
                jObject.put("Id","fleur");
                jObject.put("Name","Fleur Wohlgemuth");
                jObject.put("Email","fwohlgemuth@student.ethz.ch");

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setConnectTimeout(5000);//5 secs
                urlConnection.setReadTimeout(5000);//5 secs
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(jObject.toString());
                out.flush();
                out.close();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                userInfo = buffer.toString();
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            return userInfo;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tvConnectionJson.setText(s);
            Log.i("json", s);
        }
    }

    private class ReturnUserAssignment extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String userAssignment = "test";

            try {
                // Construct the URL for the OpenWeatherMap query
                URL url = new URL("http://192.168.0.220:9200/projects/nervousnetTwo/tasks/record/assignments"); //

                // creates JSON assignment File
                JSONObject jObject = new JSONObject("{\n" +
                        "    \"Id\": \"nervousnetTwoHIVEnervousnetTwo-nervousnettwo-recordHIVEAVvAG-2kWGL0Ai5eJfBXHIVEdario\",\n" +
                        "    \"User\": \"dario\",\n" +
                        "    \"Project\": \"nervousnetTwo\",\n" +
                        "    \"Task\": \"nervousnetTwo-nervousnettwo-record\",\n" +
                        "    \"Asset\": {\n" +
                        "        \"Id\": \"AVvAG-2kWGL0Ai5eJfBX\",\n" +
                        "        \"Project\": \"nervousnetTwo\",\n" +
                        "        \"Url\": \"http://erdw.ethz.ch/nervousnet/basic/intro.html\",\n" +
                        "        \"Name\": \"Sensors\",\n" +
                        "        \"Metadata\": {\n" +
                        "            \"record\": {\n" +
                        "                \"end\": \"2017-02-02T18:29:00Z\",\n" +
                        "                \"sensors\": [\n" +
                        "                    \"accelerometer\"\n" +
                        "                ],\n" +
                        "                \"start\": \"2017-02-02T18:29:00Z\",\n" +
                        "                \"step\": 1000\n" +
                        "            }\n" +
                        "        },\n" +
                        "        \"SubmittedData\": {\n" +
                        "            \"nervousnetTwo-record\": null\n" +
                        "        },\n" +
                        "        \"Favorited\": false,\n" +
                        "        \"Verified\": false,\n" +
                        "        \"Counts\": {\n" +
                        "            \"Assignments\": 2,\n" +
                        "            \"Favorites\": 0,\n" +
                        "            \"finished\": 1,\n" +
                        "            \"skipped\": 0,\n" +
                        "            \"unfinished\": 1\n" +
                        "        }\n" +
                        "    },\n" +
                        "    \"State\": \"unfinished\",\n" +
                        "    \"SubmittedData\": null\n" +
                        "}");
                jObject.put("State", "finished");

                userAssignment = jObject.toString();

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setConnectTimeout(5000);//5 secs
                urlConnection.setReadTimeout(5000);//5 secs
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(jObject.toString());
                out.flush();
                out.close();

                // Read the input stream into a String
                if(urlConnection.getResponseCode() != 400) {
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        // Nothing to do.
                        return null;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0) {
                        // Stream was empty.  No point in parsing.
                        return null;
                    }

                    userAssignment = buffer.toString();
                } else {
                    userAssignment = urlConnection.getResponseMessage();
                }
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            return userAssignment;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tvConnectionJson.setText(s);
            Log.i("json", s);
        }
    }

    private class GetUserInfo extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String userInfo = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL("http://192.168.0.220:9200/projects/nervousnetTwo/user");

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                userInfo = buffer.toString();
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            return userInfo;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tvConnectionJson.setText(s);
            Log.i("json", s);
        }
    }

    private class GetUserAssignment extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String userAssignment = null;

            try {
                // Construct the URL for the Nervousnet Hive query
                URL url = new URL("http://192.168.0.220:9200/projects/nervousnetTwo/tasks/record/assignments"); //projects/nervousnetTwo/tasks/nervousnettwo-record/assignments

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                if(urlConnection.getResponseCode() != 400) {
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        // Nothing to do.
                        return null;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0) {
                        // Stream was empty.  No point in parsing.
                        return null;
                    }

                    userAssignment = buffer.toString();
                } else {
                    userAssignment = urlConnection.getResponseMessage();
                }
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            return userAssignment;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tvConnectionJson.setText(s);
            Log.i("json", s);
        }
    }
}