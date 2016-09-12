package br.com.thiengo.pagamentosapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

import br.com.thiengo.pagamentosapp.domain.CreditCard;
import br.com.thiengo.pagamentosapp.domain.PaymentConnection;
import br.com.thiengo.pagamentosapp.domain.Product;
import cn.carbs.android.library.MDDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements Observer {
    private Product product;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initProduct();
        initViews( product );
    }

    private void initProduct(){
        product = new Product(
                "6658-3324599755412",
                "TÊNIS ADIDAS BARRICADE COURT 2",
                "Adiwear: Borracha de altíssima durabilidade que permite que a sola não marque o solo./ Adiprene +: Protege a parte dianteira do pé proporcionando./ Adiprene: Proporciona alta absorção de impactos para amortecer e proteger o calcanhar.",
                3,
                69.90,
                R.mipmap.tennis);
    }

    private void initViews( Product product ){
        ((ImageView) findViewById(R.id.img)).setImageResource( product.getImg() );
        ((TextView) findViewById(R.id.name)).setText( product.getName() );
        ((TextView) findViewById(R.id.description)).setText( product.getDescription() );
        ((TextView) findViewById(R.id.stock)).setText( product.getStockString() );
        ((TextView) findViewById(R.id.price)).setText( product.getPriceString() );
    }


    public void buy( View view ){
        new MDDialog.Builder(this)
                .setTitle("Pagamento")
                .setContentView(R.layout.payment)
                .setNegativeButton("Cancelar", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setPositiveButton("Finalizar", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View root = v.getRootView();

                        buttonBuying( true );
                        CreditCard creditCard = new CreditCard( MainActivity.this );
                        creditCard.setCardNumber( getViewContent( root, R.id.card_number ) );
                        creditCard.setName( getViewContent( root, R.id.name ) );
                        creditCard.setMonth( getViewContent( root, R.id.month ) );
                        creditCard.setYear( getViewContent( root, R.id.year ) );
                        creditCard.setCvv( getViewContent( root, R.id.cvv ) );
                        creditCard.setParcels( Integer.parseInt( getViewContent( root, R.id.parcels ) ) );

                        getPaymentToken( creditCard );
                    }
                })
                .create()
                .show();
    }

    private String getViewContent( View root, int id ){
        EditText field = (EditText) root.findViewById(id);
        return field.getText().toString();
    }

    private void getPaymentToken( CreditCard creditCard ){
        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled( true );
        webView.addJavascriptInterface( creditCard, "Android" );
        webView.loadUrl("file:///android_asset/index.html");
    }

    private void showMessage( final String message ){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText( MainActivity.this, message, Toast.LENGTH_LONG ).show();
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        CreditCard creditCard = (CreditCard) o;

        if( creditCard.getToken() == null ){
            buttonBuying( false );
            showMessage( creditCard.getError() );
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.25.221:8888/android-payment/")
                .addConverterFactory( GsonConverterFactory.create() )
                .build();

        PaymentConnection paymentConnection = retrofit.create(PaymentConnection.class);
        Call<String> requester = paymentConnection.sendPayment(
                product.getId(),
                product.getPrice(),
                creditCard.getToken(),
                creditCard.getParcels()
        );

        requester.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                buttonBuying( false );
                showMessage( response.body() );
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                buttonBuying( false );
                Log.e("log", "Error: "+t.getMessage());
            }
        });
    }

    private void buttonBuying( final boolean status ){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String label;

                label = getResources().getString(R.string.button_buy);
                if( status ){
                    label = getResources().getString(R.string.button_buying);
                }

                ((Button) findViewById(R.id.button_buy)).setText(label);
            }
        });
    }
}
