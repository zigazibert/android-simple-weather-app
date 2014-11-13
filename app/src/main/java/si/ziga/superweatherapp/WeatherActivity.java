package si.ziga.superweatherapp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;


public class WeatherActivity extends Activity {

    String URL1 = "http://api.openweathermap.org/data/2.5/find?q=";
    String URL2 = "&units=metric";

    String iconURL1 = "http://openweathermap.org/img/w/";
    String iconURL2 = ".png";

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Button button = (Button)findViewById(R.id.button);
        final EditText city = (EditText)findViewById(R.id.editText);

        final ImageView iconWeather = (ImageView)findViewById(R.id.imageView);
        final TextView weather1 = (TextView)findViewById(R.id.textView2);
        final TextView weather2 = (TextView)findViewById(R.id.textView3);
        final TextView temperatureText = (TextView)findViewById(R.id.textView7);
        final TextView pressureText = (TextView)findViewById(R.id.textView8);
        final TextView humidityText = (TextView)findViewById(R.id.textView9);
        final RelativeLayout background = (RelativeLayout)findViewById(R.id.background);

        queue = Volley.newRequestQueue(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("onClick", "kje je vreme");
                String cityName = city.getText().toString();
                String url = URL1 + cityName + URL2;

                JsonObjectRequest lubenica = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.w("onResponse", response.toString());

                                try {
                                    JSONArray array = response.getJSONArray("list");
                                    Log.i("array", array.toString());
                                    JSONObject data = array.getJSONObject(0);
                                    Log.i("data", data.toString());
                                    JSONObject main = data.getJSONObject("main");
                                    Log.i("main", main.toString());
                                    String temp = main.getString("temp");
                                    String pressure = main.getString("pressure");
                                    String humidity = main.getString("humidity");
                                    Log.i("TEMP: ", temp);
                                    Log.i("PRESSURE: ", pressure);
                                    Log.i("HUMIDITY: ", humidity);
                                    JSONArray weatherArray = data.getJSONArray("weather");
                                    JSONObject weather = weatherArray.getJSONObject(0);
                                    String id = weather.getString("id");
                                    String description1 = weather.getString("main");
                                    String description2 = weather.getString("description");
                                    String icon = weather.getString("icon");
                                    Log.i("WEATHER", id + " " + description1 + " " + description2 + " " + icon);

                                    //set values and icon(with Picasso library - async image loading)
                                    weather1.setText(description1);
                                    weather2.setText(description2);
                                    temperatureText.setText(temp + " \u2103");
                                    pressureText.setText(pressure + " hPa");
                                    humidityText.setText(humidity + " %");

                                    String url = iconURL1 + icon + iconURL2;

                                    Picasso.with(getBaseContext()).load(url).into(iconWeather);

                                    //change background color based on temperature
                                    double t = Double.parseDouble(temp);
                                    if(t < -10){
                                        background.setBackgroundColor(Color.parseColor("#003399"));
                                    }else if(t < 0){
                                        background.setBackgroundColor(Color.parseColor("#66CCFF"));
                                    }else if(t < 10){
                                        background.setBackgroundColor(Color.parseColor("#FFFF66"));
                                    }else if(t < 20){
                                        background.setBackgroundColor(Color.parseColor("#FF9933"));
                                    }else if(t < 30){
                                        background.setBackgroundColor(Color.parseColor("#FF3300"));
                                    }else{
                                        background.setBackgroundColor(Color.parseColor("#FF0000"));
                                    }
                                }catch (Exception e){
                                    Log.e("ERROR", e.toString());
                                }

                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Error", error.toString());
                            }
                        }
                );

                queue.add(lubenica);

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
