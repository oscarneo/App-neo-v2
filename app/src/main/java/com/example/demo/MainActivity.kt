package com.example.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var firebaseRemoteConfig:FirebaseRemoteConfig? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Inicializar
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder().build()
        firebaseRemoteConfig!!.setConfigSettingsAsync(configSettings)

        //Inicializar valor por defecto
        val defaultValue = HashMap<String,Any>()
        defaultValue["image_link"] = "https://4.bp.blogspot.com/-3nSawuPGEOc/XEcZ3EK0HqI/AAAAAAAACKc/JqT2BTfMneUHcDmoGJ_zumFTLggTNLuZACLcBGAs/s1600/10986-mega-man-x4-playstation-front-cover.jpg"
        defaultValue["btn_enable"] = true
        defaultValue["text"] = "DEFAULT TEXT"
        firebaseRemoteConfig!!.setDefaults(defaultValue)

        //cargar imagen por defecto
        Picasso.get().load("https://4.bp.blogspot.com/-3nSawuPGEOc/XEcZ3EK0HqI/AAAAAAAACKc/JqT2BTfMneUHcDmoGJ_zumFTLggTNLuZACLcBGAs/s1600/10986-mega-man-x4-playstation-front-cover.jpg")
            .into(image_view)

        //evento de actualización
        btn_update.setOnClickListener {
            // acá se puede asignar el tiempo en caché del resultado, si se pone 0 no se hace caché
            firebaseRemoteConfig!!.fetch(0)
                .addOnCompleteListener(this@MainActivity){ task ->
                    if(task.isSuccessful){
                        firebaseRemoteConfig!!.fetchAndActivate()
                        btn_test.isEnabled = firebaseRemoteConfig!!.getBoolean("btn_enable")
                        btn_test.text = firebaseRemoteConfig!!.getString("btn_text")
                        Picasso.get().load(firebaseRemoteConfig!!.getString("image_link")).into(image_view)
                    }else{
                        Toast.makeText(this@MainActivity,""+task.exception!!.message, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
