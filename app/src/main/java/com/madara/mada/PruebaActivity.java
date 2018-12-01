package com.madara.mada;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.madara.mada.Entidades.Prestador;
import com.madara.mada.Entidades.Solicitante;
import com.madara.mada.Entidades.Tipo_de_usuario;
import com.madara.mada.Entidades.Usuario;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PruebaActivity extends AppCompatActivity {

    String UserId, varNombre, varApellido, varDireccion, varTelefono, varTipoUsuario, varOcupacion;
    EditText Nombre, Apellido, Direccion, Telefono, Ocupacion;
    FirebaseUser firebaseUser;
    Bitmap Foto;
    ImageView UFoto;
    Spinner type_user;

    private static final int COD_SELECCIONADA = 10;
    private static final int COD_FOTO = 20;
    private static final String CARPETA_IMAGEN = "imagenes";
    private static final String CARPETA_PRINCIPAL = "misImagenesApp";
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN;
    private String path;
    private Uri miPath, downloadUri;
    //private File file;
    private ProgressDialog progressDialog;

    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference mountainsRef;

    ArrayList<String> tipos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);

        Nombre = findViewById(R.id.ETNombre);
        Apellido = findViewById(R.id.ETApellido);
        Direccion = findViewById(R.id.ETDireccion);
        Telefono = findViewById(R.id.ETTelefono);
        Ocupacion = findViewById(R.id.ETOcupacion);
        UFoto = findViewById(R.id.IVRegistrarU);
        type_user = findViewById(R.id.Tipos_spinner);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog = new ProgressDialog(this);
        tipos = new ArrayList<String>();
        getTypesUsers();
        tipos.add("Seleccione un tipo de usuario"); //RESUELVE EL ERROR DE EL SPINNER
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PruebaActivity.this,
                android.R.layout.simple_spinner_item, tipos);
        type_user.setAdapter(adapter);
        type_user.setSelection(0);
        Ocupacion.setEnabled(false);
        type_user.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    type_user.setEnabled(true);
                    if(position == 2){
                        Ocupacion.setEnabled(true);
                    }
                    else {
                        Ocupacion.setEnabled(false);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void getTypesUsers(){
        final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReferenceFromUrl("https://prueba-e81b4.firebaseio.com/");
        mDatabase.child("Proyect/db/Tipo_Usuario").addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                for(final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    mDatabase.child("Tipo_Usuario/").child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener(){
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                            Tipo_de_usuario ob = new Tipo_de_usuario(snapshot.getValue().toString());
                            tipos.add(ob.gettipo());
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), "Error2",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error1 \n" + databaseError.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }
    public void InsertarInformacion(View view) {
        if (TextUtils.isEmpty(Nombre.getText().toString())) {
            Nombre.requestFocus();
            Toast.makeText(this, "Falta su Nombre", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(Apellido.getText().toString())) {
            Apellido.requestFocus();
            Toast.makeText(this, "Falta su Apellido", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(Direccion.getText().toString())) {
            Direccion.requestFocus();
            Toast.makeText(this, "Falta su Direccion", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(Telefono.getText().toString())) {
            Telefono.requestFocus();
            Toast.makeText(this, "Falta su numero de Telefono", Toast.LENGTH_LONG).show();
            return;
        }
        if (type_user.getSelectedItemPosition() == 0) {
            type_user.setSelection(1);
            Toast.makeText(this, "Seleccione un tipo de usuario", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(Ocupacion.getText().toString())) {
            Ocupacion.setText("0");
        }


        UserId = firebaseUser.getUid();
        varNombre = Nombre.getText().toString().trim();
        varApellido = Apellido.getText().toString().trim();
        varDireccion = Direccion.getText().toString().trim();
        varTelefono = Telefono.getText().toString().trim();
        varTipoUsuario = type_user.getSelectedItem().toString();
        varOcupacion = Ocupacion.getText().toString().trim();
        varOcupacion = varOcupacion.replace(" ", "");
        Step1Registrar();
    }

    public void Step1Registrar(){
        /*
         *
         *       CODIGO PARA SUBIR LA FOTO AL SERVIDOR EN EL APARTADO DE STRONGE, EN UNA CARPETA foto/
         *
         */
        try {
            progressDialog.setMessage("Subiendo imagen...");
            progressDialog.show();
            // FirebaseStringe objet for get instance on it
            storage = FirebaseStorage.getInstance();
            // Create a storage reference from our app
            storageRef = storage.getReference();
            // Create a reference to "mountains.jpg"
            mountainsRef = storageRef.child("fotos/" + UserId + ".jpg");
            // Create a reference to 'images/mountains.jpg'
            final StorageReference mountainImagesRef = storageRef.child("fotos/"+ UserId +".jpg");
            // While the file names are the same, the references point to different files
            mountainsRef.getName().equals(mountainImagesRef.getName());    // true
            mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false

            // Get the data from an ImageView as bytes
            UFoto.setDrawingCacheEnabled(true);
            UFoto.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) UFoto.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = mountainsRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    progressDialog.dismiss();
                    Toast.makeText(PruebaActivity.this, "Error al almacenar la foto" + exception.toString(), Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                    progressDialog.dismiss();
                    Toast.makeText(PruebaActivity.this, "La foto fue almacenada", Toast.LENGTH_LONG).show();
                    /*
                     *
                     *       MANDAMOS LLAMAR EL SIGUIENTE PROCESO SÍ TODO SALE BIEN
                     *
                     */
                    Step2Registrar();
                }
            });
        }catch (Exception e)
        {
            progressDialog.dismiss();
            Toast.makeText(PruebaActivity.this, "No se pudo ingresar la foto" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void Step2Registrar(){
        /*
         *
         *       PROCESO PARA EXTRAER EL URL DE DESCARGA DE LA IMAGEN SUBIDA ANTERIORMENTE PARA ALMACENARLA EN EL PERFIL
         *
         * */
        try{
            progressDialog.setMessage("Espere...");
            progressDialog.show();
            //extraer url de la imagen almacenada
            mountainsRef = storageRef.child("fotos/");
            mountainsRef.child(UserId + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for the user
                    progressDialog.dismiss();
                    downloadUri = uri;
                    Toast.makeText(PruebaActivity.this, "url consegido", Toast.LENGTH_LONG).show();
                    /*
                     *
                     *       SI EL PROCESO SALE BIEN PASA AL SIGUIENTE PROCESO
                     *
                     * */
                    Step3Registrar();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    progressDialog.dismiss();
                    Toast.makeText(PruebaActivity.this, "url no encontrado" + exception.toString(), Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(PruebaActivity.this, "Error al conseguir el url" + e.toString(), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    }

    public void Step3Registrar(){
        /*
         *
         *          INGRESANDO LOS DATOS A LA BASE DE DATOS
         *
         * */
        try{
            progressDialog.setMessage("Ingresando sus datos...");
            progressDialog.show();
            //Para ingresar los datos a la db
            Usuario user = new Usuario(varNombre, varApellido, varDireccion, varTelefono, varTipoUsuario);
            DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
            mDatabase.child("Proyect/db/Usuarios").child(UserId).setValue(user);
            progressDialog.dismiss();
            Toast.makeText(this, "Nodo insertado con exito", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            progressDialog.dismiss();
        }
        //      SIGUIENTE PROCESO
        Step4Registrar();
    }

    public void Step4Registrar(){
        /*
         *
         *      ACTUALIZANDO LOS DATOS DEL PERFIL DE AUTHENTICATION DE FIREBASE
         *
         * */
        try {
            progressDialog.setMessage("Actualizando perfil de usuario...");
            progressDialog.show();
            //Para ingresar los datos de usuario creado
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(varNombre + " " + varApellido)
                    .setPhotoUri(downloadUri)
                    .build();
            firebaseUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //Todo salio bien
                                progressDialog.dismiss();
                                Toast.makeText(PruebaActivity.this, "Perfil actualizado", Toast.LENGTH_LONG).show();
                                Step5Registrar();
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(PruebaActivity.this, "No se puede actualizar perfil", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }catch (Exception e){
            Toast.makeText(PruebaActivity.this, "Error al actualizar perfil", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    }

    public void Step5Registrar(){
        /*
         *
         *       SE LIMPIA EL FORMULARIO
         *
         * */
        switch (type_user.getSelectedItemPosition()){
            case 1:
                //insertar datos en nodo alumnos
                Solicitante obSol = new Solicitante(varNombre, varApellido, varDireccion, varTelefono);
                DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
                mDatabase.child("Proyect/db/Solicitante").child(UserId).setValue(obSol);
                break;
            case 2:
                //insertar datos en nodo asesores
                Prestador obPres = new Prestador(varNombre, varApellido, varDireccion, varTelefono, varOcupacion);
                DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference();
                mDatabase2.child("Proyect/db/Prestador_de_Servicios").child(UserId).setValue(obPres);
                break;
            default:
                break;
        }

        UFoto.setImageResource(R.drawable.usuario);
        Nombre.setText("");
        Apellido.setText("");
        Direccion.setText("");
        Telefono.setText("");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void subirFoto(View view){
        final CharSequence[] opciones={"Tomar Foto","Elegir de Galeria","Cancelar"};
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Elige una Opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")){
                    //abriCamara();
                }else{
                    if (opciones[i].equals("Elegir de Galeria")){
                        Intent intent=new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent, "Seleccione"), COD_SELECCIONADA);
                    }else{
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        builder.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case COD_SELECCIONADA:
                miPath=data.getData();
                UFoto.setImageURI(miPath);
                try {
                    Foto=MediaStore.Images.Media.getBitmap(this.getContentResolver(),miPath);
                    UFoto.setImageBitmap(Foto);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case COD_FOTO:
                MediaScannerConnection.scanFile(this, new String[]{path}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("Path",""+path);
                            }
                        });

                Foto= BitmapFactory.decodeFile(path);
                UFoto.setImageBitmap(Foto);
                break;
        }
    }
}
