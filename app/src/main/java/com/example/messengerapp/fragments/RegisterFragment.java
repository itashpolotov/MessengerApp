package com.example.messengerapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.messengerapp.R;
import com.example.messengerapp.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterFragment extends Fragment {

    MaterialEditText username, email, password;
    Button btn_register;
  ProgressBar progressBar;

    //нужен для авторизации
    FirebaseAuth auth;

    //нужен для указания места храния объекта
    DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_register, container, false);

        username = view.findViewById(R.id.username);
        email = view.findViewById(R.id.email_l);
        password = view.findViewById(R.id.password_l);
        btn_register = view.findViewById(R.id.register_btn);
        progressBar=view.findViewById(R.id.progressBarRegister);

        auth = FirebaseAuth.getInstance(); //initialization of auth

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(getContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6) {
                    Toast.makeText(getContext(), "Пароль должен содержать минимум 6 символов", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(progressBar.VISIBLE);
                    register(txt_username, txt_email, txt_password);
                    progressBar.setVisibility(progressBar.GONE);
                }

            }
        });
        return view;
    }
    public void register(final String username, final String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(getContext(), "Регистрация", Toast.LENGTH_SHORT).show();
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            String userId = user.getUid();


                            reference = FirebaseDatabase
                                    .getInstance()
                                    .getReference("Users")
                                    .child(userId);
                           /* HashMap<String, String> map = new HashMap<>();
                            map.put("id", userId);
                            map.put("username", username);
                            map.put("email", email);
*/
                            User user1=new User();
                            user1.setId(userId);
                            user1.setUsername(username);
                            user1.setEmail(email);
                            user1.setImageUrl("default");

                            reference.setValue(user1)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "Регистрация прошла успешно", Toast.LENGTH_SHORT).show();
                                                ViewPager layout=(ViewPager)getActivity().findViewById(R.id.view_pager);
                                                layout.setCurrentItem(0);
                                            }
                                        }
                                    });


                        }
                        else{
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.i("register", task.getException().getMessage());
                        }
                    }
                });


    }
}