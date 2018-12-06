package com.madara.mada;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.madara.mada.Entidades.CalfPerfil;
import com.madara.mada.Entidades.Calificador;
import com.madara.mada.Entidades.CalificadorData;
import com.madara.mada.Entidades.DataUser;
import com.madara.mada.Entidades.PaqEntidades.AdapterComentarios;

import java.io.File;
import java.util.ArrayList;



public class VerPerfilFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    double Total = 0;
    int ContadorComentarios = 0;

    TextView Nombre, Apellido, Telefono, Direccion;
    ImageView Foto, CalfPerfil;
    Bundle bundle;
    String UI;
    ArrayList<Calificador> calificadors;

    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference mountainsRef;
    DatabaseReference mDatabase;
    RecyclerView RComentarios;
    AdapterComentarios adapterComentarios;
    LinearLayoutManager llm;
    FragmentTransaction fragmentTransaction;

    private OnFragmentInteractionListener mListener;

    public VerPerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VerPerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VerPerfilFragment newInstance(String param1, String param2) {
        VerPerfilFragment fragment = new VerPerfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ver_perfil, container, false);
        bundle = getArguments();
        UI = bundle.getString("UI");
        calificadors = new ArrayList<Calificador>();
        Foto = view.findViewById(R.id.IVUserFoto);
        Nombre = view.findViewById(R.id.IVUserNombre);
        Apellido = view.findViewById(R.id.IVUserApellido);
        Telefono = view.findViewById(R.id.IVUserTelefono);
        Direccion = view.findViewById(R.id.IVUserDireccion);
        RComentarios = view.findViewById(R.id.ResultadoComentarios);
        CalfPerfil = view.findViewById(R.id.IVPerfilEstrellasUsuarios);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Proyect/db/Usuarios/").addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                for(final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    mDatabase.child("Usuarios/").child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener(){
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                            try {
                                if(snapshot.getKey().equals(UI)) {
                                    DataUser dataUser = snapshot.getValue(DataUser.class);
                                    Nombre.setText(dataUser.getNombre());
                                    Apellido.setText(dataUser.getApellido());
                                    Telefono.setText(dataUser.getTelefono());
                                    Direccion.setText(dataUser.getDireccion());
                                }
                            }catch (Exception e){
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DescargarImagen(UI);
        //Cargar comentarios

        mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Proyect/db/Calificaciones/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    mDatabase.child(snapshot.getKey() + "/calificador/").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(UI.equals(snapshot.getKey())) {
                                for (final DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    mDatabase.child(snapshot1.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for(final DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                mDatabase.child(snapshot2.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        try {
                                                            final Calificador calificador = new Calificador(snapshot2.getKey(), snapshot2.getValue(CalificadorData.class));
                                                            calificadors.add(calificador);
                                                            Total += calificador.getCalificacion();
                                                            ContadorComentarios++;
                                                            adapterComentarios = new AdapterComentarios(calificadors, getContext());
                                                            adapterComentarios.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    VerPerfilFragment perfilUsuariosFragment = new VerPerfilFragment();
                                                                    fragmentTransaction = getFragmentManager().beginTransaction();
                                                                    Bundle args = new Bundle();
                                                                    args.putString("UI", calificadors.get(RComentarios.getChildAdapterPosition(v)).getId());
                                                                    perfilUsuariosFragment.setArguments(args);
                                                                    fragmentTransaction.replace(R.id.fragment, perfilUsuariosFragment);
                                                                    fragmentTransaction.addToBackStack(null);
                                                                    fragmentTransaction.commit();
                                                                }
                                                            });
                                                            llm = new LinearLayoutManager(getContext());
                                                            llm.setOrientation(LinearLayoutManager.VERTICAL);
                                                            RComentarios.setLayoutManager(llm);
                                                            RComentarios.setAdapter(adapterComentarios);

                                                            switch ((int)(Total/ContadorComentarios))
                                                            {
                                                                case 1:
                                                                    CalfPerfil.setImageResource(R.drawable.cal1);
                                                                    break;
                                                                case 2:
                                                                    CalfPerfil.setImageResource(R.drawable.cal2);
                                                                    break;
                                                                case 3:
                                                                    CalfPerfil.setImageResource(R.drawable.cal3);
                                                                    break;
                                                                case 4:
                                                                    CalfPerfil.setImageResource(R.drawable.cal4);
                                                                    break;
                                                                case 5:
                                                                    CalfPerfil.setImageResource(R.drawable.cal5);
                                                                    break;
                                                                default:
                                                                    CalfPerfil.setImageResource(R.drawable.cal1);
                                                                    break;
                                                            }

                                                        } catch (Exception e) {
                                                            Toast.makeText(getContext(), "Error: \n" + e.toString(), Toast.LENGTH_LONG).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;

    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void DescargarImagen(String UI) {
        try {
            storage = FirebaseStorage.getInstance();
            storageRef = storage.getReference();
            mountainsRef = storageRef.child("fotos/" + UI + ".jpg");
            final File localFile = File.createTempFile(UI, "jpg");
            mountainsRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Foto.setImageURI(Uri.parse(localFile.getPath()));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    Toast.makeText(getContext(), "Error al descargar foto de peril", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(), "No pudimos descargar tu foto de perfil", Toast.LENGTH_LONG).show();
        }
    }
}
