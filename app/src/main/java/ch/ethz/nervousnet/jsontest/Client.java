package ch.ethz.nervousnet.jsontest;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by Dario on 15.07.17.
 */

public class Client {
    //Default Connection Constants
    private final String SCHEME = "http";
    private final String HOST = "192.168.0.220";
    private final String PORT = "9200";
    private final int TIMEOUT = 5000;

    //Variables
    private String scheme;
    private String host;
    private String port;
    private String domain;
    private int timeout;
    private HttpGet httpGet;
    private HttpGetWithUser httpGetWithUser;
    private HttpPost httpPost;
    private HttpPostWithUser httpPostWithUser;

    private TextView tvConnectionJson;
    private BufferedReader reader = null;

    //Constructors
    public Client() {
        this.scheme = SCHEME;
        this.host = HOST;
        this.port = PORT;
        this.timeout = TIMEOUT;
        this.domain = SCHEME + "://" + HOST + ":" + PORT;
        initializeHttpFunctions();
    }

    public Client(String scheme, String host, String port) {
        this.scheme = scheme;
        this.host = host;
        this.port = port;
        if (port.length() > 0) this.domain = scheme + "://" + host + ":" + port;
        else this.domain = scheme + "://" + host;
        initializeHttpFunctions();
    }

    //Initialize Http Functions
    private void initializeHttpFunctions() {
        httpGet = new HttpGet();
        httpGetWithUser = new HttpGetWithUser();
        httpPost = new HttpPost();
        httpPostWithUser = new HttpPostWithUser();
    }

