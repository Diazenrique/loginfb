package pe.edu.upeu.loginfb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends AppCompatActivity {
private CallbackManager callbackManager;
private LoginButton loginButton;
private TextView info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializar Facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());


        setContentView(R.layout.activity_main);
        // Establecer las devoluciones de llamada
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        // Registrar las devoluciones de llamada
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        info.setText(
                                "User ID: "
                                        + loginResult.getAccessToken().
                                        getUserId()
                                        + "n" +
                                        "Auth Token: "
                                        + loginResult.getAccessToken().
                                        getToken()
                        );
                        makeToast(loginResult.getAccessToken().
                                toString());
                    }

                    @Override
                    public void onCancel() {
                        String cancelMessage = "Login Cancelado.";
                        info.setText(cancelMessage);
                        makeToast(cancelMessage);
                    }

                    @Override
                    public void onError(FacebookException error) {
                        String errorMessage = "Login error.";
                        info.setText(errorMessage);
                        makeToast(errorMessage);
                    }
                }
        );
        // Inicializar el Texview
        info = (TextView) findViewById(R.id.info);
        if(isLoggedIn())
            info.setText("User ID: "
                    + AccessToken.getCurrentAccessToken().getUserId());
    }
    /**
     * Comprueba si el usuario ha iniciado sesión en Facebook y el
     token de acceso está activo
     * @return
     */
    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return (accessToken != null) && (!accessToken.isExpired());
    }
    /**
     * datos de interés en el gestor de devolución de llamada
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data)
        ;
    }
    /**
     * creamos los toast
     * @param text
     */
    private void makeToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //los logs de 'instalar' y 'aplicación activa' App Eventos.
        AppEventsLogger.activateApp(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Logs de'app desactivada' App Eventos.
        AppEventsLogger.deactivateApp(this);
    }
}