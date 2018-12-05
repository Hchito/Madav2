package com.madara.mada;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.File;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        VerPerfilFragment.OnFragmentInteractionListener,BusPrestadorFragment.OnFragmentInteractionListener,BusSolicitanteFragment.OnFragmentInteractionListener{

    String name, email;
    Uri photoUrl;
    String uid;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    Intent intent;
    TextView Nombre, Correo;
    ImageView Foto;
    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference mountainsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        getUserProfile();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        Nombre = hView.findViewById(R.id.NavNombre);
        Correo = (TextView) hView.findViewById(R.id.NavCorreo);
        Foto = hView.findViewById(R.id.NavFoto);
        Bundle bundle = getIntent().getExtras();
        int indentificar = bundle.getInt("identificador");
        navigationView.setNavigationItemSelectedListener(this);

        Nombre.setText(name);
        Correo.setText(email);
        DescargarImagen();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment miFragment = new Fragment();
        boolean Seleccionado = false;
        int id = item.getItemId();

        if (id == R.id.nav_import) {
            miFragment = new VerPerfilFragment();
            Seleccionado = true;
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_pres) {
            miFragment = new BusPrestadorFragment();
            Seleccionado = true;
        } else if (id == R.id.nav_soli) {
            miFragment = new BusSolicitanteFragment();
            Seleccionado = true;
        } else if (id == R.id.nav_send) {
            firebaseAuth.getInstance().signOut();
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        if(Seleccionado) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, miFragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getUserProfile() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            name = user.getDisplayName();
            email = user.getEmail();
            photoUrl = user.getPhotoUrl();
            uid = user.getUid();
        }
    }

    public void DescargarImagen() {
        try {
            storage = FirebaseStorage.getInstance();
            storageRef = storage.getReference();
            mountainsRef = storageRef.child("fotos/" + user.getUid() + ".jpg");
            final File localFile = File.createTempFile(user.getUid(), "jpg");
            mountainsRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Foto.setImageURI(Uri.parse(localFile.getPath()));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    Toast.makeText(MenuActivity.this, "Error al descargar foto de peril", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(MenuActivity.this, "No pudimos descargar tu foto de perfil", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
