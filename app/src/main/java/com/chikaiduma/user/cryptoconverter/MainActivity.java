package com.chikaiduma.user.cryptoconverter;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends Activity {


    public Button btExecute;
    public ProgressBar progressBar;
    private EditText amt;
    private TextView result, spinnerText, rate, spinnerTextCrypto, abbCryptoName, cryptoEntered;
    private ImageView spinnerImage, spinnerImageCrypto;
    public int from, to;
    public Spinner fiatCurr, cryptoCurr;
    RequestQueue requestQueue;

    //Used in the API
    private String[] arrayFiat = {
            "CAD", "GBP", "EUR", "CNY", "BRL", "RUB", "AUD", "NGN",
            "USD", "CHF", "CFA", "DKK", "EGP", "GHS", "INR", "ILS", "KES",
            "LRD", "MYR", "JPY"
    };
    private String[] arrayCrypto = {
            "BTC", "ETH"
    };
    private String[] cryptoCurrency = {
            "Bitcoin", "Ethereum"
    };
    private int[] symbol = {
            R.drawable.images1, R.drawable.ethereum
    };
    private String[] countryCurrency = {
            "Canadian Dollar", "British Pound", "Euro", "Chinese Yuan", "Brazilian Real", "Russian Rubble",
            "Australian Dollar", "Nigerian Naira", "US Dollar", "Swiss Franc", "Togolese Franc", "Denmark Krone",
            "Egyptian Pound", "Ghanaian Cedi", "Indian ", "Israeli Shekel", "Kenyan Shilling",
            "Liberian Dollar", "Malaysian", "Japanese Yen"
    };
    private int[] flags = {
            R.drawable.canada, R.drawable.uk, R.drawable.sa, R.drawable.china, R.drawable.bra,
            R.drawable.russia, R.drawable.australia, R.drawable.ngn, R.drawable.usa,
            R.drawable.switzerland, R.drawable.togo, R.drawable.denmark, R.drawable.egypt,
            R.drawable.ghana, R.drawable.india, R.drawable.israel, R.drawable.kenya,
            R.drawable.liberia, R.drawable.malaysia, R.drawable.japan1
    };
    private static final String RESULT_TEXT_VALUE = "resultValue";
    private static final String CURRENT_RATE_TEXT_VALUE = "rateValue";
    private static final String CRYPTO_TEXT_VALUE = "cryptoValue";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        amt = (EditText) findViewById(R.id.editAmount);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        result = (TextView) findViewById(R.id.textResult);
        btExecute = (Button) findViewById(R.id.execute);
        spinnerImage = (ImageView) findViewById(R.id.spinnerImage);
        spinnerImageCrypto = (ImageView) findViewById(R.id.spinnerImageCrypto);
        spinnerText = (TextView) findViewById(R.id.spinnerText);
        spinnerTextCrypto = (TextView) findViewById(R.id.spinnerTextCrypto);
        rate = (TextView) findViewById(R.id.rate);
        cryptoEntered = (TextView) findViewById(R.id.rateCrypto);
        abbCryptoName = (TextView) findViewById(R.id.cryptoName);

        if (savedInstanceState != null) {
            CharSequence savedResult = savedInstanceState.getCharSequence(RESULT_TEXT_VALUE);
            result.setText(savedResult);
            CharSequence savedRate = savedInstanceState.getCharSequence(CURRENT_RATE_TEXT_VALUE);
            rate.setText(savedRate);
            CharSequence savedCrypto = savedInstanceState.getCharSequence(CRYPTO_TEXT_VALUE);
            cryptoEntered.setText(savedCrypto);
        }

        fiatCurr = (Spinner) findViewById(R.id.usd);
        faitArrayList();
        fiatCurr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                to = i;
                spinnerText.setText(countryCurrency[to]);
                Picasso.with(MainActivity.this).load(flags[to]).resize(50, 30).centerCrop()
                        .error(R.drawable.ngn)
                        .into(spinnerImage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        cryptoCurr = (Spinner) findViewById(R.id.btc);
        cryptoArrayList();
        cryptoCurr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                from = i;
                spinnerTextCrypto.setText(cryptoCurrency[from]);
                Picasso.with(MainActivity.this).load(symbol[from]).resize(30, 30).centerCrop()
                        .error(R.drawable.images1)
                        .into(spinnerImageCrypto);

                abbCryptoName.setText(arrayCrypto[from]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btExecute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(amt.getText().toString()) && !amt.getText().toString().equals(".")) {

                    result.setText("");
                    String value = amt.getText().toString();
                    cryptoEntered.setText(value);
                    double iValue = Double.parseDouble(value);

                    cryptoCalculate(arrayCrypto[from], arrayFiat[to], iValue);

                    amt.setText("");
                    amt.setCursorVisible(false);
                    checkInternetConnection();
                    dismissKeyboard();

                } else {
                    Toast.makeText(MainActivity.this, "Not allowed", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    private void faitArrayList() {
        //ArrayList holding the fiat currency items
        ArrayList<ItemObject> list = new ArrayList<>();
        list.add(new ItemObject("Canadian Dollar", R.drawable.canada));
        list.add(new ItemObject("British Pound", R.drawable.uk));
        list.add(new ItemObject("Euro ", R.drawable.sa));
        list.add(new ItemObject("Chinese Yuan", R.drawable.china));
        list.add(new ItemObject("Brazilian Real", R.drawable.bra));
        list.add(new ItemObject("Russian Rubble", R.drawable.russia));
        list.add(new ItemObject("Australian Dollar", R.drawable.australia));
        list.add(new ItemObject("Nigerian Naira", R.drawable.ngn));
        list.add(new ItemObject("U.S Dollar", R.drawable.usa));
        list.add(new ItemObject("Swiss Franc", R.drawable.switzerland));
        list.add(new ItemObject("Togolese Franc", R.drawable.togo));
        list.add(new ItemObject("Denmark Krone", R.drawable.denmark));
        list.add(new ItemObject("Egyptian Pound", R.drawable.egypt));
        list.add(new ItemObject("Ghanian Cedi", R.drawable.ghana));
        list.add(new ItemObject("India Rupee", R.drawable.india));
        list.add(new ItemObject("Israeli New shekel", R.drawable.israel));
        list.add(new ItemObject("Kenyan Shillings", R.drawable.kenya));
        list.add(new ItemObject("Liberian Dollar", R.drawable.liberia));
        list.add(new ItemObject("Malaysian Ringgit", R.drawable.malaysia));
        list.add(new ItemObject("Japan Yen", R.drawable.japan1));
        CountryAdapter baseAdapter = new CountryAdapter(this,
                R.layout.country_layout, R.id.txt, list);
        fiatCurr.setAdapter(baseAdapter);
    }

    private void cryptoArrayList() {
        //ArrayList holding the crypto currency items
        ArrayList<ItemObject> list = new ArrayList<>();
        list.add(new ItemObject("Bitcoin", R.drawable.images1));
        list.add(new ItemObject("Ethereum", R.drawable.ethereum));
        CryptoAdapter cryptoAdapter = new CryptoAdapter(this,
                R.layout.crypto_layout, R.id.txt, list);
        cryptoCurr.setAdapter(cryptoAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(RESULT_TEXT_VALUE, result.getText());
        outState.putCharSequence(CURRENT_RATE_TEXT_VALUE, rate.getText());
        outState.putCharSequence(CRYPTO_TEXT_VALUE, cryptoEntered.getText());
    }

    @SuppressWarnings("deprecation")
    public boolean checkInternetConnection() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connectMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        //Check for network connections
        if (connectMgr.getNetworkInfo(0).getState() ==
                NetworkInfo.State.CONNECTED ||
                connectMgr.getNetworkInfo(0).getState() ==
                        NetworkInfo.State.CONNECTING ||
                connectMgr.getNetworkInfo(1).getState() ==
                        NetworkInfo.State.CONNECTING ||
                connectMgr.getNetworkInfo(0).getState() ==
                        NetworkInfo.State.CONNECTED) {
            progressBar.setVisibility(View.VISIBLE);
            return true;
        } else if (
                connectMgr.getNetworkInfo(0).getState() ==
                        NetworkInfo.State.DISCONNECTED ||
                        connectMgr.getNetworkInfo(1).getState() ==
                                NetworkInfo.State.DISCONNECTED) {

            Toast.makeText(this, " No Internet Connection ", Toast.LENGTH_LONG).show();

            return false;
        }
        return false;
    }

    public void dismissKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)    //Hides the keyboard when the button is pressed
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().
                getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    private void cryptoCalculate(String valCrypto, final String valFait, final double multiplier) {

        String urlString = "https://min-api.cryptocompare.com/data/price?fsym=" + valCrypto + "&tsyms=" + valFait;

        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlString, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String exResult = response.getString(valFait);
                            double fResult = Double.parseDouble(exResult) * multiplier;

                            result.setText(String.valueOf(fResult));
                            rate.setText(exResult);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        progressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        requestQueue.add(jsonObjectRequest);

    }
}
