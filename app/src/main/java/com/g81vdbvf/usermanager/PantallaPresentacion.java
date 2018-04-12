package com.g81vdbvf.usermanager;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.security.KeyPairGeneratorSpec;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Calendar;
import java.util.List;

import javax.crypto.SecretKey;
import javax.security.auth.x500.X500Principal;

public class PantallaPresentacion extends AppCompatActivity {
    public KeyStore keyStore;
    public static final String ANDROID_KEYSTORE = "AndroidKeyStore";
    public static final String TAG = "PANTALLA_PRESENTACION";

    SecretKey key;
    public static final String MIS_PREFERENCIAS = "com.g81vdbvf.usermanager.login";

    //-------------------------------MÉTODOS PBE------------------------------------//
    String getRawKey() {
        if (key == null) {
            return null;
        }
        return Crypto.toHex(key.getEncoded());
    }

    public SecretKey deriveKey(String password, byte[] salt) {
        return Crypto.deriveKeyPbkdf2(salt, password);
    }

    public String encrypt(String plaintext, String password) {
        byte[] salt = Crypto.generateSalt();
        key = deriveKey(password, salt);
        Log.d("ENCRYPT: ", "Generated key: " + getRawKey());
        return Crypto.encrypt(plaintext, key, salt);
    }
    public String decrypt(String ciphertext, String password) {
        return Crypto.decryptPbkdf2(ciphertext, password);
    }
    //----------------------------FIN MÉTODOS PBE-----------------------------------//

    //----------------------------MÉTODOS KEYSTORE----------------------------------//
    public void loadKeyStore() {
        try {
            keyStore = java.security.KeyStore.getInstance(ANDROID_KEYSTORE);
            keyStore.load(null);
        } catch (Exception e) {
            Log.v(TAG,"Error al cargar el KeyStore");
            e.printStackTrace();
        }
    }

    public void generateNewKeyPair(String alias, Context context) throws Exception {
        if(!keyStore.containsAlias(alias)){
            Log.v(TAG,"SE ENTRÓ PORQUE NO CONTENÍA EL ALIAS");
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            // expires 1 year from today
            end.add(Calendar.YEAR, 1);
            KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
                    .setAlias(alias)
                    .setSubject(new X500Principal("CN=" + alias))
                    .setSerialNumber(BigInteger.TEN)
                    .setStartDate(start.getTime())
                    .setEndDate(end.getTime())
                    .build();
            // use the Android keystore
            KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA",ANDROID_KEYSTORE);
            gen.initialize(spec);
            // generates the keypair
            gen.generateKeyPair();
        }

    }

    public PrivateKey loadPrivateKey(String alias) throws Exception {
        if (!keyStore.isKeyEntry(alias)) {
            Log.e(TAG, "Could not find key alias: " + alias);
            return null;
        }
        KeyStore.Entry entry = keyStore.getEntry(alias, null);
        if (!(entry instanceof KeyStore.PrivateKeyEntry)) {
            Log.e(TAG, " alias: " + alias + " is not a PrivateKey");
            return null;
        }
        return ((KeyStore.PrivateKeyEntry) entry).getPrivateKey();
    }

    public PublicKey loadPublicKey(String alias) throws Exception {
        if (!keyStore.isKeyEntry(alias)) {
            Log.e(TAG, "Could not find key alias: " + alias);
            return null;
        }
        KeyStore.Entry entry = keyStore.getEntry(alias, null);

        if (!(entry instanceof KeyStore.PrivateKeyEntry)) {
            Log.e(TAG, " alias: " + alias + " is not a PrivateKey");
            return null;
        }
        Certificate cert = ((KeyStore.PrivateKeyEntry) entry).getCertificate();


        return cert.getPublicKey();
    }

    //---------------------------FIN MÉTODOS KEYSTORE-----------------------------//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_presentacion);

