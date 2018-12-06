package com.madara.mada;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.madara.mada.Entidades.PaqEntidades.AdapterDatos;
import com.madara.mada.Entidades.PaqEntidades.Extend_UFinded;
import com.madara.mada.Entidades.UsersFinded;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BusPrestadorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BusPrestadorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BusPrestadorFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String AuxId;

    EditText Buscar;
    ArrayList<Extend_UFinded> extend_uFindeds;
    RecyclerView RecyclerPrestador;
    DatabaseReference mDatabase;
    ProgressDialog progressDialog;

    private OnFragmentInteractionListener mListener;

    public BusPrestadorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BusPrestadorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BusPrestadorFragment newInstance(String param1, String param2) {
        BusPrestadorFragment fragment = new BusPrestadorFragment();
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
        final View view = inflater.inflate(R.layout.fragment_bus_prestador, container, false);
        progressDialog = new ProgressDialog(getContext());
        mDatabase= FirebaseDatabase.getInstance().getReference();
        Buscar = view.findViewById(R.id.ETBuscar);
        RecyclerPrestador = view.findViewById(R.id.Resultados_busqueda);
        RecyclerPrestador.setLayoutManager(new LinearLayoutManager(getContext()));
        extend_uFindeds = new ArrayList<Extend_UFinded>();
        Buscar.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event){
                if(event.getAction() == KeyEvent.ACTION_DOWN && !Buscar.getText().toString().isEmpty()){
                    extend_uFindeds.clear();
                    progressDialog.setMessage("Buscando...");
                    progressDialog.show();
                    final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("Proyect/db/Prestador_de_Servicios/").addListenerForSingleValueEvent(new ValueEventListener(){
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                            for(final DataSnapshot snapshot: dataSnapshot.getChildren()){
                                mDatabase.child("Prestador_de_Servicios/").child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener(){
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                                        UsersFinded UF = snapshot.getValue(UsersFinded.class);
                                        if(UF.getNombre().contains(Buscar.getText().toString().trim()) ||
                                                UF.getApellido().contains(Buscar.getText().toString().trim())){
                                            extend_uFindeds.add(new Extend_UFinded(snapshot.getKey(), UF));
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError){

                                    }
                                });
                            }
                            AdapterDatos adapterDatos = new AdapterDatos(extend_uFindeds, getContext());
                            adapterDatos.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View v){
                                    Toast.makeText(getContext(),
                                            "UI: "
                                                    + extend_uFindeds.get(RecyclerPrestador.getChildAdapterPosition(v)).getId(),
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                            RecyclerPrestador.setAdapter(adapterDatos);
                            progressDialog.dismiss();

                            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(getActivity().getWindow().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError){
                            progressDialog.dismiss();
                        }
                    });
                    return true;
                }
                else{
                    return false;
                }
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
}
