package com.madara.mada.Entidades.PaqEntidades;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.madara.mada.Entidades.Calificador;
import com.madara.mada.R;

import java.io.File;
import java.util.ArrayList;

public class AdapterComentarios extends RecyclerView.Adapter<AdapterComentarios.ViewHolderComentarios>
        implements View.OnClickListener {
    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference mountainsRef;
    ArrayList<Calificador> calificadors;
    private View.OnClickListener onClickListener;
    Context context;

    public AdapterComentarios(ArrayList<Calificador> calificadors, Context context) {
        this.calificadors = calificadors;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderComentarios onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //Nos enlaza el adaptador con el itemlistcomentarios.xml
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.itemlistcomentarios, null, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setOnClickListener(this);
        return new ViewHolderComentarios(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderComentarios viewHolderComentarios, int i) {
        //put your code here
        if(calificadors.get(i).getId() != null) {
            cargarImagenFirebase(calificadors.get(i).getId(), viewHolderComentarios);
        }
        else {
            viewHolderComentarios.Foto.setImageResource(R.drawable.usuario);
        }
        viewHolderComentarios.Nombre.setText(calificadors.get(i).getNombre() + " " + calificadors.get(i).getApellido());
        switch ((int)calificadors.get(i).getCalificacion()){
            case 1:
                viewHolderComentarios.Estrellas.setImageResource(R.drawable.cal1);
                break;
            case 2:
                viewHolderComentarios.Estrellas.setImageResource(R.drawable.cal2);
                break;
            case 3:
                viewHolderComentarios.Estrellas.setImageResource(R.drawable.cal3);
                break;
            case 4:
                viewHolderComentarios.Estrellas.setImageResource(R.drawable.cal4);
                break;
            case  5:
                viewHolderComentarios.Estrellas.setImageResource(R.drawable.cal5);
                break;
            default:
                viewHolderComentarios.Estrellas.setImageResource(R.drawable.cal1);
                break;
        }
        viewHolderComentarios.Comentarios.setText(calificadors.get(i).getComentario());
    }

    @Override
    public int getItemCount() {
        return calificadors.size();
    }

    public void cargarImagenFirebase(String ruta, final ViewHolderComentarios viewHolderComentarios){
        try {
            storage = FirebaseStorage.getInstance();
            storageRef = storage.getReference();
            mountainsRef = storageRef.child("fotos/" + ruta + ".jpg");
            final File localFile = File.createTempFile(ruta, "jpg");
            mountainsRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    try{
                        viewHolderComentarios.Foto.setImageURI(Uri.parse(localFile.getPath()));
                        localFile.delete();
                    } catch (Exception e){
                        Log.e("RecyclerNoSetFoto", e.toString());
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    Toast.makeText(context, "Error al descargar foto de peril \n" + exception.toString(), Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(context, "No pudimos descargar tu foto de perfil \n" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    @Override
    public void onClick(View v) {
        if(onClickListener != null){
            onClickListener.onClick(v);
        }
    }

    public class ViewHolderComentarios extends RecyclerView.ViewHolder {

        ImageView Foto, Estrellas;
        TextView Nombre, Comentarios;

        public ViewHolderComentarios(@NonNull View itemView) {
            super(itemView);
            Foto = itemView.findViewById(R.id.IVPComentariosFotos);
            Nombre = itemView.findViewById(R.id.TVPComentariosNombre);
            Estrellas = itemView.findViewById(R.id.IVPComentariosEstrellas);
            Comentarios = itemView.findViewById(R.id.TVPComentariosComentarios);
        }
    }
}