        List<Login> list = DatabaseInitializer.getLoginList(Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "user-database").fallbackToDestructiveMigration().allowMainThreadQueries().build());

        if(list.isEmpty()) {
            DatabaseInitializer.initializeLogin(Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "user-database").fallbackToDestructiveMigration().allowMainThreadQueries().build());
            list = DatabaseInitializer.getLoginList(Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "user-database").fallbackToDestructiveMigration().allowMainThreadQueries().build());
        }else{
            for (int i = 0; i<list.size(); i++)
            Log.v("ASDASDASDASD", "LA PASS ES: " + list.get(i).getPassword() + " Y EL USER: "+ list.get(i).getUsername());
        }

        //-----VARIABLES COMUNES PARA KEYSTORE Y PBE----//
        String DBNAME;
        SharedPreferences sprefs = getSharedPreferences(MIS_PREFERENCIAS, Context.MODE_PRIVATE);
        //-----------FIN VARIABLES COMUNES--------------//

        //---------------------------------------PARTE KEYSTORE----------------------------------//
        DBNAME = "KEYSTORE.db";
        String claveCifrado = "";

        if(!sprefs.contains("KeyStoreBBDD")){
            loadKeyStore();
            SharedPreferences.Editor editor = sprefs.edit();
            try {
                generateNewKeyPair("keystore", this);
                PublicKey publicKey = loadPublicKey("keystore");
                claveCifrado = Base64.encodeToString(publicKey.getEncoded(),Base64.NO_WRAP);
            } catch (Exception e) {
                e.printStackTrace();
            }
            editor.putString("KeyStoreBBDD",claveCifrado);
            editor.commit();
        }

        if(sprefs.contains("KeyStoreBBDD")){
            SQLCipherDBHelper db = SQLCipherDBHelper.getInstance(this, DBNAME);

            Cursor cursor = db.getAllData();
            cursor.moveToFirst();
            int i = 0;
            while (!cursor.isAfterLast()){
                Log.v("LOSUSUARIOSDE-KEYSTORE",i + " " + cursor.getString(1));
                cursor.moveToNext();
                i++;
            }
            if (i == 0){
                db.insertData("KeyStore1","M", "LocationEjemplo","user123","pass123","11/11/2011","pictureEjemplo");
                db.insertData("KeyStore2","F", "LocationEjemplo2","user1234","pass1234","11/11/2012","pictureEjemplo2");
                db.insertData("KeyStore3","F", "LocationEjemplo3","user1235","pass1235","11/11/2013","pictureEjemplo3");
                db.insertData("KeyStore4","M", "LocationEjemplo4","user1236","pass1236","11/11/2014","pictureEjemplo4");
                db.insertData("KeyStore5","M", "LocationEjemplo5","user1237","pass1237","11/11/2015","pictureEjemplo5");
            }
            cursor.close();
            db.close();
        }

        //-----------------------------------FIN PARTE KEYSTORE----------------------------------//

        //---------------------------------------PARTE PBE---------------------------------------//

        DBNAME = "PBE.db";
        if(!sprefs.contains("PassBBDD")) {
            Log.v("Proceso de CIFRADO: ", "EL PASS ES: " + list.get(0).getPassword());
            key = deriveKey(list.get(0).getPassword(), Crypto.generateSalt());
            SharedPreferences.Editor editor = sprefs.edit();
            Log.v("PASS DE LA BBDD", getRawKey());
            editor.putString("PassBBDD", getRawKey());
            editor.commit();
        }

        if(sprefs.contains("PassBBDD")){
            SQLCipherDBHelper db = SQLCipherDBHelper.getInstance(this, DBNAME);

            Cursor cursor = db.getAllData();
            cursor.moveToFirst();
            int i = 0;
            while (!cursor.isAfterLast()){
                Log.v("LOSUSUARIOSDE-PBE",i + " " + cursor.getString(1));
                cursor.moveToNext();
                i++;
            }
            if (i == 0){
                db.insertData("NombreEjemplo","M", "LocationEjemplo","user123","pass123","11/11/2011","pictureEjemplo");
                db.insertData("NombreEjemplo2","F", "LocationEjemplo2","user1234","pass1234","11/11/2012","pictureEjemplo2");
                db.insertData("NombreEjemplo3","F", "LocationEjemplo3","user1235","pass1235","11/11/2013","pictureEjemplo3");
                db.insertData("NombreEjemplo4","M", "LocationEjemplo4","user1236","pass1236","11/11/2014","pictureEjemplo4");
                db.insertData("NombreEjemplo5","M", "LocationEjemplo5","user1237","pass1237","11/11/2015","pictureEjemplo5");
            }
            cursor.close();
            db.close();
        }
        //-----------------------------------FIN PARTE PBE---------------------------------------//

        Thread reloj = new Thread(){
            public void run(){
                try {
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally {
                    Intent abrirPrincipal = new Intent("android.intent.action.LOGIN");
                    startActivity(abrirPrincipal);
                }
            }

        };
        reloj.start();
    }
}