    //Getter
    public String getScheme() {
        return scheme;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getDomain() {
        return domain;
    }

    public int getTimeout() {
        return timeout;
    }

    public TextView getTvConnectionJson() {
        return tvConnectionJson;
    }

    //Setter
    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setTvConnectionJson(TextView tvConnectionJson) {
        this.tvConnectionJson = tvConnectionJson;
    }

    //Public Types
    //
    //    ---- Public data types
    //

    class Params {
        public String from;
        public String size;
        public String sortBy;
        public String sortDir;
        public String task;
        public String state;
        public String verified;
    }

    class Meta {
        public int Total;
        public int From;
        public int Size;
    }

    class Project {
        public String id;
        public String name;
        public String description;
        public int assetCount;
        public int taskCount;
        public int userCount;
        public Counts assignmentCount;
        public MetaProperty[] metaProperties;
    }

    class Task {
        public String id;
        public String project;
        public String name;
        public String description;
        public String currentState;
        public AssignmentCriteria assignmentCriteria;
        public CompletionCriteria completionCriteria;
    }

    class Asset {
        public String id;
        public String project;
        public String url;
        public String name;
        public Map<String, Object> metadata;
        public SubmittedData submittedData;
        public boolean favorited;
        public boolean verified;
        public Counts counts;
    }

    class User {
        public String id;
        public String name;
        public String email;
        public String project;
        public String externalId;
        public Counts counts;
        public UserFavorites favorites;
        public UserFavorites newFavorites;
        public String[] verifiedAssets;
    }

    class Assignment {
        public String id;
        public String user;
        public String project;
        public String task;
        public Asset asset;
        public String state;
        public SubmittedData submittedData;
    }

    class Counts {
        public Map<String, Integer> metadata;
    }

    class MetaProperty {
        public String name;
        public String type;
    }

    class AssignmentCriteria {
        public Map<String, Object> submittedData;
    }

    class CompletionCriteria {
        public int total;
        public int matching;
    }

    class SubmittedData {
        public Map<String, Object> submittedData;
    }

    class UserFavorites {
        public Map<String, Asset> userFavorites;
    }

    //
    //    ---- Admin interface
    //

    //
    //    Setup
    //

    String adminRoot(String projectId, String taskId) {
        httpGet.setHiveEndpoint("/");
        httpGet.execute();

        return httpGet.getResponse();
    }

    // AdminSetup -- finds or creates an unfinished task assignment for the current user
    // /admin/setup/{DELETE_MY_DATABASE} [put]
    String adminSetup(Boolean resetDb, JSONObject project, JSONArray tasks, JSONArray assets) {
        if(resetDb) httpPost.setHiveEndpoint("/admin/setup/YES_I_AM_SURE");
        else httpPost.setHiveEndpoint("/admin/setup");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Project", project);
            jsonObject.put("Tasks", tasks);
            jsonObject.put("Assets", assets);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpPost.setJsonObject(jsonObject);
        httpPost.execute();

        return httpPost.getResponse();
    }

    //
    //    Projects
    //

    // AdminProjects -- returns a paginated list of projects in Hive
    // /admin/projects [get]
    String adminProjects(String projectId, String taskId) {
        httpGet.setHiveEndpoint("/admin/projects");
        httpGet.execute();

        return httpGet.getResponse();
    }

    // AdminProject -- returns a project by ID
    // /admin/projects/{project_id} [get]
    String adminProject(String projectId, String taskId) {
        httpGet.setHiveEndpoint("/admin/projects/" + projectId);
        httpGet.execute();

        return httpGet.getResponse();
    }

    // AdminCreateProject -- creates or updates a project
    // /admin/projects/{project_id} [post]
    String adminCreateProject(JSONObject project, String projectId) {
        httpPost.setHiveEndpoint("/admin/projects/" + projectId);
        httpPost.setJsonObject(project);
        httpPost.execute();

        return httpPost.getResponse();
    }

    //
    //    Tasks
    //

    // AdminTasks -- returns a paginated list of tasks in a project
    // /admin/projects/{project_id}/tasks [get]
    String adminTasks(String projectId, String taskId) {
        httpGet.setHiveEndpoint("/admin/projects/" + projectId + "/tasks/");
        httpGet.execute();

        return httpGet.getResponse();
    }

    // AdminTask -- returns info for a single task by ID
    // /admin/projects/{project_id}/tasks/{task_id} [get]
    String adminTask(String projectId, String taskId) {
        httpGet.setHiveEndpoint("/admin/projects/" + projectId + "/tasks/" + taskId);
        httpGet.execute();

        return httpGet.getResponse();
    }

    // AdminCreateTasks -- creates or updates tasks in a project
    // /admin/projects/{project_id}/tasks [post]
    String adminCreateTasks(JSONArray tasks, String projectId) {
        httpPost.setHiveEndpoint("/admin/projects/" + projectId + "/tasks");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Tasks", tasks);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpPost.setJsonObject(jsonObject);
        httpPost.execute();

        return httpPost.getResponse();
    }

    // AdminCreateTask -- creates or updates a task in a project
    // /admin/projects/{project_id}/tasks/{task_id} [post]
    String adminCreateTask(JSONObject task, String projectId, String taskId) {
        httpPost.setHiveEndpoint("/admin/projects/" + projectId + "/tasks/" + taskId);
        httpPost.setJsonObject(task);
        httpPost.execute();

        return httpPost.getResponse();
    }

    // AdminCompleteTask -- updates assets matching task CompletionCriteria with SubmittedData
    // /admin/projects/{project_id}/tasks/{task_id}/complete [get]
    String adminCompleteTask(String projectId, String taskId) {
        httpGet.setHiveEndpoint("/admin/projects/" + projectId + "/tasks/" + taskId + "/complete");
        httpGet.execute();

        return httpGet.getResponse();
    }

    // AdminDisableTask -- makes a task unavailable for assignment by disabling it
    // /admin/projects/{project_id}/tasks/{task_id}/disable [get]
    String adminDisableTask(String projectId, String taskId) {
        httpGet.setHiveEndpoint("/admin/projects/" + projectId + "/tasks/" + taskId + "/disable");
        httpGet.execute();

        return httpGet.getResponse();
    }

    // AdminEnableTask -- makes a task available for assignment by enabling it
    // /admin/projects/{project_id}/tasks/{task_id}/enable [get]
    String adminEnableTask(String projectId, String taskId) {
        httpGet.setHiveEndpoint("/admin/projects/" + projectId + "/tasks/" + taskId + "/enable");
        httpGet.execute();

        return httpGet.getResponse();
    }

    //
    //    Assets
    //

    // AdminAssets -- returns a paginated list of assets in a project
    // /admin/projects/{project_id}/assets [get]
    String adminAssets(String projectId) {
        httpGet.setHiveEndpoint("/admin/projects/" + projectId + "/assets");
        httpGet.execute();

        return httpGet.getResponse();
    }

    // AdminAsset -- retrieves a single project asset defined by an id
    // /admin/projects/{project_id}/assets/{asset_id} [get]
    String adminAsset(String projectId, String assetId) {
        httpGet.setHiveEndpoint("/admin/projects/" + projectId + "/assets/" + assetId);
        httpGet.execute();

        return httpGet.getResponse();
    }

    // AdminCreateAssets -- creates assets in a project
    // /admin/projects/{project_id}/assets [post]
    String adminCreateAssets(JSONArray assets, String projectId) {
        httpPost.setHiveEndpoint("/admin/projects/" + projectId + "/assets");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Assets", assets);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpPost.setJsonObject(jsonObject);
        httpPost.execute();

        return httpPost.getResponse();
    }

    //
    //    Users
    //

    // AdminUsers -- returns a paginated list of users in a project
    // /admin/projects/{project_id}/users [get]
    String adminUsers(String projectId) {
        httpGet.setHiveEndpoint("/admin/projects/" + projectId + "/users/");
        httpGet.execute();

        return httpGet.getResponse();
    }

    // AdminUser -- returns a single user in a project by ID
    // /admin/projects/{project_id}/users/{user_id} [get]
    String adminUser(String projectId, String userId) {
        httpGet.setHiveEndpoint("/admin/projects/" + projectId + "/users/" + userId);
        httpGet.execute();

        return httpGet.getResponse();
    }

    //
    //    Assignments
    //

    // AdminAssignments -- returns a paginated list of assignments in a task
    // /admin/projects/{project_id}/assignments [get]
    String adminAssignments(String projectId) {
        httpGet.setHiveEndpoint("/admin/projects/" + projectId + "/assignments/");
        httpGet.execute();

        return httpGet.getResponse();
    }

    //
    //    ---- User interface
    //

    //
    //    Projects
    //

    // Project -- returns a project by ID
    // /projects/{project_id} [get]
    String project(String projectId) {
        httpGet.setHiveEndpoint("/projects/" + projectId);
        httpGet.execute();

        return httpGet.getResponse();
    }

    //
    //    Tasks
    //

    // Tasks -- returns a paginated list of tasks in a project
    // /projects/{project_id}/tasks [get]
    String tasks(String projectId) {
        httpGet.setHiveEndpoint("/projects/" + projectId + "/tasks/");
        httpGet.execute();

        return httpGet.getResponse();
    }

    // Task -- returns public info for a single task by ID
    // /projects/{project_id}/tasks/{task_id} [get]
    String task(String projectId, String taskId) {
        httpGet.setHiveEndpoint("/projects/" + projectId + "/tasks/" + taskId);
        httpGet.execute();

        return httpGet.getResponse();
    }

    //
    //    Assets
    //

    // Asset -- returns public info for a single asset by ID
    // /projects/{project_id}/assets/{asset_id} [get]
    String asset(String projectId, String taskId) {
        httpGet.setHiveEndpoint("/projects/" + projectId + "/assets/" + taskId);
        httpGet.execute();

        return httpGet.getResponse();
    }

    //
    //    Users
    //

    // User -- returns info for the current user, creating a matching record if none found
    // /projects/{project_id}/user [get]
    String user(String projectId, String userId) {
        httpGetWithUser.setHiveEndpoint("/projects/" + projectId + "/user");
        httpGetWithUser.setUserId(userId);
        httpGetWithUser.setProjectId(projectId);
        httpGetWithUser.execute();

        return httpGetWithUser.getResponse();
    }

    // CreateUser -- creates a user in a project
    // /projects/{project_id}/user [post]
    String createUser(String projectId, JSONObject user) {
        httpPost.setHiveEndpoint("/projects/" + projectId + "/user");
        httpPost.setJsonObject(user);
        httpPost.execute();

        return httpPost.getResponse();
    }

    // ExternalUser -- finds or creates a user by external ID
    // /projects/{project_id}/user/external/{connect} [post]
    String externalUser(String projectId, JSONObject user) {
        httpPost.setHiveEndpoint("/projects/" + projectId + "/user/external");
        httpPost.setJsonObject(user);
        httpPost.execute();

        return httpPost.getResponse();
    }

    // Favorites -- returns a paginated list of favorited assets for the current user
    // /projects/{project_id}/user/favorites [get]
    String favorites(String projectId, String userId) {
        httpGetWithUser.setHiveEndpoint("/projects/" + projectId + "/user/favorites");
        httpGetWithUser.setUserId(userId);
        httpGetWithUser.setProjectId(projectId);
        httpGetWithUser.execute();

        return httpGetWithUser.getResponse();
    }

    // Favorite -- toggles favoriting on an asset for the current user
    // /projects/{project_id}/assets/{asset_id}/favorite [get]
    String favorite(String projectId, String userId, String assetId) {
        httpGetWithUser.setHiveEndpoint("/projects/" + projectId + "/assets/" + assetId + "/favorite");
        httpGetWithUser.setUserId(userId);
        httpGetWithUser.setProjectId(projectId);
        httpGetWithUser.execute();

        return httpGetWithUser.getResponse();
    }

    //
    //    Assignments
    //

    // Assignment -- returns public info for a single assignment by ID
    // /projects/{project_id}/assignments/{assignment_id} [get]
    String assignment(String projectId, String assignmentId) {
        httpGet.setHiveEndpoint("/projects/" + projectId + "/assignments/" + assignmentId);
        httpGet.execute();

        return httpGet.getResponse();
    }

    // AssignAsset -- finds or creates an unfinished assignment for the given asset, task and current user
    // /projects/{project_id}/tasks/{task_id}/assets/{asset_id}/assignments [get]
    String assignAsset(String projectId, String taskId, String assetId, String userId) {
        httpGetWithUser.setHiveEndpoint("/projects/" + projectId + "/tasks/" + taskId + "/assets/" + assetId + "/assignments");
        httpGetWithUser.setUserId(userId);
        httpGetWithUser.setProjectId(projectId);
        httpGetWithUser.execute();

        return httpGetWithUser.getResponse();
    }

    // UserAssignment -- finds or creates an unfinished task assignment for the current user
    // /projects/{project_id}/tasks/{task_id}/assignments [get]
    String userAssignment(String projectId, String userId, String taskId) {
        httpGetWithUser.setHiveEndpoint("/projects/" + projectId + "/tasks/" + taskId + "/assignments");
        httpGetWithUser.setUserId(userId);
        httpGetWithUser.setProjectId(projectId);
        httpGetWithUser.execute();

        return httpGetWithUser.getResponse();
    }

    // UserCreateAssignment -- finishes a task assignment & assigns a new one for the current user
    // /projects/{project_id}/tasks/{task_id}/assignments [post]
    // CreateUser -- creates a user in a project
    // /projects/{project_id}/user [post]
    String userCreateAssignment(String projectId, JSONObject user, String taskId, String userId) {
        httpPostWithUser.setHiveEndpoint("/projects/" + projectId + "/tasks/" + taskId + "/assignments");
        httpPostWithUser.setJsonObject(user);
        httpPostWithUser.setUserId(userId);
        httpPostWithUser.setProjectId(projectId);
        httpPostWithUser.execute();

        return httpPostWithUser.getResponse();
    }

    //Connection function and classes
    private String httpGet(URL url) {
        HttpURLConnection urlConnection = null;
        try {
            //Set urlConnection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(timeout);
            urlConnection.setReadTimeout(timeout);

            //connect
            urlConnection.connect();

            //Return response as String
            return urlConnectionToString(urlConnection);
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
    }

    private class HttpGet extends AsyncTask<Void, Void, String> {
        //Class Variables
        private String hiveEndpoint = "";
        private String response = "";

        @Override
        protected String doInBackground(Void... params) {
            URL url = null;
            try {
                // Construct the URL for the Nervousnet query
                url = new URL(domain + hiveEndpoint);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            return httpGet(url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tvConnectionJson.setText(s);
            response = s;
            Log.i("json", s);
        }

        //Getter ans Setter
        public String getHiveEndpoint() {
            return hiveEndpoint;
        }

        public void setHiveEndpoint(String hiveEndpoint) {
            this.hiveEndpoint = hiveEndpoint;
        }

        public String getResponse() {
            return response;
        }
    }

    private String httpGetWithUser(URL url, String userId, String projectId) {
        HttpURLConnection urlConnection = null;
        try {
            //Set urlConnection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Cookie",projectId + "_user_id=" + userId);
            urlConnection.setConnectTimeout(timeout);
            urlConnection.setReadTimeout(timeout);

            //connect
            urlConnection.connect();

            //Return response as String
            return urlConnectionToString(urlConnection);
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
    }

    private class HttpGetWithUser extends AsyncTask<Void, Void, String> {
        //Class Variables
        private String hiveEndpoint = "";
        private String response = "";
        private String userId = "";
        private String projectId = "";

        @Override
        protected String doInBackground(Void... params) {
            URL url = null;
            try {
                // Construct the URL for the Nervousnet Hive query
                url = new URL(domain + hiveEndpoint);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            return httpGetWithUser(url, userId, projectId);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tvConnectionJson.setText(s);
            response = s;
            Log.i("json", s);
        }

        //Getter ans Setter
        public String getHiveEndpoint() {
            return hiveEndpoint;
        }

        public void setHiveEndpoint(String hiveEndpoint) {
            this.hiveEndpoint = hiveEndpoint;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getProjectId() {
            return projectId;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }

        public String getResponse() {
            return response;
        }
    }

    private String httpPost(URL url, JSONObject jObject) {
        HttpURLConnection urlConnection = null;
        try {
            //Set urlConnection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setConnectTimeout(timeout);
            urlConnection.setReadTimeout(timeout);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");

            //connect
            urlConnection.connect();

            //Send Post Data
            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
            out.write(jObject.toString());
            out.flush();
            out.close();

            //Return response as String
            return urlConnectionToString(urlConnection);
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
    }

    private class HttpPost extends AsyncTask<Void, Void, String> {
        //Class Variables
        private String hiveEndpoint = "";
        private String response = "";
        private JSONObject jsonObject;

        @Override
        protected String doInBackground(Void... params) {
            URL url = null;
            try {
                // Construct the URL for the Nervousnet query
                url = new URL(domain + hiveEndpoint);
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                return null;
            }
            // Create the request to Hive, and open the connection
            return httpPost(url, jsonObject);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tvConnectionJson.setText(s);
            response = s;
            Log.i("json", s);
        }

        //Getter ans Setter
        public String getHiveEndpoint() {
            return hiveEndpoint;
        }

        public void setHiveEndpoint(String hiveEndpoint) {
            this.hiveEndpoint = hiveEndpoint;
        }

        public JSONObject getJsonObject() {
            return jsonObject;
        }

        public void setJsonObject(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        public String getResponse() {
            return response;
        }
    }

    private String httpPostWithUser(URL url, JSONObject jObject, String userId, String projectId) {
        HttpURLConnection urlConnection = null;
        try {
            //Set urlConnection
            System.out.println(url.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Cookie", projectId + "_user_id=" + userId);
            //if(!(urlConnection.getHeaderFields() == null)) System.out.println(urlConnection.getHeaderFields().toString());
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setInstanceFollowRedirects(false);
            //urlConnection.setConnectTimeout(timeout);
            //urlConnection.setReadTimeout(timeout);
            //urlConnection.setDoOutput(true);

            //TEST
            System.out.println(urlConnection.getRequestProperties());



            //connect
            urlConnection.connect();

            //Send Post Data
            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
            out.write(jObject.toString());
            out.flush();
            out.close();

            //Return response as String
            return urlConnectionToString(urlConnection);
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
    }

    private class HttpPostWithUser extends AsyncTask<Void, Void, String> {
        //Class Variables
        private String hiveEndpoint = "";
        private String response = "";
        private String userId = "";
        private String projectId = "";
        private JSONObject jsonObject;

        @Override
        protected String doInBackground(Void... params) {
            URL url = null;
            try {
                // Construct the URL for the Nervousnet query
                url = new URL(domain + hiveEndpoint);
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                return null;
            }
            // Create the request to Hive, and open the connection
            return httpPostWithUser(url, jsonObject, userId, projectId);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tvConnectionJson.setText(s);
            response = s;
            Log.i("json", s);
        }

        //Getter ans Setter
        public String getHiveEndpoint() {
            return hiveEndpoint;
        }

        public void setHiveEndpoint(String hiveEndpoint) {
            this.hiveEndpoint = hiveEndpoint;
        }

        public JSONObject getJsonObject() {
            return jsonObject;
        }

        public void setJsonObject(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getProjectId() {
            return projectId;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }

        public String getResponse() {
            return response;
        }
    }

    private String urlConnectionToString(HttpURLConnection urlConnection) {
        try {
            if(urlConnection.getResponseCode() == 200) {
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                return buffer.toString();
            } else {
                return urlConnection.getResponseCode() + ": " + urlConnection.getResponseMessage();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ERROR: Couldn't get a valid URLRespnse";
    }

    //
    //    ---- Request formatting
    //
    private String formatQuery(Params params) {
        if (params == null) return "";
        String query = "";
        query = appendParam(query, "from", params.from);
        query = appendParam(query, "size", params.size);
        query = appendParam(query, "sortBy", params.sortBy);
        query = appendParam(query, "sortDir", params.sortDir);
        query = appendParam(query, "task", params.task);
        query = appendParam(query, "state", params.state);
        query = appendParam(query, "verified", params.verified);
        return query;
    }

    private String appendParam(String query, String name, String value) {
        if (value.length() > 0) {
            if (query.length() == 0) {
                query = "?" + name + "=" + value;
            } else {
                query += "&" + name + "=" + value;
            }
        }
        return query;
    }
}
