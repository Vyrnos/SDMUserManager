package com.g81vdbvf.usermanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;


public class UsersAdapter extends ArrayAdapter<User> {


    UsersAdapter(Context context, List<User> users){
        super(context,0,users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final User user = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_users, parent, false);

        ImageView profilePic = convertView.findViewById(R.id.profilePic);
        TextView nombre = convertView.findViewById(R.id.nombre);
        TextView genero = convertView.findViewById(R.id.genero);
        TextView registrado = convertView.findViewById(R.id.registradoEn);
        final ImageButton localizacion = convertView.findViewById(R.id.imageButton3);
        TextView usuario = convertView.findViewById(R.id.username);
        TextView password = convertView.findViewById(R.id.password);

        if (user == null) throw new AssertionError();
        new DownloadImageTask(profilePic).execute(user.getImage());
        nombre.setText(user.getName());
        genero.setText(user.getGender());
        registrado.setText(user.getRegistered());
        if(usuario != null && password != null){
            usuario.setText(user.getUsername());
            password.setText(user.getPassword());
        }
        if (localizacion != null) {
            localizacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse("geo:0,0?q=" + Uri.encode(user.getLocation()));
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                    intent.setPackage("com.google.android.apps.maps");
                    UsersAdapter.this.getContext().startActivity(intent);
                }
            });
        }

        return convertView;
}

public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    @SuppressLint("StaticFieldLeak")
    ImageView bmImage;

    DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}
}
