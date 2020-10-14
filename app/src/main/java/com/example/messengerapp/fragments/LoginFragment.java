package com.example.messengerapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.messengerapp.MainActivity;
import com.example.messengerapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.rengwuxian.materialedittext.MaterialEditText;


public class LoginFragment extends Fragment {
MaterialEditText email, password;
Button login_btn;
FirebaseAuth auth;
ProgressBar progressBar;
DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_login, container, false);
        email=view.findViewById(R.id.email_l);
        password=view.findViewById(R.id.password_l);
        login_btn=view.findViewById(R.id.login_btn);
        progressBar=view.findViewById(R.id.progressBarLogin);

        auth=FirebaseAuth.getInstance();

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email=email.getText().toString();
                String txt_password=password.getText().toString();

                if(TextUtils.isEmpty(txt_email)|| TextUtils.isEmpty(txt_password)){
                    Toast.makeText(getContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
                }else{
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                    auth.signInWithEmailAndPassword(txt_email,txt_password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getContext(), "Вы успешно зарегистрировались", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(ProgressBar.GONE);
                                startActivity(new Intent(getActivity(), MainActivity.class));
                                getActivity().finish();

                                } else{
                                    Toast.makeText(getContext(), "Регистрация не удалась", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(ProgressBar.GONE);

                                }
                            }
                        });
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}